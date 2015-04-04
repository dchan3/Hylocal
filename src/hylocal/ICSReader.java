package hylocal;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ICSReader {
	public static EventObject readICSFile(String fn) {
		EventObject retval = null;
		try {
			File f = new File(fn + ".ics");
			if (!f.exists()) {
				throw new FileNotFoundException();
			}
			Scanner in = new Scanner(f);
			String input = in.nextLine();
			if (!input.equals("BEGIN:VCALENDAR")) {
				in.close();
				throw new Exception();
			}
			input = in.nextLine();
			if (!input.equals("VERSION:2.0")) {
				in.close();
				throw new Exception();
			}
			input = in.nextLine();
			if (!input.equals("BEGIN:VEVENT")) {
				in.close();
				throw new Exception();
			}
			
			int cls = -1, pri = -1;
			int[][] dates = {{0,0,0}, {0,0,0}}, times = {{0,0,0}, {0,0,0}};
			String loc = "", sum = "", timezone = "", tempTimezone1 = "", tempTimezone2 = "";
			while (in.hasNextLine() && !input.equals("END:VEVENT")) {
				input = in.nextLine();
				if (input.startsWith("LOCATION:")) {
					loc = getSummaryAndLocation(input);
				}
				if (input.startsWith("SUMMARY:")) {
					sum = getSummaryAndLocation(input);
				}
				if (input.startsWith("PRIORITY:")) {
					pri = parsePriority(input);
				}
				if (input.startsWith("CLASS:")) {
					cls = parseClass(input);
				}
				if (input.startsWith("DTSTART;")) {
					tempTimezone1 = parseTimezone(input);
					dates[0] = parseDate(input);
					times[0] = parseTime(input);
				}
				if (input.startsWith("DTEND;")) {
					tempTimezone2 = parseTimezone(input);
					dates[1] = parseDate(input);
					times[1] = parseTime(input);
				}
			}
			in.close();
			if (allGood(sum, loc, tempTimezone1, tempTimezone2, cls, pri, dates, times)) {
				timezone = tempTimezone1;
				retval = new EventObject(sum, loc, timezone, cls, pri, dates, times); 
			}
			else throw new Exception();
		}
		catch (FileNotFoundException e){
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retval;
	}
	
	private static boolean allGood(String s, String l, String tz1, String tz2, int c, int p, int[][] d, int[][] t) {
		return (!s.equals("")) && (!l.equals("")) && (tz1.equals(tz2)) && 
				(c != -1 && c >= 0 && c <= 2) && (p != -1 && p >= 0 && p <= 9) &&
				(validateInput(d, t));
	}
	
	private static boolean validateInput(int[][] d, int[][] t) {
		boolean retval = false;
		if (isLaterOrSameDate(d)) {
			if (d[0][0] == d[1][0] && d[0][1] == d[1][1] && d[0][2] == d[1][2]) {
				retval = isLaterTime(t);
			}
			else retval = true;
		}
		return retval;
	} 
	
	private static boolean isLaterOrSameDate(int[][] d) {
		boolean retval = false;
		if (d[0][0] < d[1][0]) {
			retval = true;
		}
		else if (d[0][0] == d[1][0]) {
			if (d[0][1] < d[1][1]) {
				retval = true;
			}
			else if (d[0][1] == d[1][1]) {
				if (d[0][2] <= d[1][2]) {
					retval = true;
				}
			}
		}
		return retval;
	}
	
	private static boolean isLaterTime(int[][] t) {
		boolean retval = false;
		if (t[0][0] < t[1][0]) {
			retval = true;
		}
		else if (t[0][0] == t[0][0]) {
			if (t[0][1] < t[1][1]) {
				retval = true;
			}
			else if (t[0][1] == t[1][1]) {
				if (t[0][2] < t[1][2]) {
					retval = true;
				}
			}
		}
		return retval;
	}
	
	private static int parseClass(String line) {
		int retval = -1;
		if (line.startsWith("CLASS:")) {
			String p = line.split(":")[1];
			retval = (p.equals("CONFIDENTIAL")) ? 2 : (p.equals("PUBLIC") ? 0 : (p.equals("PRIVATE") ? 1 : -1));
		}
		return retval;
	}
	
	private static int parsePriority(String line) {
		int retval = -1;
		if (line.startsWith("PRIORITY:")) {
			retval = Integer.parseInt(line.split(":")[1], 10);
		}
		return retval;
	}
	
	private static int[] parseDate(String line) {
		int[] retval = new int[3];
		String date = line.split(":")[1].split("T")[0];
		retval[0] = Integer.parseInt(date.substring(0,4),10);
		retval[1] = Integer.parseInt(date.substring(4,6), 10);
		retval[2] = Integer.parseInt(date.substring(6,8), 10);
		return retval;
	}
	
	private static int[] parseTime(String line) {
		int[] retval = new int[3];
		String time = line.split(":")[1].split("T")[1];
		retval[0] = Integer.parseInt(time.substring(0,2),10);
		retval[1] = Integer.parseInt(time.substring(2,4), 10);
		retval[2] = Integer.parseInt(time.substring(4,6), 10);
		return retval;
	}
	
	private static String parseTimezone(String line) {
		return line.split("\\=")[1].split(":")[0];
	}
	
	private static String getSummaryAndLocation(String line) {
		return line.split(":")[1];
	}
}
