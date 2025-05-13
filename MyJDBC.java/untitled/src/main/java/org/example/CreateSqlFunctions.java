package org.example;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateSqlFunctions {
    public static void createGetSumLoggedValues(Connection connection) {
        String functionDefinition = "CREATE DEFINER=`root`@`localhost` FUNCTION `GetSumLoggedValues`(p_LogID INT) RETURNS DOUBLE\n" +
                "DETERMINISTIC\n" +
                "BEGIN\n" +
                "  DECLARE SumLoggedValues DOUBLE;\n" +
                "  SELECT SUM(LoggedValue) INTO SumLoggedValues\n" +
                "  FROM plswork\n" +
                "  WHERE LogID = p_LogID\n" +
                "  GROUP BY LogID;\n" +
                "  RETURN SumLoggedValues;\n" +
                "END";
        String procedureDefinition1 = "CREATE DEFINER=`root`@`localhost` PROCEDURE `GetHighestLoggedValue`()\n" +
                "BEGIN\n" +
                "  SELECT LogID, LineID, LogTime, LoggedValue\n" +
                "  FROM plswork\n" +
                "  ORDER BY LoggedValue DESC\n" +
                "  LIMIT 1;\n" +
                "END";
        String procedureDefinition2 = "CREATE DEFINER=`root`@`localhost` PROCEDURE `GetLoggedValueZero`()\n" +
                "BEGIN\n" +
                "  SELECT *\n" +
                "  FROM plswork\n" +
                "  WHERE LoggedValue = 0;\n" +
                "END";
        String functionDefinition2 = "CREATE DEFINER=`root`@`localhost` FUNCTION `GetMinLogIDAndSumLoggedValues`() RETURNS VARCHAR(255) CHARSET UTF8MB4\n" +
                "DETERMINISTIC\n" +
                "BEGIN\n" +
                "  DECLARE MinLogID INT;\n" +
                "  DECLARE MinSum DOUBLE;\n" +
                "  DECLARE finished INTEGER DEFAULT 0;\n" +
                "  DECLARE cur CURSOR FOR\n" +
                "    SELECT LogID, SUM(LoggedValue) AS SumLoggedValues\n" +
                "    FROM plswork\n" +
                "    WHERE LogID IN (1, 3, 4, 5, 6)\n" +
                "    GROUP BY LogID;\n" +
                "  DECLARE CONTINUE HANDLER FOR NOT FOUND SET finished = 1;\n" +
                "  OPEN cur;\n" +
                "  FETCH cur INTO MinLogID, MinSum;\n" +
                "  WHILE finished = 0 DO\n" +
                "    FETCH cur INTO MinLogID, MinSum;\n" +
                "  END WHILE;\n" +
                "  CLOSE cur;\n" +
                "  RETURN CONCAT('Minimum LogID: ', MinLogID, ', Sum of LoggedValues: ', MinSum);\n" +
                "END";

        try {
            Statement statement = connection.createStatement();
            statement.execute(functionDefinition);

            Statement statement1 = connection.createStatement();
            statement1.execute(procedureDefinition1);

            Statement statement2 = connection.createStatement();
            statement2.execute(procedureDefinition2);

            Statement statement3 = connection.createStatement();
            statement3.execute(functionDefinition2);


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}