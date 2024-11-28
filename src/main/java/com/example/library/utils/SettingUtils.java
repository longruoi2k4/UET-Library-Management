package com.example.library.utils;

/**
 * Singleton utility class for managing application settings related to highlighting.
 */
public class SettingUtils {
  private static volatile SettingUtils instance;
  private boolean highlightLate = false; // Giá trị mặc định
  private boolean highlightReturn = false;

  private SettingUtils() {
    // private constructor để ngăn chặn tạo instance từ bên ngoài
  }

  /**
   * Provides a global point of access to the singleton instance of SettingUtils.
   *
   * @return the singleton instance of SettingUtils
   */
  public static SettingUtils getInstance() {
    if (instance == null) {
      synchronized (SettingUtils.class) {
        if (instance == null) {
          instance = new SettingUtils();
        }
      }
    }
    return instance;
  }

  public boolean isHighlightLate() {
    return highlightLate;
  }

  public void setHighlightLate(boolean highlightLate) {
    this.highlightLate = highlightLate;
  }

  public boolean isHighlightReturn() {
    return highlightReturn;
  }

  public void setHighlightReturn(boolean highlightReturn) {
    this.highlightReturn = highlightReturn;
  }
}
