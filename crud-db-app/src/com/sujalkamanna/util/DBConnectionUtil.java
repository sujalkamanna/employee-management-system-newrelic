package com.sujalkamanna.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionUtil {

private static final String URL =
        "jdbc:mysql://mysql:3306/employeedirectory";

private static final String DRIVER =
        "com.mysql.jdbc.Driver";

private static final String USERNAME =
        "root";

private static final String PASSWORD =
        "root";

private static Connection connection = null;

public static Connection openConnection() {

    if (connection != null) {
        return connection;
    }

    try {

        Class.forName(DRIVER);

        connection = DriverManager.getConnection(
                URL,
                USERNAME,
                PASSWORD
        );

    } catch (ClassNotFoundException | SQLException e) {

        e.printStackTrace();

    }

    return connection;
}

}