package finalproject;
import java.io.Serializable;
import java.util.ArrayList;
// Khanh Hua - e1900304
/*
 * This class contains all expenses (class Item) of a day
 * It also provides a method to calculate sum of all expense of  the day
 */
public class DayExpense implements Serializable {
	private static final long serialVersionUID = 1L;
	private ArrayList<Item> expenses = new ArrayList<>();
	public DayExpense() {}

	//Add expense to ArrayList
	public void addExpense(Item item) {
		expenses.add(item);
	}
	public ArrayList<Item> getExpenses() {
		return expenses;
	}
	//Print all expenses of a day
	public String toString() {
		StringBuilder expensesString = new StringBuilder();
		expensesString.append("Category\t\tExpense\t\t\tDescription\n");
		for (Item item : expenses) {
			expensesString.append(item.toString());
			expensesString.append("\n");
		}
		return expensesString.toString();
	}
	// Calculate the total expense of the day
	public double sumDayExpense() {
		double sum = 0;
		for (Item item: expenses) {
			sum += item.getExpense();
		}
		return sum;
	}
}
