package hylocal;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class InputTestCases {

	@Test
	public void testDates() {
		String dateOne = "03/10/2015";
		String dateTwo = "03/14/2015";
		String dateThree = "03/32/2015";
		String dateFour = "03-04-2015";
		String dateFive = "04/31/2015";
		String dateSix = "02/29/2015";
		boolean validOne = true;
		boolean validTwo = true;
		boolean validThree = false;
		boolean validFour = false;
		boolean validFive = false;
		boolean validSix = false;
		assertEquals(Main.isValidDate(dateOne), validOne);
		assertEquals(Main.isValidDate(dateTwo), validTwo);
		assertEquals(Main.isValidDate(dateThree), validThree);
		assertEquals(Main.isValidDate(dateFour), validFour);
		assertEquals(Main.isValidDate(dateFive), validFive);
		assertEquals(Main.isValidDate(dateSix), validSix);
		
		assertEquals(true, Main.laterDate(dateOne, dateTwo));
		assertEquals(false, Main.laterDate(dateTwo, dateOne));
	}

	@Test
	public void testTimes() {
		String timeOne = "1305a";
		String timeTwo = "1111a";
		String timeThree = "420p";
		
		String timeFour = "1213a";
		String timeFive = "1213p";
		
		String timeSix = "1296a";
		
		assertEquals(false, Main.isValidTime(timeOne));
		assertEquals(true, Main.isValidTime(timeTwo));
		assertEquals(true, Main.isValidTime(timeThree));
		
		assertEquals(true, Main.laterTime(timeTwo, timeThree));
		assertEquals(false, Main.laterTime(timeThree, timeTwo));
		
		assertEquals(true, Main.laterTime(timeFour, timeFive));
		assertEquals(false, Main.laterTime(timeFive, timeFour));
		
		assertEquals(false, Main.isValidTime(timeSix));
	}
	
	@Test 
	public void testReadWrite() {
		int[][] dates = {{2015, 5, 6}, {2015, 5, 6}};
		int[][] times = {{14, 30, 0}, {17, 45, 0}};
		EventObject original = new EventObject("Beta Testing", "Home", 0, 9, dates, times);
		assertEquals(true, ICSCreator.createICSFile("test", original));
		EventObject readIn = ICSReader.readICSFile("test");
		assertEquals(original.getClassification(), readIn.getClassification());
		assertEquals(true, original.getLocation().equals(readIn.getLocation()));
		assertEquals(true, original.getSummary().equals(readIn.getSummary()));
		assertEquals(original.getDates()[0][0], readIn.getDates()[0][0]);
		assertEquals(original.getDates()[0][1], readIn.getDates()[0][1]);
		assertEquals(original.getDates()[0][2], readIn.getDates()[0][2]);
		assertEquals(original.getDates()[1][0], readIn.getDates()[1][0]);
		assertEquals(original.getDates()[1][1], readIn.getDates()[1][1]);
		assertEquals(original.getTimes()[1][2], readIn.getTimes()[1][2]);
		assertEquals(original.getTimes()[0][0], readIn.getTimes()[0][0]);
		assertEquals(original.getTimes()[0][1], readIn.getTimes()[0][1]);
		assertEquals(original.getTimes()[0][2], readIn.getTimes()[0][2]);
		assertEquals(original.getTimes()[1][0], readIn.getTimes()[1][0]);
		assertEquals(original.getTimes()[1][1], readIn.getTimes()[1][1]);
		assertEquals(original.getTimes()[1][2], readIn.getTimes()[1][2]);
	}
	
	@Test
	public void freeTimeTests1() {
		int[][] time1 = {{17, 45, 0}, {18, 30, 0}}, 
				time2 = {{18, 15, 0}, {20, 45, 0}},
				time3 = {{21, 0, 0}, {22, 15, 0}},
				free13 = {{18, 30, 0}, {21,0,0}},
				free23 = {{20,45,0}, {21, 0, 0}};
		assertEquals(free13[0][0], FreeTimeFinder.calcFreeTime(time1, time3)[0][0]);
		assertEquals(free13[0][1], FreeTimeFinder.calcFreeTime(time1, time3)[0][1]);
		assertEquals(free13[0][2], FreeTimeFinder.calcFreeTime(time1, time3)[0][2]);
		assertEquals(free13[1][0], FreeTimeFinder.calcFreeTime(time1, time3)[1][0]);
		assertEquals(free13[1][1], FreeTimeFinder.calcFreeTime(time1, time3)[1][1]);
		assertEquals(free13[1][2], FreeTimeFinder.calcFreeTime(time1, time3)[1][2]);
		
		assertEquals(free23[0][0], FreeTimeFinder.calcFreeTime(time2, time3)[0][0]);
		assertEquals(free23[0][1], FreeTimeFinder.calcFreeTime(time2, time3)[0][1]);
		assertEquals(free23[0][2], FreeTimeFinder.calcFreeTime(time2, time3)[0][2]);
		assertEquals(free23[1][0], FreeTimeFinder.calcFreeTime(time2, time3)[1][0]);
		assertEquals(free23[1][1], FreeTimeFinder.calcFreeTime(time2, time3)[1][1]);
		assertEquals(free23[1][2], FreeTimeFinder.calcFreeTime(time2, time3)[1][2]);
	}
	
	@Test
	public void freeTimeTests2() {
		ArrayList<EventObject> list = new ArrayList<>();
		int[][] dates = {{2015, 8, 8}, {2015, 8, 8}},
				times1 = {{16, 0, 0}, {23, 59, 0}},
				times2 = {{13, 30, 0}, {15, 0, 0}},
				times3 = {{15, 30, 0}, {15, 45, 0}},
				intendedResult = {{15, 0, 0},{16, 0, 0}};
		list.add(new EventObject("Partayy!!!", "Club", 0, 9, dates, times1));
		list.add(new EventObject("Turn down fo' homework", "Dorms", 1, 9, dates, times2));
		assertEquals(intendedResult[0][0], FreeTimeFinder.findFreeTime(list).get(0)[0][0]);
		assertEquals(intendedResult[0][1], FreeTimeFinder.findFreeTime(list).get(0)[0][1]);
		assertEquals(intendedResult[0][2], FreeTimeFinder.findFreeTime(list).get(0)[0][2]);
		assertEquals(intendedResult[1][0], FreeTimeFinder.findFreeTime(list).get(0)[1][0]);
		assertEquals(intendedResult[1][1], FreeTimeFinder.findFreeTime(list).get(0)[1][1]);
		assertEquals(intendedResult[1][2], FreeTimeFinder.findFreeTime(list).get(0)[1][2]);
		list.add(new EventObject("Horsing around", "Dorms", 2, 9, dates, times3));
		assertEquals(15, FreeTimeFinder.findFreeTime(list).get(0)[0][0]);
		assertEquals(0, FreeTimeFinder.findFreeTime(list).get(0)[0][1]);
		assertEquals(0, FreeTimeFinder.findFreeTime(list).get(0)[0][2]);
		assertEquals(15, FreeTimeFinder.findFreeTime(list).get(0)[1][0]);
		assertEquals(30, FreeTimeFinder.findFreeTime(list).get(0)[1][1]);
		assertEquals(0, FreeTimeFinder.findFreeTime(list).get(0)[1][2]);
		
		assertEquals(15, FreeTimeFinder.findFreeTime(list).get(1)[0][0]);
		assertEquals(45, FreeTimeFinder.findFreeTime(list).get(1)[0][1]);
		assertEquals(0, FreeTimeFinder.findFreeTime(list).get(1)[0][2]);
		assertEquals(16, FreeTimeFinder.findFreeTime(list).get(1)[1][0]);
		assertEquals(0, FreeTimeFinder.findFreeTime(list).get(1)[1][1]);
		assertEquals(0, FreeTimeFinder.findFreeTime(list).get(1)[1][2]);
	}
}
