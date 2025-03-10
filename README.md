# Weather Data Analyzer Dashboard

A modern JavaFX application that analyzes weather data from a CSV file. The dashboard calculates the average temperature for a selected month, counts rainy days, and filters out days with temperatures above a user-defined threshold. The results are displayed using a clean, responsive UI styled with CSS.

## Features

- **CSV Parsing:** Reads weather data from a CSV file with the following columns: Date, Temperature, Humidity, and Precipitation.
- **Average Temperature Calculation:** Compute the average temperature for a month selected via a drop-down menu.
- **Rainy Days Count:** Display the number of rainy days (i.e., days with precipitation greater than zero).
- **Threshold Filtering:** Filter and display days where the temperature is above a specified threshold in a TableView.
- **Modern Java Features:** Leverages Java records, lambdas, enhanced switch statements, and other Java 15+ features.
- **JavaFX UI:** Uses FXML and CSS to provide a modern, user-friendly interface.
