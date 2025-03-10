package org.example.weatherapp;

import org.example.weatherapp.WeatherData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The WeatherAnalyzer interface provides static methods to analyze weather data.
 *
 * <p>This interface demonstrates modern Java features such as:
 * <ul>
 *   <li>Records</li>
 *   <li>Enhanced switch statements</li>
 *   <li>Text Blocks</li>
 *   <li>Lambdas and Streams</li>
 * </ul>
 *
 * **Usage Example:**
 * ```java
 * List<WeatherData> data = WeatherAnalyzer.parseCSV("weatherdata.csv");
 * double avgTemp = WeatherAnalyzer.averageTemperatureForMonth(data, 8);
 * System.out.println(avgTemp);
 * ```
 */
public interface WeatherAnalyzer {

    /**
     * Parses weather data from a CSV file.
     *
     * @param filePath the path to the CSV file
     * @return a list of WeatherData records
     * @throws IOException if file reading fails
     */
    static List<WeatherData> parseCSV(String filePath) throws IOException {
        return Files.lines(Path.of(filePath))
                .skip(1) // Skip header line
                .map(line -> {
                    String[] parts = line.split(",");
                    return new WeatherData(
                            LocalDate.parse(parts[0]),
                            Double.parseDouble(parts[1]),
                            Integer.parseInt(parts[2]),
                            Double.parseDouble(parts[3])
                    );
                })
                .collect(Collectors.toList());
    }

    /**
     * Calculates the average temperature for a given month.
     *
     * @param data  the list of WeatherData records
     * @param month the month (1-12)
     * @return the average temperature, or Double.NaN if no data is available for that month
     */
    static double averageTemperatureForMonth(List<WeatherData> data, int month) {
        return data.stream()
                .filter(w -> w.date().getMonthValue() == month)
                .mapToDouble(WeatherData::temperature)
                .average()
                .orElse(Double.NaN);
    }

    /**
     * Returns a list of weather data records for days where the temperature exceeds a given threshold.
     *
     * @param data      the list of WeatherData records
     * @param threshold the temperature threshold
     * @return a list of records with temperatures above the threshold
     */
    static List<WeatherData> daysAboveTemperature(List<WeatherData> data, double threshold) {
        return data.stream()
                .filter(w -> w.temperature() > threshold)
                .collect(Collectors.toList());
    }

    /**
     * Counts the number of rainy days (where precipitation > 0).
     *
     * @param data the list of WeatherData records
     * @return the number of rainy days
     */
    static long countRainyDays(List<WeatherData> data) {
        return data.stream()
                .filter(w -> w.precipitation() > 0)
                .count();
    }

    /**
     * Determines the weather category based on the temperature using an enhanced switch statement.
     *
     * <p>The logic is:
     * <ul>
     *   <li>Temperature >= 30.0: "Hot"</li>
     *   <li>Temperature >= 20.0 and &lt; 30.0: "Warm"</li>
     *   <li>Otherwise: "Cold"</li>
     * </ul>
     *
     * @param temperature the temperature to evaluate
     * @return the weather category as a String
     */
    static String weatherCategory(double temperature) {
        int category = temperature >= 30.0 ? 1 : (temperature >= 20.0 ? 2 : 3);
        return switch (category) {
            case 1 -> "Hot";
            case 2 -> "Warm";
            default -> "Cold";
        };
    }
}