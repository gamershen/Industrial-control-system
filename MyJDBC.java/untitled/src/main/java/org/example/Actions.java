package org.example;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.awt.Color.WHITE;

public class Actions {

    public static void showLoggedValueZeroRowsPieChart(Connection connection) {
        try {
            // Query to get the dates and count of zero-logged values for each date
            String query = "SELECT SUBSTRING_INDEX(LogTime, ' ', 1) AS LogDate, COUNT(*) AS Count FROM plswork WHERE LoggedValue = 0 GROUP BY SUBSTRING_INDEX(LogTime, ' ', 1)";

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // Create a map to store the count of zero-logged values for each date
            Map<String, Integer> dateCountMap = new HashMap<>();

            // Populate the map with data from the result set
            while (rs.next()) {
                String date = rs.getString("LogDate");
                int count = rs.getInt("Count");
                dateCountMap.put(date, count);
            }

            // Create a dataset for the pie chart
            DefaultPieDataset dataset = new DefaultPieDataset();

            // Add data to the dataset
            int total = dateCountMap.values().stream().mapToInt(Integer::intValue).sum(); // Calculate total count
            for (Map.Entry<String, Integer> entry : dateCountMap.entrySet()) {
                double percentage = (double) entry.getValue() / total * 100; // Calculate percentage
                dataset.setValue(entry.getKey() + " (" + String.format("%.2f", percentage) + "%)", entry.getValue());
            }

            // Create the pie chart
            JFreeChart chart = ChartFactory.createPieChart(
                    "Logged Values with Zero Rows",
                    dataset,
                    true, // Include legend
                    true, // Include tooltips
                    false // Do not include URLs
            );

            // Customize the chart appearance
            chart.setBackgroundPaint(WHITE);

            // Get the plot and customize its appearance
            chart.getPlot().setBackgroundPaint(WHITE);

            // Customize the font
            chart.getTitle().setFont(new Font("SansSerif", Font.HANGING_BASELINE, 20));
            chart.getLegend().setItemFont(new Font("Ariel", Font.TYPE1_FONT, 16));
            chart.getPlot().setOutlineVisible(false);

            // Create and show the chart panel
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(700, 600)); // Adjust size as needed

            JFrame frame = new JFrame();
            frame.setTitle("Logged Values with Zero Rows (Pie Chart)");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.getContentPane().add(chartPanel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error occurred while creating pie chart: " + e.getMessage());
        }
    }

