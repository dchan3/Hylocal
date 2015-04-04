package hylocal;

import java.util.Scanner;

public class Main {
	static Scanner in;
	
	public static void main(String args[]) {
		in = new Scanner(System.in);
		System.out.println("Welcome to Hylocal 1.0!!!");
		System.out.println("I take parameters for an event and output them to files.");
		System.out.println("---------");
		mainMenu();
	}
	
	public static void mainMenu() {
		boolean exit = false;
		System.out.println("How may I help you today?");
		while (!exit) {
			System.out.println("Type in 1 to create an event.");
			System.out.println("Type in 2 to find free time given a set of events.");
			System.out.println("Type in X to quit.");
			String input = in.nextLine();
			if (input.equals("1")) {
				takeInputAndWriteFile();
			}
			else if (input.equals("2")) {
				freeTime();
			}
			else if (input.toLowerCase().equals("x")) {
				System.out.println("Have a nice day!");
				System.exit(0);
			}
			else {
				System.out.println("Invalid input. Please try again.");
			}
		}
		
	}
	
	public static void freeTime() {
		System.out.println("What is the name of the text file containing your list of ICS files?");
		String fn = in.nextLine();
		if (FreeTimeFinder.writeOutFreeTimeICSFiles(fn)) System.out.println("Free time files output successful!");
		else System.out.println("Free time files output unsuccessful...");
	}
	
