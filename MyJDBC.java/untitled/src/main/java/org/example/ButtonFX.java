package org.example;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.swing.*;
import java.sql.Connection;

public class ButtonFX extends Application {
    private Connection connection;
    private Stage primaryStage; // Store the reference to primaryStage

    public ButtonFX(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage; // Store the reference to primaryStage
        primaryStage.setTitle("Database Actions");
        Image image = new Image("file:src/pics/ggg.jpg");


        ImageView mv = new ImageView(image);

        GridPane grid = new GridPane();
        grid.setStyle("-fx-background-image: url('file:///C:/folder/MyJDBC.java/untitled/src/pics/ggg.jpg'); -fx-background-size: cover;");
        grid.setPadding(new Insets(20, 10, 20, 20));
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER); // Center the elements within the GridPane

        Label titleLabel = new Label("מערכת בקרה תעשייתית");
        titleLabel.setStyle("-fx-font-size: 45; -fx-font-weight: bold; -fx-text-fill: #ffffff; -fx-effect: dropshadow(three-pass-box, black, 1, 1, 0, 0);"); // Underline the title
        GridPane.setConstraints(titleLabel, 0, 0, 2, 1); // span 2 columns
        GridPane.setMargin(titleLabel, new Insets(0, 0, 150, 0)); // Add margin at the bottom
        GridPane.setHalignment(titleLabel, HPos.CENTER); // Center the title label horizontally
        grid.getChildren().add(titleLabel);

        // First Title Label
        Label sectitleLabel = new Label("למפעל ייצור יריעות ניילון לחממות");
        sectitleLabel.setStyle("-fx-font-size: 45; -fx-font-weight: bold ; -fx-text-fill: #ffffff; -fx-effect: dropshadow(three-pass-box, black, 1, 1, 0, 0);"); // Underline the title
        GridPane.setConstraints(sectitleLabel, 0, 0, 2, 1); // span 2 columns
        GridPane.setMargin(sectitleLabel, new Insets(0, 0, 40, 0)); // Add margin at the bottom
        GridPane.setHalignment(sectitleLabel, HPos.CENTER); // Center the title label horizontally
        grid.getChildren().add(sectitleLabel);

        // Second Title Label
        Label secondTitleLabel = new Label("בחר פעולה:");
        secondTitleLabel.setStyle("-fx-font-size: 34; -fx-font-weight: bold; -fx-text-fill: #ffffff; -fx-effect: dropshadow(three-pass-box, black, 1, 1, 0, 0);"); // Underline the title
        GridPane.setConstraints(secondTitleLabel, 0, 1, 2, 1); // span 2 columns
        GridPane.setMargin(secondTitleLabel, new Insets(0, 0, 10, 0)); // Add margin at the bottom
        GridPane.setHalignment(secondTitleLabel, HPos.CENTER); // Center the second title label horizontally
        grid.getChildren().add(secondTitleLabel);


        addButton(grid, "Get Sum of Logged Values", Color.GHOSTWHITE);
        addButton(grid, "Get Highest Logged Value", Color.GHOSTWHITE);
        addButton(grid, "Get Logged Values with Zero Rows", Color.GHOSTWHITE);
        addButton(grid, "Get Logged Values with Zero Rows (Pie Chart)", Color.GHOSTWHITE);
        addButton(grid, "Get Min Log ID and Sum of Logged Values", Color.GHOSTWHITE);
        addButton(grid, "Get Max Line ID Between Dates", Color.GHOSTWHITE);
        addButton(grid, "Threads", Color.GHOSTWHITE);
        addButton(grid, "Run All", Color.GHOSTWHITE);

        Scene scene = new Scene(grid, 1450, 750);
        primaryStage.setScene(scene);
        primaryStage.show();
        CreateSqlFunctions.createGetSumLoggedValues(connection);

    }



    private void addButton(GridPane grid, String text, Color bgColor) {
        Button button = new Button(text);
        button.setBackground(new Background(new BackgroundFill(bgColor, CornerRadii.EMPTY, Insets.EMPTY)));
        button.setTextFill(Color.BLACK);
        button.setPrefSize(300, 40); // Set fixed width and height for the button
        button.setOnAction(event -> handleButtonClick(text));

        //button.setStyle("-fx-font-weight: bold;");
        button.setStyle("-fx-font-weight: bold; -fx-background-radius: 20px; -fx-border-radius: 20px; -fx-font-size: 13px; -fx-background-color: #ffffff;;");

        button.setOnMouseEntered(e -> button.setStyle("-fx-font-weight: bold; -fx-background-radius: 20px; -fx-border-radius: 20px; -fx-font-size: 13px; -fx-background-color: #0cd879;-fx-text-fill: #ffffff;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-font-weight: bold; -fx-background-radius: 20px; -fx-border-radius: 20px; -fx-font-size: 13px; -fx-background-color: #ffffff;"));
        // Set button alignment to center
        button.setAlignment(Pos.CENTER);

        // Set constraints for button
        GridPane.setMargin(button, new Insets(0, 0, 0, 195)); // Add margin at the bottom
        grid.add(button, 0, grid.getChildren().size());
    }

    private void handleButtonClick(String buttonText) {
        if (buttonText.equals("Get Sum of Logged Values")) {
            Actions.GetSumLoggedValues(connection);
        } else if (buttonText.equals("Get Highest Logged Value")) {
            // Handle button click for "Get Highest Logged Value"
            // Call the appropriate method from the Actions class
            Actions.getHighestLoggedValue(connection);
        } else if (buttonText.equals("Get Logged Values with Zero Rows")) {
            // Handle button click for "Get Logged Values with Zero Rows"
            // Call the appropriate method from the Actions class
            Actions.printLoggedValueZeroRows(connection);
        } else if (buttonText.equals("Get Logged Values with Zero Rows (Pie Chart)")) {
            // Handle button click for "Get Logged Values with Zero Rows (Pie Chart)"
            // Call the appropriate method from the Actions class
            Actions.showLoggedValueZeroRowsPieChart(connection);
        } else if (buttonText.equals("Get Min Log ID and Sum of Logged Values")) {
            // Handle button click for "Get Min Log ID and Sum of Logged Values"
            // Call the appropriate method from the Actions class
            Actions.getMinLogIDAndSumLoggedValues(connection);
        } else if (buttonText.equals("Get Max Line ID Between Dates")) {
            // Handle button click for "Get Max Line ID Between Dates"
            // Show the date range input dialog and then call the appropriate method from the Actions class
            Actions.showDateRangeInputDialog(connection, primaryStage);
        } else if (buttonText.equals("Run All")) {
            RunAllDialog dialog = new RunAllDialog(primaryStage, connection);
            dialog.showAndWait(); // Show the dialog and wait for it to close
        } else if (buttonText.equals("Threads")) {
            ThreadsDialog threads = new ThreadsDialog(connection, primaryStage);
            threads.showThreadsDialog();
        }
    }

}