    public static void GetSumLoggedValues(Connection connection) {

        String logIDInput = JOptionPane.showInputDialog(null, "Enter LogID:");
        if (logIDInput != null && !logIDInput.isEmpty()) {
            int logID = Integer.parseInt(logIDInput);
            try {
                CallableStatement stmt = connection.prepareCall("SELECT GetSumLoggedValues(?)");
                stmt.setInt(1, logID); // Use the user-provided logID
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    double sum = rs.getDouble(1);
                    String formattedSum = String.format("%.3f", sum);
                    JOptionPane.showMessageDialog(null, "Sum of LoggedValues for LogID " + logID + ": " + formattedSum);
                } else {
                    JOptionPane.showMessageDialog(null, "No LoggedValues found for LogID " + logID);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "An error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void getHighestLoggedValue(Connection connection) {
        try {
            CallableStatement stmt = connection.prepareCall("{CALL GetHighestLoggedValue()}");
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                int logID = resultSet.getInt("LogID");
                String lineID = resultSet.getString("LineID");
                String logTime = resultSet.getString("LogTime");
                double loggedValue = resultSet.getDouble("LoggedValue");
                String message = "Highest LoggedValue:\n" +
                        "LogID: " + logID + "\n" +
                        "LineID: " + lineID + "\n" +
                        "LogTime: " + logTime + "\n" +
                        "LoggedValue: " + loggedValue;
                JOptionPane.showMessageDialog(null, message);
            } else {
                JOptionPane.showMessageDialog(null, "No records found in the table.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "An error occurred: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void printLoggedValueZeroRows(Connection connection) {
        try {
            CallableStatement stmt = connection.prepareCall("{CALL GetLoggedValueZero()}");
            ResultSet resultSet = stmt.executeQuery();

            StringBuilder messageBuilder = new StringBuilder();
            while (resultSet.next()) {
                int logID = resultSet.getInt("LogID");
                String lineID = resultSet.getString("LineID");
                String logTime = resultSet.getString("LogTime");
                double loggedValue = resultSet.getDouble("LoggedValue");
                int cmdType = resultSet.getInt("CmdType");
                String description = resultSet.getString("Description");
                String unitType = resultSet.getString("UnitType");

                messageBuilder.append("LogID: ").append(logID).append("\n")
                        .append("LineID: ").append(lineID).append("\n")
                        .append("LogTime: ").append(logTime).append("\n")
                        .append("LoggedValue: ").append(loggedValue).append("\n")
                        .append("CmdType: ").append(cmdType).append("\n")
                        .append("Description: ").append(description).append("\n")
                        .append("UnitType: ").append(unitType).append("\n\n");
            }

            if (messageBuilder.length() > 0) {
                JTextArea textArea = new JTextArea(messageBuilder.toString());

                // Set font size to 16
                Font font = new Font("Arial", Font.PLAIN, 16);
                textArea.setFont(font);

                textArea.setLineWrap(true);
                textArea.setWrapStyleWord(true);

                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

                JFrame frame = new JFrame("Logged Values with Zero Rows");
                frame.getContentPane().add(scrollPane);
                frame.pack();
                frame.setSize(600, 400);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "No logged values with zero rows found.",
                        "Logged Values with Zero Rows", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void getMinLogIDAndSumLoggedValues(Connection connection) {
        try {
            String result;
            CallableStatement stmt = connection.prepareCall("{? = CALL GetMinLogIDAndSumLoggedValues()}");
            stmt.registerOutParameter(1, Types.VARCHAR);
            stmt.execute();
            result = stmt.getString(1);
            JOptionPane.showMessageDialog(null, result);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "An error occurred: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void findMaxSumLineIDBetweenDates(Connection connection, String startDateString, String endDateString) {
        try {
            List<String> lineIDs = new ArrayList<>();
            List<Double> sums = new ArrayList<>();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT LineID, SUM(LoggedValue) AS TotalSum FROM plswork WHERE LogTime BETWEEN '" +
                    startDateString + "' AND '" + endDateString + "' GROUP BY LineID");
            while (rs.next()) {
                String lineID = rs.getString("LineID");
                double sum = rs.getDouble("TotalSum");
                lineIDs.add(lineID);
                sums.add(sum);
            }
            if (!lineIDs.isEmpty()) {
                double maxSum = sums.get(0);
                int maxIndex = 0;
                for (int i = 1; i < sums.size(); i++) {
                    if (sums.get(i) > maxSum) {
                        maxSum = sums.get(i);
                        maxIndex = i;
                    }
                }
                JOptionPane.showMessageDialog(null, "LineID with the highest sum of LoggedValue between " + startDateString +
                        " and " + endDateString + ": " + lineIDs.get(maxIndex) + " (Sum: " + maxSum + ")");
            } else {
                JOptionPane.showMessageDialog(null, "No data found within the specified date range.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void showDateRangeInputDialog(Connection connection, Stage primaryStage) {
        Platform.runLater(() -> {
            DateRangeInputDialog dialog = new DateRangeInputDialog(primaryStage);
            dialog.showAndWait();
            if (dialog.isConfirmed()) {
                String startDate = dialog.getStartDate();
                String endDate = dialog.getEndDate();

                // Split the date string by forward slash and convert it to an array
                String[] parts = endDate.split("/");

                // Parse the parts of the date
                int day = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                int year = Integer.parseInt(parts[2]);

                // Convert the date to a LocalDate object for manipulation
                LocalDate endDateObject = LocalDate.of(year, month, day);

                // Add 1 day to the end date
                LocalDate adjustedEndDateObject = endDateObject.plusDays(1);

                // Format the adjusted end date back to the desired format
                String adjustedEndDate = adjustedEndDateObject.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                // Call the appropriate method from the Actions class with the selected date range
                Actions.findMaxSumLineIDBetweenDates(connection, startDate, adjustedEndDate);
            }
        });
    }

    public static void runAction(String actionName, Connection connection) {
        switch (actionName) {
            case "Get Sum of Logged Values":
                new Thread(() -> GetSumLoggedValues(connection)).start();
                break;
            case "Get Highest Logged Value":
                new Thread(() -> getHighestLoggedValue(connection)).start();
                break;
            case "Get Logged Values with Zero Rows":
                new Thread(() -> printLoggedValueZeroRows(connection)).start();
                break;
            case "Get Logged Values with Zero Rows (Pie Chart)":
                new Thread(() -> showLoggedValueZeroRowsPieChart(connection)).start();
                break;
            case "Get Min Log ID and Sum of Logged Values":
                new Thread(() -> getMinLogIDAndSumLoggedValues(connection)).start();
                break;
        }
    }



}

