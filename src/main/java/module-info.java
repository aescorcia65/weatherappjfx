module org.example.weatherapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.weatherapp to javafx.fxml;
    exports org.example.weatherapp;
}