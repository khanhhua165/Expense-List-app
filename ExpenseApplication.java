package finalproject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
//Khanh Hua - e1900304
/*
 * Application for handling expenses. Features:
 * Read expenses and its dates from specific file
 * Create new expense and a that expense to a specific date.
 * Show expenses of the selected date. 
 * Draw a bar chart from selected categories.
 * Save expenses to file
 * Read expenses from file
 */
public class ExpenseApplication extends Application {
	
	private final ComboBox<String> dateCmb = new ComboBox<>();
	private final ComboBox<String> categoryCmb = new ComboBox<>();
	private final ComboBox<String> categoryCmb2 = new ComboBox<>();
	private HashMap<String, DayExpense> allExpenses = new HashMap<>();
	private final ArrayList<String> initialCategories = new ArrayList<>();
	private AllExpenses allE = new AllExpenses();
	private ArrayList<String> categoryStore = new ArrayList<>();
	private final ArrayList<String> dates = new ArrayList<>();
	private final ArrayList<Double> barExpenses = new ArrayList<>();
	private final ArrayList<String> barCategories = new ArrayList<>();
	private final TextField categoryField = new TextField();
	private final TextField expenseField = new TextField();
	private final TextField descriptionField = new TextField();
	private final TextField dateField = new TextField();
	private final TextField dateField2 = new TextField();
	private final TextArea textArea = new TextArea();
	private final FileChooser fileChooser = new FileChooser();
	private BarChart<String, Number> barchart;

