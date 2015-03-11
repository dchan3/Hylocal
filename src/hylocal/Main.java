package hylocal;

import java.util.Scanner;

public class Main {
	public static void main(String args[]) {
		// int[][] dates = {{2015, 5, 8} , {2015, 5, 8}};
		// int[][] times = {{18, 0, 0}, {20,0,0}};
		// ICSCreator.createICSFile("N-Genir", 0, "Hamilton Library", 9, "Studying for finals", dates, times);
		System.out.println("Welcome to Hylocal 1.0!!!");
		// takeInput();
	}
	
	public static void takeInput() {
		Scanner in = new Scanner(System.in);
		
		String fn = "", loc = "", sum = "", part = "";
		int cls = 0, pri = 0;
		int[][] dates, times;
		
		System.out.println("What is the name of your event? ");
		sum = in.nextLine();
		System.out.println("Where is your event going to be?");
		loc = in.nextLine();
		System.out.println("Is your event going to be during part of only one day? (Y/N)");
		while (part.equals(""))	{
			part = in.nextLine();
			part = !(part.toLowerCase().equals("y") || part.toLowerCase().equals("n")) ? "": part;
			if (part.equals("")) {
				System.out.println("Invalid input detected. Please try again.");
			}
		}
		if (part.toLowerCase().equals("y")) {
			System.out.println("On what day will your event be?");
		}
		else if (part.toLowerCase().equals("n")) {
			System.out.println("On what day will your event begin?");
			System.out.println("On what day will your event end?");
		}
	}
	
	public static boolean isValidDate(String n) {
		boolean retval = false;
		if (n.matches("[0-9]{1,2}+/[0-9]{1,2}+/[0-9]{4}")) {
			int year = Integer.parseInt(n.split("/")[2], 10), 
				month = Integer.parseInt(n.split("/")[0], 10),
				day = Integer.parseInt(n.split("/")[1], 10);
			
			if (year <= 9999) {
				if (month >= 1 && month <= 12) {
					if (day > 28 && day < 32) {
						if (day == 29) {
							if (month == 2 && (year % 4 == 0 && year % 100 != 0)) {
								retval = true;
							}
						}
						else if (day == 30) {
							if (month != 2) {
								retval = true;
							}
						}
						else if (day == 31) {
							if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
								retval = true;
							}
						}
					}
					else if (day >= 1 && day <= 28) {
						retval = true;
					}
				}
			}
		}
		return retval;
	}
	
	public static boolean laterOrSameDate(String j, String k) {
		boolean retval = false;
		int j_year = Integer.parseInt(j.split("/")[0], 10), 
			k_year = Integer.parseInt(k.split("/")[0], 10), 
			j_month = Integer.parseInt(j.split("/")[1], 10), 
			k_month = Integer.parseInt(j.split("/")[1], 10), 
			j_day = Integer.parseInt(j.split("/")[2], 10), 
			k_day = Integer.parseInt(j.split("/")[2], 10);
		if (j_year == k_year) {
			if (j_month == k_month) {
				if (j_day <= k_day) {
					retval = true;
				}
			}
			else if (j_month < k_month) {
				retval = true;
			}
		}
		else if (j_year < k_year) {
			retval = true;
		}
		return retval;
	}
	
	public static boolean isValidTime(String t) {
		boolean retval = false;
		if (t.matches("[0-9]{1,2}[0-5][0-9][apAP]")) {
			int time = Integer.parseInt(t.substring(0, t.length() - 1), 10);
			if (time / 100 <= 12 && time / 100 >= 0) {
				if (time % 100 <= 59 && time % 100 >= 0) {
					retval = true;
				}
			}
		}
		return retval;
	}
	
	public static boolean laterTime(String i, String k) {
		boolean retval = false;
		int i_time = Integer.parseInt(i.substring(0, i.length() - 1), 10), k_time = Integer.parseInt(k.substring(0, k.length() - 1), 10);
		boolean i_ap = i.charAt(i.length() - 1) == 'a';
		int i_hour = i_time / 10;
		boolean k_ap = k.charAt(k.length() - 1) == 'a';
		int k_hour = k_time / 10;
		
		int i_min = i_time % 100, k_min = k_time % 100; 
		if (i_hour == 12) { i_ap = !i_ap; }
		if (k_hour == 12) { k_ap = !k_ap; }
		if (!i_ap) { i_hour = (i_hour + 12) % 24; }
		if (!k_ap) { k_hour = (k_hour + 12) % 24; }
		
		if (i_hour < k_hour) {
			retval = true;
		}
		else if (i_hour == k_hour) {
			if (i_min < k_min) {
				return true;
			}
		}
		return retval;
	}
}
