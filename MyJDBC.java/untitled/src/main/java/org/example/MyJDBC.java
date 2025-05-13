package org.example;

import javafx.application.Application;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;


public class MyJDBC extends Application {
    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // Create a grid pane for layout
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);

            // Create labels and text fields for username and password
            Label usernameLabel = new Label("Username:");
            TextField usernameField = new TextField();
            Label passwordLabel = new Label("Password:");
            PasswordField passwordField = new PasswordField();

            // Add components to grid pane
            grid.add(usernameLabel, 0, 0);
            grid.add(usernameField, 1, 0);
            grid.add(passwordLabel, 0, 1);
            grid.add(passwordField, 1, 1);

            // Create a dialog for user input
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Enter Credentials");
            dialog.setHeaderText("Enter your username and password");
            dialog.getDialogPane().setContent(grid);

            // Add buttons to the dialog
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            // Show the dialog and wait for user response
            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Retrieve username and password from text fields
                String username = usernameField.getText();
                String password = passwordField.getText();

                try {
                    // Register MySQL JDBC driver
                    Class.forName("com.mysql.cj.jdbc.Driver");

                    // Establish database connection using user provided credentials
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", username, password);

                    // Start the JavaFX application by creating a ButtonFX instance
                    new ButtonFX(connection).start(primaryStage);
                } catch (SQLException e) {
                    // Show error popup for database connection issue
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Database Connection Error");
                    alert.setHeaderText("Failed to connect to the database");
                    alert.setContentText("Please check your username and password and try again.");
                    alert.showAndWait();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
