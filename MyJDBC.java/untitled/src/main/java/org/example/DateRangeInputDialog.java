package org.example;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DateRangeInputDialog extends Stage {
    private TextField startDateField;
    private TextField endDateField;
    private boolean confirmed = false;

    public DateRangeInputDialog(Stage parent) {
        super();

        setTitle("Enter Date Range");
        initModality(Modality.APPLICATION_MODAL);
        initOwner(parent);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label startDateLabel = new Label("Start Date (dd/mm/yyyy):");
        startDateField = new TextField();
        Label endDateLabel = new Label("End Date (dd/mm/yyyy):");
        endDateField = new TextField();

        Button confirmButton = new Button("Confirm");
        confirmButton.setOnAction(e -> {
            confirmed = true;
            close();
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> close());

        gridPane.add(startDateLabel, 0, 0);
        gridPane.add(startDateField, 1, 0);
        gridPane.add(endDateLabel, 0, 1);
        gridPane.add(endDateField, 1, 1);
        gridPane.add(confirmButton, 0, 2);
        gridPane.add(cancelButton, 1, 2);

        setScene(new Scene(gridPane, 300, 100));
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getStartDate() {
        return startDateField.getText();
    }

    public String getEndDate() {
        return endDateField.getText();
    }
}