	public static void takeInputAndWriteFile() {
		String fn = "", loc = "", sum = "", part = "", date_in = "", time_in = "";
		int cls = 0, pri = 0;
		int[][] dates = new int[2][3], times = new int [2][3];
		
		System.out.println("What is the name of your event? ");
		sum = in.nextLine();
		System.out.println("Where is your event going to be?");
		loc = in.nextLine();
		System.out.println("Is your event going to be during part of only one calendar day? (Y/N)");
		while (part.equals(""))	{
			part = in.nextLine();
			part = !(part.toLowerCase().equals("y") || part.toLowerCase().equals("n")) ? "": part;
			if (part.equals("")) {
				System.out.println("Invalid input detected. Please try again.");
			}
		}
		if (part.toLowerCase().equals("y")) {
			System.out.println("On what day will your event be? (MM/DD/YYYY)");
			while (!isValidDate(date_in)) {
				date_in = in.nextLine();
				if (!isValidDate(date_in)) {
					System.out.println("Invalid input detected. Please try again.");
				}
			}
			dates[0][0] = Integer.parseInt(date_in.split("/")[2], 10);
			dates[0][1] = Integer.parseInt(date_in.split("/")[0], 10);
			dates[0][2] = Integer.parseInt(date_in.split("/")[1], 10);
			dates[1][0] = Integer.parseInt(date_in.split("/")[2], 10);
			dates[1][1] = Integer.parseInt(date_in.split("/")[0], 10);
			dates[1][2] = Integer.parseInt(date_in.split("/")[1], 10);
			System.out.println("At what time will it start? (HHMM + A/P)");
			while (!isValidTime(time_in) || time_in.equals("1159p")) {
				time_in = in.nextLine();
				if (!isValidTime(time_in)) {
					System.out.println("Invalid input detected. Please try again.");
				}
				else if (time_in.equals("1159p")) {
					System.out.println("You're joking, right? Please try again.");
				}
			}
			String tempOne = time_in;
			time_in = "";
			System.out.println("At what time will it finish? (HHMM + A/P)");
			while (!isValidTime(time_in) || !laterTime(tempOne, time_in)) {
				time_in = in.nextLine();
				if (!isValidTime(time_in)) {
					System.out.println("Invalid input detected. Please try again.");
				}
				else if (!laterTime(tempOne, time_in)) {
					System.out.println("Earlier time detected. Please try again.");
				}
			}
			String tempTwo = time_in;
			times[0] = outToTime(tempOne);
			times[1] = outToTime(tempTwo);
			date_in = "";
			time_in = "";
		}
		else if (part.toLowerCase().equals("n")) {
			String dateOne = "", dateTwo = "", timeOne = "", timeTwo = "";
			date_in = "";
			System.out.println("On what day will your event begin? (MM/DD/YYYY)");
			while (!isValidDate(date_in)) {
				date_in = in.nextLine();
				if (!isValidDate(date_in)) {
					System.out.println("Invalid input detected. Please try again.");
				}
			}
			dateOne = date_in;
			date_in = "";
			System.out.println("At what time will it start? (HHMM + A/P)");
			while (!isValidTime(time_in)) {
				time_in = in.nextLine();
				if (!isValidTime(time_in)) {
					System.out.println("Invalid input detected. Please try again.");
				}
			}
			timeOne = time_in;
			time_in = "";
			System.out.println("On what day will your event end? (MM/DD/YYYY)");
			while (!isValidDate(date_in) || !laterDate(dateOne, date_in)) {
				date_in = in.nextLine();
				if (!isValidDate(date_in)) {
					System.out.println("Invalid input detected. Please try again.");
				}
				else if (!laterDate(dateOne, date_in)) {
					System.out.println("Earlier date detected. Please try again.");
				}
			}
			dateTwo = date_in;
			date_in = "";
			System.out.println("At what time will it finish? (HHMM + A/P)");
			while (!isValidTime(time_in)) {
				time_in = in.nextLine();
				if (!isValidTime(time_in)) {
					System.out.println("Invalid input detected. Please try again.");
				}
			}
			timeTwo = time_in;
			time_in = "";
			dates[0] = outToDate(dateOne);
			dates[1] = outToDate(dateTwo);
			times[0] = outToTime(timeOne);
			times[1] = outToTime(timeTwo);
		}
		System.out.println("How public is it? (Type 0 for PUBLIC, 1 for PRIVATE, and 2 for CONFIDENTIAL.)");
		String int_in = "";
		while (!int_in.matches("[0-2]")) {
			int_in = in.nextLine();
			if (!int_in.matches("[0-2]")) {
				System.out.println("Invalid input detected. Please try again.");
			}
		}
		cls = Integer.parseInt(int_in, 10);
		int_in = "";
		System.out.println("How high of a priority is this to you? (Type 0 for HIGH, 1 for MEDIUM, and 2 for LOW.)");
		while (!int_in.matches("[0-2]")) {
			int_in = in.nextLine();
			if (!int_in.matches("[0-2]")) {
				System.out.println("Invalid input detected. Please try again.");
			}
		}
		pri = Integer.parseInt(int_in, 10) % 2 == 1 ? 5 : (Integer.parseInt(int_in, 10) == 0 ? 1 : 9);
		System.out.println("What would you like to name the event file?");
		fn = in.nextLine();
		System.out.println("Writing to file...");
		String op = ICSCreator.createICSFile(fn, cls, loc, pri, sum, dates, times) ? "File successfully written!" : "File not written!";
		System.out.println(op);
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
	
	public static boolean laterDate(String j, String k) {
		boolean retval = false;
		if (isValidDate(j) && isValidDate(k)) {
			int j_year = Integer.parseInt(j.split("/")[0], 10), 
				k_year = Integer.parseInt(k.split("/")[0], 10), 
				j_month = Integer.parseInt(j.split("/")[1], 10), 
				k_month = Integer.parseInt(k.split("/")[1], 10), 
				j_day = Integer.parseInt(j.split("/")[2], 10), 
				k_day = Integer.parseInt(k.split("/")[2], 10);
			if (j_year == k_year) {
				if (j_month == k_month) {
					if (j_day < k_day) {
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
		if (isValidTime(i) && isValidTime(k)) {
			int i_time = Integer.parseInt(i.substring(0, i.length() - 1), 10), k_time = Integer.parseInt(k.substring(0, k.length() - 1), 10);
			boolean i_ap = i.charAt(i.length() - 1) == 'a';
			int i_hour = i_time / 100;
			boolean k_ap = k.charAt(k.length() - 1) == 'a';
			int k_hour = k_time / 100;
			
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
		}
		return retval;
	}

	public static int[] outToDate(String n) {
		int [] retval = {Integer.parseInt(n.split("/")[2], 10), Integer.parseInt(n.split("/")[0], 10), Integer.parseInt(n.split("/")[1], 10)};
		return retval;
	}
	
	public static int[] outToTime(String n) {
		int i_time = Integer.parseInt(n.substring(0, n.length() - 1), 10);
		boolean i_ap = n.charAt(n.length() - 1) == 'a';
		int i_hour = i_time / 100;
		int i_min = i_time % 100;
		if (i_hour == 12) { i_ap = !i_ap; }
		if (!i_ap) { i_hour = (i_hour + 12) % 24; }
		int[] retval = {i_hour, i_min, 0};
		return retval;
	}
}
