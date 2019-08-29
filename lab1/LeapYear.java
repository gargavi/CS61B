public class LeapYear {

	public static void main (String[] args) {
		int year = 1900;
		if (isLeapYear(year)) {
			System.out.println(year + " is a leap year."); 
		}
		else {
			System.out.println(year + " is not a leap year.");
		}



	}
	public static boolean isLeapYear(int giv_year){ 
		if (giv_year % 400 == 0 ){ 
			return true; }
		if (giv_year % 4 == 0 && giv_year % 100 != 0 ) { 
			return true; }
		return false; 
	}


 }