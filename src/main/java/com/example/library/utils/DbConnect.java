package com.example.library.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Singleton class for managing database connection to a MySQL database.
 * This class provides methods to execute SQL queries and updates.
 */
public class DbConnect { // singleton pattern
  private static DbConnect instance;
  private static final String JDBC_URL = "jdbc:mysql://localhost:3306/library";
  private static final String USERNAME = "root";
  private static final String PASSWORD = "GvhFC]w5n1dMgwen";

  private Connection connection;

  private DbConnect() {
    System.out.println("Create connection....");
    try {
      connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Provides a global point of access to the singleton instance of DbConnect.
   *
   * @return the singleton instance of DbConnect
   */
  public static DbConnect getInstance() { // khoi tao doi tuong singleton
    if (instance == null) {
      instance = new DbConnect();
    }
    return instance;
  }

  /**
   * Executes the given SQL query and returns the result set.
   *
   * @param sql the SQL query to be executed
   * @return the result set of the executed query, or null if an error occurs
   */
  public ResultSet executeQuery(String sql) { // nhung cau query co tra ve gia tri select
    try {
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery(sql);
      return resultSet;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Executes an SQL update statement such as INSERT, UPDATE, or DELETE.
   *
   * @param sql the SQL update statement to be executed
   */
  public void executeUpdate(
      String sql) { // nhung cau query khong tra ve gia tri // update, insert, delete
    try {
      Statement statement = connection.createStatement();
      statement.executeUpdate(sql);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
