package finalproject;
import java.util.ArrayList;
import java.util.HashMap;
// Khanh Hua - e1900304
/*
 * This class contains all expenses of all days
 * It provides a method to calculate based on category, mainly for drawing the bar chart
 */

public class AllExpenses {
	private ArrayList<Item> expenses = new ArrayList<>();
	public AllExpenses() {}
	
	// Receive all expenses under the HashMap form, extract it and add it to the expense store
	public void setExpenses(HashMap<String, DayExpense> allExpenses) {
		for (DayExpense dayExpense : allExpenses.values()) {
			for (Item item: dayExpense.getExpenses()) {
				expenses.add(item);
			}
		}
	}
	
	public void clearAllItems() {
		expenses.clear();
	}
	public void addItem(Item item) {
		expenses.add(item);
	}
	// Get all the available categories
	public ArrayList<String> getAllCateogories() {
		ArrayList<String> categories = new ArrayList<>();
		for (Item item : expenses) {
			if (!categories.contains(item.getItemCategory())) {
				categories.add(item.getItemCategory());
			}
		}
		return categories;
	}
	
	public double sumItemByCategory(String category) {
		double sum = 0;
		for (Item item : this.expenses) {
			if (item.getItemCategory().equals(category)) {
				sum += item.getExpense();
			}
		}
		return sum;
	}
}
