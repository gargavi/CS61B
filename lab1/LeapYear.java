public class LeapYear {
	int year = 2000; 

	public static void main (String[] args) { 
		if (isLeapYear(year)) {
			System.out.println(year + "is a leap year.")
		}



	}
	public static boolean isLeapYear(Int giv_year){ 
		if (year % 400 == 0 ){ 
			return true }
		if (year % 4 == 0 && year % 100 != 0 ) { 
			return true }
		return false; 
	}


 }