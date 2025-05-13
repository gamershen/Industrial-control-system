package org.example;

import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class ThreadsDialog {
    private Connection connection;
    private Stage parentStage; // Renamed from primaryStage to parentStage

    public ThreadsDialog(Connection connection, Stage parentStage) {
        this.connection = connection;
        this.parentStage = parentStage;
    }

    public void showThreadsDialog() {
        // Create a new stage for the dialog
        Stage dialogStage = new Stage();

        // If there is a parent stage, position the dialog relative to it
        if (parentStage != null) {
            dialogStage.setX(parentStage.getX() + 50); // Offset from the parent stage's X position
            dialogStage.setY(parentStage.getY() + 50); // Offset from the parent stage's Y position
        }

        // Create checkboxes for each action
        CheckBox checkbox1 = new CheckBox("Get Sum of Logged Values");
        CheckBox checkbox2 = new CheckBox("Get Highest Logged Value");
        CheckBox checkbox3 = new CheckBox("Get Logged Values with Zero Rows");
        CheckBox checkbox4 = new CheckBox("Get Logged Values with Zero Rows (Pie Chart)");
        CheckBox checkbox5 = new CheckBox("Get Min Log ID and Sum of Logged Values");
        CheckBox checkbox6 = new CheckBox("Get Max Line ID Between Dates");

        // Create a VBox to hold the checkboxes
        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(checkbox1, checkbox2, checkbox3, checkbox4, checkbox5, checkbox6);

        // Create a Button to confirm selection
        Button confirmButton = new Button("Confirm");
        confirmButton.setOnAction(event -> {
            List<String> selectedActions = new ArrayList<>();
            if (checkbox1.isSelected()) selectedActions.add(checkbox1.getText());
            if (checkbox2.isSelected()) selectedActions.add(checkbox2.getText());
            if (checkbox3.isSelected()) selectedActions.add(checkbox3.getText());
            if (checkbox4.isSelected()) selectedActions.add(checkbox4.getText());
            if (checkbox5.isSelected()) selectedActions.add(checkbox5.getText());
            if (checkbox6.isSelected()) selectedActions.add(checkbox6.getText());

            // Perform the selected actions using the Actions class
            for (String action : selectedActions) {
                if (action.equals("Get Max Line ID Between Dates")) {
                    new Thread(() -> Actions.showDateRangeInputDialog(connection, dialogStage)).start();
                } else {
                    Actions.runAction(action, connection);
                }
            }

            // Close the dialog
            dialogStage.close();
        });

        // Add the confirm button to the VBox
        vBox.getChildren().add(confirmButton);

        // Create a Scene with the VBox
        Scene scene = new Scene(vBox, 300, 200);

        // Set the Scene and show it
        dialogStage.setScene(scene);
        dialogStage.showAndWait();
    }

}