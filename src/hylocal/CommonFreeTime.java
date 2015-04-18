package hylocal;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class CommonFreeTime {
	public static boolean writeToFile(ArrayList<String> list) {
		boolean retval = true;
		ArrayList<int[][]> freeTimes = commonFreeTime(list, true);
		ArrayList<EventObject> dummy = readInLists(list.get(0));
		String fn_stem = "";
		for (String s : list) {
			fn_stem += s + "_";
		}
		for (int p = 0; p < freeTimes.size(); p++) {
			EventObject eo = new EventObject("Common Free Time", "", 1, 9, dummy.get(0).getDates(), freeTimes.get(p));
			retval &= ICSCreator.createICSFile("Hylocal_" + fn_stem + "_FreeTime_" + p, eo);
		}
		return retval;
	}
	
	public static boolean writeToFile(String one, String two) {
		boolean retval = false;
		ArrayList<int[][]> freeTimes = commonFreeTime(one, two);
		ArrayList<EventObject> dummy = readInLists(one);
		for (int i = 0; i < freeTimes.size(); i++) {
			EventObject eo = new EventObject("Common Free Time", "", 1, 9, dummy.get(0).getDates(), freeTimes.get(i));
			ICSCreator.createICSFile("Hylocal_" + one + "_" + two + "_FreeTime_" + i, eo);
		}
		return retval;
	}
	
	public static ArrayList<int[][]> commonFreeTime(ArrayList<String> list, boolean dummy){
		ArrayList<int[][]> retval = new ArrayList<>();
		ArrayList<EventObject> l = readInLists(list);
		retval = FreeTimeFinder.findFreeTime(l);
		return retval;
	}
	
	public static ArrayList<int[][]> commonFreeTime(String one, String two) {
		ArrayList<int[][]> retval = new ArrayList<>();
		if (FreeTimeFinder.writeOutFreeTimeICSFiles(one, true) &&
		    FreeTimeFinder.writeOutFreeTimeICSFiles(two, true)) {
			ArrayList<EventObject> uno = readInLists(one);
			ArrayList<EventObject> dos = readInLists(two);
			retval = commonFreeTime(uno, dos, true);
		}
		return retval;
	}
	
	public static ArrayList<int[][]> commonFreeTime(ArrayList<EventObject> one, ArrayList<EventObject> two, boolean dummy) {
		ArrayList<int[][]> retval = new ArrayList<>();
		ArrayList<int[][]> uno = new ArrayList<>();
		ArrayList<int[][]> dos = new ArrayList<>();
		for (EventObject e : one) {
			uno.add(e.getTimes());
		}
		for (EventObject f : two) {
			dos.add(f.getTimes());
		}
		retval = commonFreeTime(uno, dos);
		return retval;
	}
	
	public static ArrayList<int[][]> commonFreeTime(ArrayList<ArrayList<int[][]>> list) {
	    ArrayList<int[][]> retval = new ArrayList<>();
	    int p = 1;
	    while (p < list.size()) {
	        if (p == 1) retval = commonFreeTime(list.get(0), list.get(1));
	        else retval = commonFreeTime(retval, list.get(p));
	        p++;
	    }
	    return retval;
	}

	public static ArrayList<int[][]> commonFreeTime(ArrayList<int[][]> one, ArrayList<int[][]> two) {
	    ArrayList<int[][]> retval = new ArrayList<>();
	    for (int p = 0; p < one.size(); p++) {
	        for (int q = 0; q < two.size(); q++) {
	        	if (overlapOrEqual(one.get(p), two.get(q))) {
	        		int[][] eh = {earlierOrLaterDate(one.get(p)[0], two.get(q)[0], false), 
	        				      earlierOrLaterDate(one.get(p)[1], two.get(q)[1], true)};
	        		retval.add(eh);
	        	}
	        }
	    }
	    return retval;
	}

	public static int[] earlierOrLaterDate(int[] one, int[] two, boolean earlier) {
		int[] retval = new int[3];
		if (earlier) {
			if (one[0] < two[0]) {
				retval[0] = one[0];
				retval[1] = one[1];
				retval[2] = one[2];
			}
			else if (two[0] < one[0]){
				retval[0] = two[0];
				retval[1] = two[1];
				retval[2] = two[2];
			}
			else if (one[0] == two[0]) {
				if (one[1] < two[1]) {
					retval[0] = one[0];
					retval[1] = one[1];
					retval[2] = one[2];
				}
				else if (two[1] < one[1]){
					retval[0] = two[0];
					retval[1] = two[1];
					retval[2] = two[2];
				}
				else {
					retval[0] = one[0];
					retval[1] = one[1];
					retval[2] = one[2];
				}
			}
		}
		else {
			if (one[0] > two[0]) {
				retval[0] = one[0];
				retval[1] = one[1];
				retval[2] = one[2];
			}
			else if (two[0] > one[0]){
				retval[0] = two[0];
				retval[1] = two[1];
				retval[2] = two[2];
			}
			else if (one[0] == two[0]) {
				if (one[1] > two[1]) {
					retval[0] = one[0];
					retval[1] = one[1];
					retval[2] = one[2];
				}
				else if (two[1] > one[1]){
					retval[0] = two[0];
					retval[1] = two[1];
					retval[2] = two[2];
				}
				else {
					retval[0] = one[0];
					retval[1] = one[1];
					retval[2] = one[2];
				}
			}
		}
		return retval;
	}
	
	public static boolean earlierOrEqual(int[] one, int[] two) {
		boolean retval = false;
		if (one[0] < two[0]) retval = true;
		else if (one[0] == two[0]) {
			if (one[1] <= two[1]) {
				retval = true;
			}
		}
		return retval;
	}
	
	private static boolean overlapOrEqual(int[][] one, int[][] two) {
		return (earlierOrEqual(one[1], two[0]) || earlierOrEqual(two[1], one[0]));
	}

	private static ArrayList<EventObject> readInLists(ArrayList<String> fn) {
		ArrayList<EventObject> eo = new ArrayList<>();
		ArrayList<String> eh = new ArrayList<>();
		for (String f : fn) {
			try {
				File fi = new File(f);
				if (!fi.exists()) {
					throw new FileNotFoundException();
				}
				Scanner in = new Scanner(fi);
				
				while (in.hasNextLine()){ 
					String n = (in.nextLine()); 
					if (n.endsWith(".ics")) {
						eh.add(n);
					}
				}
				in.close();
				for (String h : eh) {
					EventObject p = ICSReader.readICSFile(h.substring(0, h.length() - 4));
					if (p != null) {
						eo.add(p);
					}
				}
			} catch (FileNotFoundException e) {}
		}
		return eo;
	}
	
	private static ArrayList<EventObject> readInLists(String fn) {
		ArrayList<EventObject> o = new ArrayList<>();
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
			
			for (String s : filenames) {
				EventObject eh = ICSReader.readICSFile(s.substring(0, s.length() - 4));
				if (eh != null) {
					o.add(eh);
				}
			}
		}
		catch (FileNotFoundException e) {
		
		}
		return o;
	}
}
