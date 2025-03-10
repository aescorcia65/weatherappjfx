package org.example.weatherapp;

import java.time.LocalDate;
/**
 * A record representing a single day's weather data.
 * **Example:**
 * ```java
 * WeatherData data = new WeatherData(LocalDate.parse("2023-08-01"), 32.5, 65, 0.0);
 * ```
 */
public record WeatherData(LocalDate date, double temperature, int humidity, double precipitation) {}
