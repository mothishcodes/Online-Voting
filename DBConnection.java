package com.onlinevoting.utils;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnection {
private static final String URL = "jdbc:mysql://localhost:3306/online_voting?useSSL=false&serverTimezone=UTC";
private static final String USER = "root"; // change to your DB user
private static final String PASS = "password"; // change to your DB password


static {
try {
Class.forName("com.mysql.cj.jdbc.Driver");
} catch (ClassNotFoundException e) {
e.printStackTrace();
}
}


public static Connection getConnection() throws SQLException {
return DriverManager.getConnection(URL, USER, PASS);
}
