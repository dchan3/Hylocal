package hylocal;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class FreeTimeFinder {
	public static boolean writeOutFreeTimeICSFiles(String fn) {
		boolean retval = false;
		try {
			File f = new File(fn);
			if (!f.exists()) {
				throw new FileNotFoundException();
			}
			Scanner in = new Scanner(f);
			ArrayList<String> filenames = new ArrayList<>();
			while (in.hasNextLine()) {
				String input = in.nextLine();
				if (input.endsWith(".ics")) {
					filenames.add(input);
				}
			}
			in.close();
			ArrayList<EventObject> o = new ArrayList<>();
			for (String s : filenames) {
				EventObject eh = ICSReader.readICSFile(s.substring(0, s.length() - 4));
				if (eh != null) {
					o.add(eh);
				}
			}
			int[][] day = sortList(o).get(0).getDates();
			ArrayList<int[][]> freeTimeList = findFreeTime(o);
			String fn_stem = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
			int p = 1;
			for (int[][] ft : freeTimeList) {
				EventObject eo = new EventObject("Free Time", "", 1, 9, day, ft);
				ICSCreator.createICSFile("Hylocal_" + fn + "_" + fn_stem + "_" + p, eo);
				p++;
			};
			retval = true;
		} catch (FileNotFoundException e) {
			
		}
		return retval;
	}
	
	public static ArrayList<int[][]> findFreeTime(ArrayList<EventObject> list) {
		ArrayList<int[][]> retval = new ArrayList<>();
		list = sortList(list);
		for (int l = 0; l < list.size() - 1; l++) {
			if (isThereFreeTimeBetween(list.get(l), list.get(l + 1))) {
				retval.add(calcFreeTime(list.get(l), list.get(l + 1)));
			}
		}
		return retval;
	}

	public static ArrayList<EventObject> sortList(ArrayList<EventObject> list) {
		ArrayList<EventObject> retval = list;
		for (int i = 1; i <= retval.size() - 1; i++) {
			EventObject temp = retval.get(i);
			int j = i;
			while (j > 0 && !laterTimes(retval.get(j - 1).times[0], temp.times[0])) {
				retval.set(j, retval.get(j - 1));
				j--;
				retval.set(j, temp);
			}
		}
		return retval;
	}
	
	public static int[][] calcFreeTime(EventObject e1, EventObject e2) {
		int[][] retval = {{0,0,0}, {0,0,0}};
		if (isThereFreeTimeBetween(e1, e2)) {
			retval = calcFreeTime(e1.getTimes(), e2.getTimes());
		}
		return retval;
	}
	
	public static int[][] calcFreeTime(int[][] uno, int[][] dos) {
		int[][] retval = {uno[1], dos[0]};
		if (overlap(uno, dos) && !laterTimes(uno[0], dos[0])) {
			for (int u = 0; u < 1; u++) {
				for (int v = 0; v < 2; v++) {
					retval[u][v] = 0;
				}
			}
		}
		return retval;
	}
	
	private static boolean isThereFreeTimeBetween(EventObject e1, EventObject e2) {
		boolean retval = false;
		if (dayIsSame(e1.getDates(), e2.getDates())) {
			if (!overlap(e1.times, e2.times)) {
				return true;
			}
		}
		return retval;
	}
	
	private static boolean dayIsSame(int[][] d1, int[][] d2) {
		return (d1[0][0] == d2[0][0]) &&
			   (d1[0][1] == d2[0][1]) &&
			   (d1[0][2] == d2[0][2]) &&
			   (d1[1][0] == d2[1][0]) &&
			   (d1[1][1] == d2[1][1]) &&
			   (d1[1][2] == d2[1][2]);
	}
	
	private static boolean laterTimes(int[] t1, int t2[]) {
		boolean retval = false;
		if (t1[0] < t2[0]) {
			retval = true;
		}
		else if (t1[0] == t2[0]) {
			if (t1[1] < t2[1]) {
				retval = true;
			}
			else if (t1[1] == t2[1]) {
				if (t1[2] <= t2[2]) {
					retval = true;
				}
			}
		}
		return retval;
	}
	
	private static boolean overlap(int[][] t1, int[][] t2) {
		return (laterTimes(t1[0], t2[0]) && !laterTimes(t1[1], t2[0]));
	}
}
