package com.example.library.utils;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Utility class for displaying different types of alerts.
 */
public class AlertUtil {

  /**
   * Displays an alert dialog with the given parameters.
   *
   * @param type the type of alert to be displayed, such as INFORMATION, WARNING, or ERROR.
   * @param title the title of the alert dialog.
   * @param header the header text of the alert dialog.
   * @param content the content text of the alert dialog.
   */
  public static void showAlert(Alert.AlertType type, String title, String header, String content) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(header);
    alert.setContentText(content);
    alert.showAndWait();
  }

  /**
   * Displays a confirmation alert with the specified message.
   *
   * @param message the message to be displayed in the confirmation alert
   * @return true if the user clicks OK, false otherwise
   */
  public static boolean showConfirmation(String message) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message);
    alert.setTitle("Xác nhận");
    alert.setHeaderText(null);
    Optional<ButtonType> result = alert.showAndWait();
    return result.isPresent() && result.get() == ButtonType.OK;
  }
}
