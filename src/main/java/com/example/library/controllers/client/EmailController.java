package com.example.library.controllers.client;

import com.example.library.services.IAccountService;
import com.example.library.services.impl.AccountServiceImpl;
import com.example.library.utils.AlertUtil;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

/**
 * Controller class that handles email-related actions in the application.
 */
public class EmailController {
  public TextField txtEmail;

  private final IAccountService accountService;

  /**
   * Constructs an EmailController object and initializes the account service
   * to an instance of AccountServiceImpl.
   */
  public EmailController() {
    this.accountService = new AccountServiceImpl();
  }

  /**
   * Handles the action event when the submit button is clicked.
   * This method retrieves the email from the text field, attempts to reset the password
   * using the account service, and displays a success or error alert based on the outcome.
   *
   * @param actionEvent The action event triggered by the submit button click.
   */
  public void btnSubmit(ActionEvent actionEvent) {
    System.out.println(txtEmail.getText());
    try {
      accountService.resetPassword(txtEmail.getText());
      AlertUtil.showAlert(
          Alert.AlertType.INFORMATION,
          "Success",
          null,
          "Reset password successfully, check your email for more information!");
    } catch (Exception e) {
      AlertUtil.showAlert(
          javafx.scene.control.Alert.AlertType.ERROR, "Error", null, e.getMessage());
    }
  }
}
