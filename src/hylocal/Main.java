package hylocal;

public class Main {
	public static void main(String args[]) {
		int[][] dates = {{2015, 5, 8} , {2015, 5, 8}};
		int[][] times = {{18, 0, 0}, {20,0,0}};
		ICSCreator.createICSFile("N-Genir", 0, "Hamilton Library", 9, "Studying for finals", dates, times);
	}
}
