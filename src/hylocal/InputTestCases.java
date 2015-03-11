package hylocal;

import static org.junit.Assert.*;

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
	}

	@Test
	public void testTimes() {
		String timeOne = "1305a";
		String timeTwo = "1111a";
		String timeThree = "420p";
		assertEquals(false, Main.isValidTime(timeOne));
		assertEquals(true, Main.isValidTime(timeTwo));
		assertEquals(true, Main.isValidTime(timeThree));
	}
}
