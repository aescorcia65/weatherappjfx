package org.example.weatherapp;

import java.io.File;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Controller for the file upload scene.
 */
public class FileUpload {

    @FXML
    private Label filePathLabel;

    /**
     * Opens a file chooser for selecting a CSV file and then transitions to the average temperature scene,
     * passing the selected CSV file path.
     */
    @FXML
    public void onUploadCSV() {
        Window window = filePathLabel.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        File selectedFile = fileChooser.showOpenDialog(window);
        if (selectedFile != null) {
            String csvFilePath = selectedFile.getAbsolutePath();
            filePathLabel.setText(csvFilePath);

            // Transition to the Average Temperature scene
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
                Parent root = loader.load();

                // Retrieve the controller for the Average Temperature scene
                Dashboard avgController = loader.getController();
                // Pass the CSV file path to the AverageTempController
                avgController.setCsvFilePath(csvFilePath);

                // Change the scene
                Stage stage = (Stage) filePathLabel.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (IOException e) {
                e.printStackTrace();
                filePathLabel.setText("Error loading new scene.");
            }
        } else {
            filePathLabel.setText("No file selected.");
        }
    }
}