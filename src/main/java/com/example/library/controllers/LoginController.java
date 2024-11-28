package com.example.library.controllers;

import com.example.library.App;
import com.example.library.models.Account;
import com.example.library.services.IAccountService;
import com.example.library.services.impl.AccountServiceImpl;
import com.example.library.utils.AlertUtil;
import com.example.library.utils.UserContext;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Controller class for managing login functionality.
 * This class handles the login process, including validation of user input
 * and interaction with the account service to verify user credentials.
 * It also provides options for resetting passwords and closing the program.
 */
public class LoginController implements Initializable {
  @FXML private ComboBox<String> cbRole;
  @FXML private TextField txtTaiKhoan;
  @FXML private PasswordField pwMatKhau;
  @FXML private AnchorPane pane;

  private final IAccountService accountService;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    cbRole.getItems().addAll("Librarian", "Reader");
    cbRole.setValue("Reader");
    try {
      FXMLLoader loader =
          new FXMLLoader(getClass().getResource("/com/example/library/RegisterFrm.fxml"));
      pane.getChildren().clear();
      AnchorPane newPane = loader.load();
      pane.getChildren().add(newPane);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Default constructor for the LoginController class.
   * This constructor initializes an instance of the LoginController
   * by creating an instance of AccountServiceImpl and assigning it to
   * the accountService field.
   */
  public LoginController() {
    this.accountService = new AccountServiceImpl();
  }

  /**
   * Handles the login button click event, validating username and password inputs,
   * checking account credentials, and updating user context or showing error messages as needed.
   *
   * @param actionEvent The action event triggered by clicking the login button.
   * @throws IOException if an input-output exception occurs while loading the next scene.
   */
  public void onClickLogin(ActionEvent actionEvent) throws IOException {
    String username = txtTaiKhoan.getText();
    String password = pwMatKhau.getText();
    String role = cbRole.getValue();

    if (username.isEmpty() || password.isEmpty()) {
      AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", null, "Username or password is empty!");
      return;
    }

    Account account = Account.builder().username(username).password(password).role(role).build();
    if (accountService.checkAccount(account)) {

      if (accountService.isBlocked(username)) {
        AlertUtil.showAlert(
            Alert.AlertType.ERROR,
            "Error",
            null,
            "Your account is blocked, please contact admin for more information!");
        return;
      }

      AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Information", null, "Login successfully!");

      UserContext.getInstance().setRole(role);
      UserContext.getInstance().setUsername(username);

      App.setRoot("DashboardFrm");

    } else {
      AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", null, "Username or password wrong!");
    }
  }

  /**
   * Handles the reset password button click event. This method will change the
   * root of the application's*/
  public void onClickResetPassword(ActionEvent actionEvent) throws IOException {
    App.setRootPop("EmailFrm", "Reset password", false);
  }

  /**
   * Handles the event of closing the program by confirming with the user.
   *
   * @param event The mouse event that triggers the close program action.
   */
  public void closeProgram(MouseEvent event) {
    // Lấy Stage chứa AnchorPane `pane`
    Stage currentStage = (Stage) pane.getScene().getWindow();

    // Tạo Alert để hỏi người dùng có muốn thoát hay không
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Confirm Exit");
    alert.setHeaderText(null);
    alert.setContentText("Are you sure you want to exit?");

    // Tạo sự kiện khi người dùng chọn nút OK hoặc Cancel
    ButtonType result =
        alert
            .showAndWait()
            .orElse(ButtonType.CANCEL); // Nếu tắt cửa sổ thì mặc định là ButtonType.CANCEL

    // Nếu người dùng chọn OK, thì đóng chương trình
    if (result == ButtonType.OK) {
      currentStage.close();
    }
  }
}
