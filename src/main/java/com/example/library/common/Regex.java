package com.example.library.common;

/**
 * The `Regex` class provides constant regular expressions and validation methods for common patterns.
 */
public class Regex {
  public static final String INTEGER_NUMBER = "^[1-9]\\d*$"; // 112312312312 // -123/ abcsdf
  public static final String EMAIL = "[A-Za-z0-9+_.-]+@(.+)$";
  public static final String PHONE_NUMBER = "^(0[1-9]|84[1-9])[0-9]{8}$";
  public static final String ISBN = "^(?:\\d{9}[\\dX]|\\d{13})$";
  
  public static boolean isValid(String regex, String value) {
    switch (regex) {
      case "INTEGER_NUMBER":
        return value.matches(INTEGER_NUMBER);
      case "EMAIL":
        return value.matches(EMAIL);
      case "PHONE_NUMBER":
        return value.matches(PHONE_NUMBER);
      case "ISBN":
        return value.matches(ISBN);
      default:
        return false;
    }
  }
}
