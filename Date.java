package finalproject;
import java.time.LocalDate;

//Khanh Hua - e1900304
/*
 * I reuse this Class from assignment 2
 * This Class is mainly used for comparing between two dates
 */
public class Date {
	private int day;
	private int month;
	private int year;
	
	public static final String[] MONTHNAMES = {"January", "February", "March", 
			"April", "May", "June", 
			"July", "August", "September", 
			"October", "November", "December"};
	
	public Date() {
		LocalDate today = LocalDate.now();
		this.day = today.getDayOfMonth();
		this.month = today.getMonthValue();
		this.year = today.getYear();
	}
	
	public Date(int day, int month, int year) {
		this.day = day;
		this.month = month;
		this.year = year;
	}
	
	public Date(String date) {
		this.day = Integer.parseInt(date.substring(0, 2));
		this.month = Integer.parseInt(date.substring(3, 5));
		this.year = Integer.parseInt(date.substring(6));
	}
	
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	
	public String toString() {
		if (this.day <10 && this.month <10) {
			return "0" + this.getDay() + "." + "0" + this.getMonth() + "." + this.getYear();
		}
		if (this.day >10 && this.month <10) {
			return this.getDay() + "." + "0" + this.getMonth() + "." + this.getYear();
		}
		if (this.day <10 && this.month >10 ) {
			return "0" + this.getDay() + "."+ this.getMonth() + "." + this.getYear();
		}
		return this.getDay() + "." + this.getMonth() + "." + this.getYear();
	}
	
	public boolean isSame(Date theDate) {
		return this.toString().equals(theDate.toString());
	}
	
	public String getLongString() {
		String dayLong;
		switch (this.getDay() % 10) {
			case 1:
				dayLong = this.getDay() + "st";
				break;
			case 2:
				dayLong = this.getDay() + "nd";
				break;
			case 3:
				dayLong = this.getDay() + "rd";
				break;
			default:
				dayLong = this.getDay() + "th";
		}
		return "The " + dayLong + " of " + MONTHNAMES[this.getMonth()-1] + ", " + this.getYear();
	}
	
	public static int getMonthINT(String monthName) {
		String newMonthName = monthName.substring(0, 1).toUpperCase() + monthName.substring(1).toLowerCase();
		int monthNum = 0;
		for (int i = 0; i < MONTHNAMES.length; i++) {
			if (MONTHNAMES[i].equals(newMonthName)) {
				monthNum = i;
			}
		}
		return monthNum + 1;
	}
}
