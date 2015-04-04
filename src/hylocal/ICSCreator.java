package hylocal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.TimeZone;

public class ICSCreator {
	public static boolean createICSFile(String fn, int cls, String loc, int pri, String sum, int[][] dates, int[][] times) {
		boolean retval = false;
		try {
			File f = new File(fn + ".ics");
			if (!f.exists()) {
				f.createNewFile();
			}
			PrintWriter out = new PrintWriter(f);
			out.println("BEGIN:VCALENDAR");
			out.println("VERSION:2.0");
			out.println("BEGIN:VEVENT");
			String cls_str = (cls % 2 == 0) ? (cls == 0 ? "PUBLIC" : "CONFIDENTIAL") : "PRIVATE";
			out.println("CLASS:" + cls_str);
			if (!loc.equals(""))
			{
				out.println("LOCATION:" + loc);
			}
			out.println("PRIORITY:" + String.valueOf(pri));
			out.println("SUMMARY:" + sum);
			String timeZone = TimeZone.getDefault().getID();
			for (int i = 0; i < 2; i++) {
				String startend = (i == 0) ? "DTSTART;TZID=" : "DTEND;TZID=";
				out.println(startend + timeZone + ":" +
						  String.format("%04d", dates[i][0]) + 
						  String.format("%02d", dates[i][1]) + 
						  String.format("%02d", dates[i][2]) + "T" +
						  String.format("%02d", times[i][0]) + 
						  String.format("%02d", times[i][1]) + 
						  String.format("%02d", times[i][2]));
			}
			out.println("END:VEVENT");
			out.println("END:VCALENDAR");
			out.close();
			retval = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return retval;
	}
	
	public static boolean createICSFile(String fn, EventObject e) {
		return createICSFile(fn, e.getClassification(), e.getLocation(), e.getPriority(), e.getSummary(), e.getDates(), e.getTimes());
	}
}
