package hylocal;

import java.util.TimeZone;

public class EventObject {
	String summary, location, timezone;
	int classification, priority;
	int[][] dates, times;
	
	public EventObject(String sum, String loc, int cls, int pri, int[][] d, int[][] t) {
		summary = sum;
		location = loc;
		timezone = TimeZone.getDefault().getID();
		classification = cls;
		priority = pri;
		dates = d;
		times = t;
	}
	
	public EventObject(String sum, String loc, String tz, int cls, int pri, int[][] d, int[][] t) {
		summary = sum;
		location = loc;
		timezone = tz;
		classification = cls;
		priority = pri;
		dates = d;
		times = t;
	} 
	
	public String getSummary() {
		return summary;
	}
	
	public void setSummary(String sum) {
		summary = sum;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String loc) {
		location = loc;
	}
	
	public String getTimezone() {
		return timezone;
	}
	
	public void setTimezone(String tz) {
		timezone = tz;
	}
	
	public int getClassification() {
		return classification;
	}
	
	public void setClassification(int cls) {
		classification = cls;
	}
	
	public int getPriority() {
		return priority;
	}
	
	public void setPriority(int pri) {
		priority = pri;
	}
	
	public int[][] getDates() {
		return dates;
	}
	
	public void setDates(int[][] d) {
		dates = d;
	}
	
	public int[][] getTimes() {
		return times;
	}
	
	public void setTimes(int[][] t) {
		times = t;
	}
}