	@SuppressWarnings("unchecked")
	@Override
	public void start(Stage primaryStage)  {
		BorderPane mainpanel = new BorderPane();
		allE.setExpenses(allExpenses);
		//Controls for creating new expense
		GridPane grid1 = new GridPane();
		grid1.setAlignment(Pos.CENTER);
		grid1.setHgap(10); //Horizontal space between columns
		grid1.setVgap(10); //Vertical space between rows
		grid1.setPadding(new Insets(25, 25, 25, 25));
		// Text control for instructions
		Text instrText = new Text("Give Category(or select from the combobox), expense, Date, a short description and create new expense.\n"
				+ "If date is empty, today's date will be used.");
		// In column 0, row 0, column span 2 and row span 1
		grid1.add(instrText, 0, 0, 2, 1);
		// Label in column 0, row 1
		Label label1 = new Label("Category:");
		grid1.add(label1, 0, 1);
		// TextField in column 1, row 1
		grid1.add(categoryField, 1, 1);
		// Combo box for selecting category
		grid1.add(categoryCmb, 1, 2);
		// Add some initial items to category
		initialCategories.add("Food and Drink");
		initialCategories.add("Ultilities");
		initialCategories.add("Transport");
		initialCategories.add("Clothing");
		initialCategories.add("Health");
		// categoryStore contains all the categories used and the initial categories, which will be used for categoryCmb
		// All new unique categories will be added to the store and the combo box as well
		categoryStore = (ArrayList<String>)initialCategories.clone();
		categoryCmb.setItems(FXCollections.observableArrayList(categoryStore));
		Label label2 = new Label("Description:");
		grid1.add(label2, 0, 3);
		grid1.add(descriptionField, 1, 3);
		Label label4 = new Label("Expense($):");
		grid1.add(label4, 0, 4);
		grid1.add(expenseField,	 1, 4);
		Label label5 = new Label("Date:");
		grid1.add(label5, 0, 5);
		grid1.add(dateField, 1, 5);
		dateField.setText("date as 'dd.mm.yyyy' Or empty");
		Button createBtn = new Button("Add expense");
		grid1.add(createBtn, 1, 6);
		
		//Controls for viewing expenses of a selected date
		Text instrText2 = new Text("Insert(or you can select from combobox) to view all expenses of that date");
		grid1.add(instrText2, 0, 7, 2, 1);
		Label label3 = new Label("Date:");
		grid1.add(label3, 0, 8);
		dateField2.setText("date as 'dd.mm.yyyy' or choose from the combo box");
		grid1.add(dateField2, 1, 8);
		grid1.add(dateCmb, 1, 9);
		Button findBtn = new Button("Find expenses of the day");
		grid1.add(findBtn, 1, 10);

		//Controls to draw bar chart
		Text instrText3 = new Text("Choose which category you would like to draw to or remove from the barchart.");
		grid1.add(instrText3, 0, 11, 2, 1);
		Label label6 = new Label("Category:");
		grid1.add(label6, 0, 10);
		grid1.add(categoryCmb2, 1, 12);
		
		Button addToChartBtn = new Button("Add to Barchart");
		grid1.add(addToChartBtn, 0, 13);
		Button removeFromChartBtn = new Button("Remove from Barchart");
		grid1.add(removeFromChartBtn, 1, 13);
		Button addAllToChartBtn = new Button("Add all to Barchart");
		grid1.add(addAllToChartBtn, 0, 14);
		Button removeAllFromChartBtn = new Button("Remove all from Barchart");
		grid1.add(removeAllFromChartBtn, 1, 14);
		//Grid1 left on borderpane
		mainpanel.setLeft(grid1);
		
		//TextArea center of borderpane
		textArea.setEditable(false);
		textArea.setFont(Font.font("Courier New", FontWeight.NORMAL, 12));
		mainpanel.setCenter(textArea);
        //If text exceeds the width, then text wrap onto another line.
        textArea.setWrapText(true); 
        // Create Bar Chart at the bottom of borderpane

        CategoryAxis xAxis = new CategoryAxis(); //String category
        NumberAxis yAxis = new NumberAxis();  
        barchart = new BarChart<>(xAxis,yAxis);
        barchart.setTitle("Expense Distribution by Category");
        barchart.setLegendVisible(false); //Just one series, legend is not needed
        xAxis.setLabel("Category");       
        yAxis.setLabel("Expense");
        XYChart.Series<String, Number> series1 = new Series<>();
        barchart.getData().add(series1);	
        mainpanel.setBottom(barchart);
		//Create menubar and File menu
		MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("File");
        menuBar.getMenus().add(menuFile);
        MenuItem startNew = new MenuItem("New");
        MenuItem open = new MenuItem("Open");
        MenuItem saveAs = new MenuItem("Save As");
        MenuItem exit = new MenuItem("Exit");
        menuFile.getItems().addAll(startNew, open, saveAs, new SeparatorMenuItem(),  exit);
		mainpanel.setTop(menuBar);
		
		Scene scene = new Scene(mainpanel, 1200, 1000);
		primaryStage.setTitle("Daily Expenses");		
		primaryStage.setScene(scene);
		primaryStage.show();
		
		//Event handlers
		categoryCmb.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				String value = categoryCmb.getValue();
				categoryField.setText(value);
			}
		});
		
		createBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				textArea.clear();
				if (categoryField.getText() == null || categoryField.getText().equals("")) {
					textArea.appendText("You must fill in or choose a category");
					return;
				}
				if (descriptionField.getText() == null || descriptionField.getText().equals("")) {
					textArea.appendText("You must fill in a description!!");
					return;
				}
				String categoryName = categoryField.getText();
				String descriptionName = descriptionField.getText();
				// Check the date input with regular expression
				if(!expenseField.getText().matches("\\d+\\.?\\d*")) {
					textArea.appendText("Please input a valid expense (a number)");
					return;
				}
				double expense = Double.parseDouble(expenseField.getText());
				Item item = new Item(expense, descriptionName, categoryName);
				Date date;
				int dateFlag = 0;
				int categoryFlag = 0;

				if (dateField.getText().equals("")) {
					date = new Date();
				} else {
					// Check expense input with regular expression
					if (!dateField.getText().matches("\\d{2}\\.\\d{2}\\.\\d{4}")) {
						textArea.appendText("You must input a valid date format(dd.mm.yyyy) or leave it empty");
						return;
					}
				date = new Date(dateField.getText());	
				}
				String dateString = date.toString();
				for (String i : allExpenses.keySet()) {
					if(new Date(i).isSame(date)) {
						dateFlag++;
					}
				}
				allE.addItem(item);
				// If the date already exists, add the item to that date in the HashMap
				// Otherwise, create a new item in HashMap with the date as key
				if (dateFlag != 0) {
					allExpenses.get(dateString).addExpense(item);
				} else {
					DayExpense dayExpense = new DayExpense();
					dayExpense.addExpense(item);
					allExpenses.put(dateString, dayExpense);
					dateCmb.getItems().add(dateString);
					dates.add(dateString);
				}
				for (String i : categoryStore) {
					if(i.equals(categoryName)) {
						categoryFlag++;
					}
				}
				// If a new unique category is added, add that category to the combo box
				if (categoryFlag == 0) {
					categoryStore.add(categoryName);
					categoryCmb.getItems().add(categoryName);
				}
				categoryCmb2.getItems().clear();
				// categoryCmb2 contains all the used categories
				categoryCmb2.setItems(FXCollections.observableArrayList(allE.getAllCateogories()));
				// After finishing add a new expense, the program shows the total expenses of that date
				textArea.appendText(dateString);
				textArea.appendText(":\n");
				textArea.appendText(allExpenses.get(dateString).toString());
				textArea.appendText(String.format("\nTotal expenses of the day: %.2f", allExpenses.get(dateString).sumDayExpense()));
			}
		});

		dateCmb.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				String text = dateCmb.getValue();
				dateField2.setText(text);
			}
		});
		
		findBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				String value = dateField2.getText();
				if (!dateField2.getText().matches("\\d{2}\\.\\d{2}\\.\\d{4}")) {
					textArea.clear();
					textArea.appendText("You must input a valid date format(dd.mm.yyyy)");
					return;
				}
				Date date = new Date(value);
				int dateFlag = 0;
				for (String i : allExpenses.keySet()) {
					if(new Date(i).isSame(date)) {
						dateFlag++;
					}
				}
				textArea.clear();
				if (dateFlag != 0) {
					textArea.appendText(value);
					textArea.appendText(":\n");
					textArea.appendText(allExpenses.get(value).toString());	
					textArea.appendText(String.format("\nTotal expenses of the day: %.2f", allExpenses.get(value).sumDayExpense()));
				} else {
					textArea.appendText("The date you searched is not in the database !");
				}
			}
		});
		// Add the selected category in the combo box to the bar chart
		addToChartBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (categoryCmb2.getValue() == null) {
					textArea.clear();
					textArea.appendText("You must choose a category to add");
					return;
				}
				String chosenCategory = categoryCmb2.getValue();
				Double sum = allE.sumItemByCategory(chosenCategory);
				if (!barCategories.contains(chosenCategory)) {
					barExpenses.add(sum);
					barCategories.add(chosenCategory);
					writeTotalSum();
					barchart.setData(FXCollections.observableArrayList());
					XYChart.Series<String, Number> series = new Series<>();
					for (int i = 0; i < barExpenses.size(); i++) {
						series.getData().add(new Data<>(barCategories.get(i), barExpenses.get(i)));
					}
					barchart.getData().add(series);
				} else {
					textArea.clear();
					textArea.appendText("The category you choose to add is already in the bar chart");
				}
			}
		});
		
		addAllToChartBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Double sum;
				barCategories.clear();
				barExpenses.clear();
				for (String category : allE.getAllCateogories()) {
					barCategories.add(category);
					sum = allE.sumItemByCategory(category);
					barExpenses.add(sum);
				}
				writeTotalSum();
				barchart.setData(FXCollections.observableArrayList());
				XYChart.Series<String, Number> series = new Series<>();
				for (int i = 0; i < barExpenses.size(); i++) {
					series.getData().add(new Data<>(barCategories.get(i), barExpenses.get(i)));
				}
				barchart.getData().add(series);
			}
		});
		
		// Remove the selected category in the combo box from the bar chart
		removeFromChartBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (categoryCmb2.getValue() == null) {
					textArea.clear();
					textArea.appendText("You must choose a category to remove");
					return;
				}
				String errorMessage = "The category you choose to remove currently does not appear on the Bar Chart, please choose another one.";
				String chosenCategory = categoryCmb2.getValue();
				int categoryIndex = barCategories.indexOf(chosenCategory);
				if (categoryIndex == -1) {
					textArea.clear();
					textArea.appendText(errorMessage);
				} else {
					textArea.clear();
					barCategories.remove(categoryIndex);
					barExpenses.remove(categoryIndex);
					writeTotalSum();
					barchart.setData(FXCollections.observableArrayList());
					XYChart.Series<String, Number> series = new Series<>();
					for (int i = 0; i < barExpenses.size(); i++) {
						series.getData().add(new Data<>(barCategories.get(i), barExpenses.get(i)));
					}
					barchart.getData().add(series);
				}
			}
		});
		
		removeAllFromChartBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				barCategories.clear();
				barExpenses.clear();
				textArea.clear();
				barchart.setData(FXCollections.observableArrayList());
			}
		});
		
		saveAs.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				 File file = fileChooser.showSaveDialog(primaryStage);
                 if (file != null) {
                    saveExpensesToFile(file);
                 }
			}
		});
		
		open.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				 File file = fileChooser.showOpenDialog(primaryStage);
                 if (file != null) {
                    readExpensesFromFile(file);
                 }
			}
		});
		
		exit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				//System.exit(0);
				Platform.exit();
			}
		});
		
		startNew.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				clearControls();
			}
		});

	}

	//Writes expenses to file 
	private void saveExpensesToFile(File file) {
		System.out.println(file.getAbsolutePath());
		try (ObjectOutputStream file_out = new ObjectOutputStream(new FileOutputStream(file))){
            file_out.writeObject(allExpenses);
        }
        catch(Exception e) {
            System.out.println("Problems with " + file);
            e.printStackTrace();
        }
	}
	
	//Reads expenses from file
	@SuppressWarnings("unchecked")
	private void readExpensesFromFile(File file) {
		try (ObjectInputStream file_in = new ObjectInputStream(new FileInputStream(file))){
			clearControls();
            allExpenses = (HashMap<String, DayExpense>)file_in.readObject();
            allE.setExpenses(allExpenses);
            categoryCmb2.setItems(FXCollections.observableArrayList(allE.getAllCateogories()));
            categoryStore = (ArrayList<String>) allE.getAllCateogories().clone();
            for (String initialCategory: initialCategories) {
            	if (!categoryStore.contains(initialCategory)) {
            		categoryStore.add(initialCategory);
            	}
            }
            categoryCmb.getItems().clear();
            categoryCmb.setItems(FXCollections.observableArrayList(categoryStore));
            for (String date : allExpenses.keySet()) {
            	dates.add(date);
            }
            dateCmb.setItems(FXCollections.observableArrayList(dates));
        }
        catch(Exception e) {
            System.out.println("Problems with " + file);
            e.printStackTrace();
        }
	}
	
	// Write total sums by category to Text area
	public void writeTotalSum() {
		textArea.clear();
		textArea.appendText("Total exepenses by category:\n");
		for (int i = 0; i < barCategories.size(); i++) {
			textArea.appendText(String.format("%s: %.2f\n", barCategories.get(i), barExpenses.get(i)));
		}
	}
	
	//Clears GUI controls
	@SuppressWarnings("unchecked")
	private void clearControls() {
		categoryField.setText(null);
		expenseField.setText(null);
		descriptionField.setText(null);
		dateField.setText("date as 'dd.mm.yyyy' Or empty");
		dateField2.setText("date as 'dd.mm.yyyy' or choose from the combo box");
		textArea.setText(null);
		dateCmb.getSelectionModel().clearSelection();
		categoryCmb.getSelectionModel().clearSelection();
		categoryCmb2.getSelectionModel().clearSelection();
		dateCmb.getItems().clear();
		categoryCmb.getItems().clear();
		categoryCmb2.getItems().clear();
		allExpenses.clear();
		categoryStore.clear();
		categoryStore = (ArrayList<String>)initialCategories.clone();
		dates.clear();
		allE.clearAllItems();
		categoryCmb.setItems(FXCollections.observableArrayList(initialCategories));
		barExpenses.clear();
		barCategories.clear();
		barchart.setData(FXCollections.observableArrayList());
	}

	public static void main(String[] args) {
		launch(args);

	}
}

