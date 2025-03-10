package com.example.library.controllers;

import com.example.library.models.Account;
import com.example.library.services.IAccountService;
import com.example.library.services.impl.AccountServiceImpl;
import com.example.library.utils.AlertUtil;
import com.example.library.utils.UserContext;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;

/**
 * Controller class responsible for handling user actions related to changing passwords.
 * This class communicates with the view components, retrieves user input, and interacts
 * with the account service to perform password changes.
 */
public class ChangePasswordController {
  @FXML private PasswordField pwCur;
  @FXML private PasswordField pwNew;
  @FXML private PasswordField pwReEnter;

  private final IAccountService accountService;

  /**
   * Default constructor initializing the ChangePasswordController.
   * This constructor sets up the account service that this controller will use
   * to handle password change operations.
   */
  public ChangePasswordController() {
    this.accountService = new AccountServiceImpl();
  }

  /**
   * Handles the event triggered when the "OK" button is clicked to change the user's password.
   * This method retrieves the current password, new password, and re-entered password from the
   * respective password fields. It then verifies whether the new password and re-entered password
   * match.
   * If they do, it attempts to change the password of the currently logged-in user by calling
   * the account service. Appropriate alerts are shown to the user based on the outcome.
   *
   * @param actionEvent the event triggered by clicking the "OK" button.
   */
  public void onClickOk(ActionEvent actionEvent) {
    String oldPassword = pwCur.getText();
    String newPassword = pwNew.getText();
    String reEnterPassword = pwReEnter.getText();

    if (!newPassword.equals(reEnterPassword)) {
      AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", null, "Re-type password not match!");
      return;
    }

    Account account =
        Account.builder()
            .role(UserContext.getInstance().getRole())
            .password(oldPassword)
            .username(UserContext.getInstance().getUsername())
            .build();

    try {
      accountService.changePassword(account, newPassword);
      AlertUtil.showAlert(
          Alert.AlertType.INFORMATION, "Information", null, "Change password successfully!");

    } catch (Exception e) {
      AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", null, e.getMessage());
    }
  }
}
