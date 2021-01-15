package finalproject;

import java.io.Serializable;

// This class represents an expense
/*
 * This class represents an expense
 * Each expense has a description, an expense amount, and an category name
 */
public class Item implements Serializable {
	private static final long serialVersionUID = 1L;
	private double expense;
	private String description;
	private String itemCategory;
	public Item(double expense, String description, String itemCategory) {
		this.expense = expense;
		this.description = description;
		this.itemCategory = itemCategory;
	}
	public double getExpense() {
		return this.expense;
	}
	public String getDescription() {
		return this.description;
	}
	public String getItemCategory() {
		return this.itemCategory;
	}
	public String toString() {
		return String.format("%-10s\t\t%-7.2f\t\t\t%-25s", this.itemCategory, this.expense, this.description);
	}
	
	public static void main(String[] args) {
		String cac = "hehe";
		String lon = cac;
		System.out.println(cac == lon);
	}
}
