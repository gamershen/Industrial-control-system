package org.example;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.Actions;

import java.sql.Connection;

public class RunAllDialog extends Stage {
    private Connection connection;
    private TextField startDateField;
    private TextField endDateField;
    private TextField lineIDField;

    public RunAllDialog(Stage parent, Connection connection) {
        super();
        this.connection = connection;
        setTitle("Run All Actions");
        initModality(Modality.APPLICATION_MODAL);
        initOwner(parent);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20));

        Label startDateLabel = new Label("Please enter start date:");
        startDateField = new TextField();

        Label endDateLabel = new Label("Please enter end date:");
        endDateField = new TextField();

        Label lineIDLabel = new Label("Please enter a LineID:");
        lineIDField = new TextField();

        Button confirmButton = new Button("Confirm");
        confirmButton.setOnAction(e -> {
            String startDate = startDateField.getText();
            String endDate = endDateField.getText();
            String lineIDText = lineIDField.getText();

            if (startDate.isEmpty() || endDate.isEmpty() || lineIDText.isEmpty()) {
                // Show error message if any field is empty
                showError("All fields are required.");
            } else {
                try {
                    int lineID = Integer.parseInt(lineIDText);
                    // Start threads for each action with the provided inputs
                    Thread thread1 = new Thread(() -> Actions.GetSumLoggedValues(connection));
                    Thread thread2 = new Thread(() -> Actions.getHighestLoggedValue(connection));
                    Thread thread3 = new Thread(() -> Actions.printLoggedValueZeroRows(connection));
                    Thread thread4 = new Thread(() -> Actions.showLoggedValueZeroRowsPieChart(connection));
                    Thread thread5 = new Thread(() -> Actions.getMinLogIDAndSumLoggedValues(connection));
                    Thread thread6 = new Thread(() -> Actions.findMaxSumLineIDBetweenDates(connection, startDate, endDate));

                    thread1.start();
                    thread2.start();
                    thread3.start();
                    thread4.start();
                    thread5.start();
                    thread6.start();

                    // Close the dialog after starting the threads
                    close();
                } catch (NumberFormatException ex) {
                    showError("Invalid LineID. Please enter a valid integer.");
                }
            }
        });

        gridPane.add(startDateLabel, 0, 0);
        gridPane.add(startDateField, 1, 0);
        gridPane.add(endDateLabel, 0, 1);
        gridPane.add(endDateField, 1, 1);
        gridPane.add(lineIDLabel, 0, 2);
        gridPane.add(lineIDField, 1, 2);
        gridPane.add(confirmButton, 0, 3);

        Scene scene = new Scene(gridPane);
        setScene(scene);
    }

    private void showError(String message) {
        // Show error message dialog
        Stage errorStage = new Stage();
        errorStage.initModality(Modality.APPLICATION_MODAL);
        VBox errorLayout = new VBox(10);
        errorLayout.setPadding(new Insets(20));
        errorLayout.getChildren().add(new Label(message));
        Button closeButton = new Button("OK");
        closeButton.setOnAction(event -> errorStage.close());
        errorLayout.getChildren().add(closeButton);
        Scene errorScene = new Scene(errorLayout);
        errorStage.setScene(errorScene);
        errorStage.showAndWait();
    }
}