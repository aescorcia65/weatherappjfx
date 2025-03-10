package org.example.weatherapp;

import java.io.IOException;
import java.time.Month;
import java.util.List;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * Controller for the Weather Dashboard.
 *
 * <p>This controller handles several functionalities:
 * <ul>
 *   <li>Calculating the average temperature for a selected month</li>
 *   <li>Displaying the number of rainy days</li>
 *   <li>Finding and displaying days with temperatures above a specified threshold using a TableView</li>
 * </ul>
 *
 * <p>Example usage in an FXML file:
 *
 * <pre>
 * {@code
 * <VBox xmlns:fx="http://javafx.com/fxml"
 *       fx:controller="org.example.weatherapp.Dashboard"
 *       alignment="CENTER"
 *       spacing="20">
 *     <!-- Month selection -->
 *     <ComboBox fx:id="monthComboBox" prefWidth="200">
 *         <!-- Items defined here -->
 *     </ComboBox>
 *
 *     <!-- Button to calculate average temperature -->
 *     <Button text="Calculate Average Temperature" onAction="#onCalculateAverageTemp"/>
 *
 *     <!-- Label to display average temperature -->
 *     <Label fx:id="resultLabel"/>
 *
 *     <!-- Label to display rainy days count -->
 *     <Label fx:id="rainyDaysLabel"/>
 *
 *     <!-- TextField for threshold input -->
 *     <TextField fx:id="thresholdTextField" promptText="Enter temperature threshold"/>
 *
 *     <!-- Button to find days above threshold -->
 *     <Button text="Find Days Above Threshold" onAction="#onFindDaysAboveThreshold"/>
 *
 *     <!-- TableView to display the filtered days -->
 *     <TableView fx:id="thresholdTableView">
 *         <columns>
 *             <TableColumn fx:id="dateColumn" text="Date"/>
 *             <TableColumn fx:id="tempColumn" text="Temperature (°C)"/>
 *             <TableColumn fx:id="humidityColumn" text="Humidity (%)"/>
 *             <TableColumn fx:id="precipColumn" text="Precipitation"/>
 *         </columns>
 *     </TableView>
 * </VBox>
 * }
 * </pre>
 *
 * @see WeatherAnalyzer
 * @see WeatherData
 */
public class Dashboard {

    @FXML
    private ComboBox<String> monthComboBox;

    @FXML
    private Label resultLabel;

    @FXML
    private Label rainyDaysLabel;

    @FXML
    private TextField thresholdTextField;

    @FXML
    private TableView<WeatherData> thresholdTableView;

    @FXML
    private TableColumn<WeatherData, String> dateColumn;

    @FXML
    private TableColumn<WeatherData, Double> tempColumn;

    @FXML
    private TableColumn<WeatherData, Integer> humidityColumn;

    @FXML
    private TableColumn<WeatherData, Double> precipColumn;

    // Field to hold the CSV file path passed from the previous scene.
    private String csvFilePath;

    // Field to store parsed weather data for reuse in multiple methods.
    private List<WeatherData> weatherDataList;

    /**
     * Sets the CSV file path from the previous scene and immediately parses the CSV data.
     *
     * <p>This method uses {@link WeatherAnalyzer#parseCSV(String)} to load and parse the CSV file,
     * then stores the resulting weather data for later use. It also updates the rainy days label by
     * counting the rainy days via {@link WeatherAnalyzer#countRainyDays(List)}.
     *
     * @param csvFilePath the path to the CSV file containing weather data
     */
    public void setCsvFilePath(String csvFilePath) {
        this.csvFilePath = csvFilePath;
        try {
            // Parse the CSV file and store the weather data in a list.
            this.weatherDataList = WeatherAnalyzer.parseCSV(csvFilePath);
            this.rainyDaysLabel.setText("Rainy days: " + WeatherAnalyzer.countRainyDays(weatherDataList));
        } catch (IOException e) {
            if (resultLabel != null) {
                resultLabel.setText("Error reading weather data file.");
            }
            e.printStackTrace();
        }
    }

    /**
     * Calculates the average temperature for the selected month and displays the result.
     *
     * <p>This method retrieves the selected month from {@code monthComboBox}, converts it to its
     * corresponding month number using {@link Month}, and computes the average temperature by
     * invoking {@link WeatherAnalyzer#averageTemperatureForMonth(List, int)}.
     *
     * <p>Example:
     *
     * <pre>
     * {@code
     * // Assuming "January" is selected in the ComboBox:
     * onCalculateAverageTemp();
     * // ResultLabel might display: "Average temperature for January: 5.00°C"
     * }
     * </pre>
     */
    @FXML
    public void onCalculateAverageTemp() {
        String selectedMonth = monthComboBox.getValue();

        if (selectedMonth == null) {
            resultLabel.setText("Please select a month.");
            return;
        }

        int monthNumber;
        try {
            monthNumber = Month.valueOf(selectedMonth.toUpperCase()).getValue();
        } catch (IllegalArgumentException e) {
            resultLabel.setText("Invalid month selected.");
            return;
        }

        if (weatherDataList == null || weatherDataList.isEmpty()) {
            resultLabel.setText("No weather data available.");
            return;
        }

        double averageTemp = WeatherAnalyzer.averageTemperatureForMonth(weatherDataList, monthNumber);

        if (Double.isNaN(averageTemp)) {
            resultLabel.setText("No data available for " + selectedMonth + ".");
        } else {
            resultLabel.setText(String.format("Average temperature for %s: %.2f°C", selectedMonth, averageTemp));
        }
    }

    /**
     * Filters the weather data for days with temperatures above the specified threshold and
     * displays the results in a TableView.
     *
     * <p>This method retrieves a temperature threshold from {@code thresholdTextField}, filters the
     * stored weather data using {@link WeatherAnalyzer#daysAboveTemperature(List, double)}, and then
     * populates the {@code TableView} with the filtered list. The cell value factories are set using
     * lambda expressions to work with Java records.
     *
     * <p>Example:
     *
     * <pre>
     * {@code
     * // If the thresholdTextField contains "30", clicking the button calls:
     * onFindDaysAboveThreshold();
     * // The TableView is then populated with all records where temperature > 30°C.
     * }
     * </pre>
     */
    @FXML
    public void onFindDaysAboveThreshold() {
        // Retrieve the threshold value from the text field.
        String thresholdText = thresholdTextField.getText();
        double threshold;
        try {
            threshold = Double.parseDouble(thresholdText);
        } catch (NumberFormatException e) {
            resultLabel.setText("Invalid threshold value. Please enter a valid number.");
            return;
        }

        if (weatherDataList == null || weatherDataList.isEmpty()) {
            resultLabel.setText("No weather data available.");
            return;
        }

        // Filter the weather data for days with temperature above the threshold.
        List<WeatherData> daysAbove = WeatherAnalyzer.daysAboveTemperature(weatherDataList, threshold);

        // Set cell value factories using lambda expressions to correctly extract record properties.
        dateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().date().toString()));
        tempColumn.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().temperature()).asObject());
        humidityColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().humidity()).asObject());
        precipColumn.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().precipitation()).asObject());

        // Populate the TableView with the filtered list.
        thresholdTableView.setItems(FXCollections.observableArrayList(daysAbove));
    }
}