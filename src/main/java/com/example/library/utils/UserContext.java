package com.example.library.utils;

/**
 * Singleton class that manages the context of user data for an application.
 * This class holds the user's role, username, and reader ID.
 */
public class UserContext {
  private static UserContext instance;
  private String role;
  private String username;
  private String readerId;

  public String getReaderId() {
    return readerId;
  }

  public void setReaderId(String readerId) {
    this.readerId = readerId;
  }

  private UserContext() {}

  /**
   * Provides a global point of access to the singleton instance of UserContext.
   *
   * @return the singleton instance of UserContext
   */
  public static UserContext getInstance() {
    if (instance == null) {
      instance = new UserContext();
    }
    return instance;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getUsername() {
    return username;
  }

  /**
   * Clears the current context by resetting the role, readerId, and username fields to
   * empty strings.
   */
  public void clearContext() {
    this.role = "";
    this.readerId = "";
    this.username = "";
  }

  public String getRole() {
    return role;
  }
}
