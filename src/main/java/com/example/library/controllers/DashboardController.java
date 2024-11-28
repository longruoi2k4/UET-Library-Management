package com.example.library.controllers;

import com.example.library.App;
import com.example.library.services.IBorrowService;
import com.example.library.services.impl.BorrowServiceImpl;
import com.example.library.utils.AlertUtil;
import com.example.library.utils.UserContext;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * DashboardController is responsible for managing the main dashboard of the application.
 * It handles the initialization and control of various UI components and actions based on
 * the user's role.
 * Different views and functionalities are dynamically loaded and displayed according to
 * the selected menu item.
 */
public class DashboardController implements Initializable {
  @FXML private ListView<String> lstMenu;
  @FXML private AnchorPane pane;
  @FXML Label label;

  private final IBorrowService borrowService;

  /**
   * DashboardController is responsible for handling the operations related to the dashboard
   * functionality of the application. It initializes necessary services and manages
   * the user interface elements associated with the dashboard.
   */
  public DashboardController() {
    this.borrowService = new BorrowServiceImpl();
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    System.out.println("DashboardController");

    String username = UserContext.getInstance().getUsername();
    label.setText("Xin chào, " + (username != null ? username : "Người dùng"));

    initMenuByRole(UserContext.getInstance().getRole());

    if (borrowService.isReaderLate(UserContext.getInstance().getReaderId())) {
      AlertUtil.showAlert(
          Alert.AlertType.WARNING,
          "Warning",
          null,
          "You have late book, please return it as soon as possible");
    }
  }

  /**
   * Initializes the menu based on the user's role.
   *
   * @param role the role of the user, which determines the menu options to display.
   *             Accepted values are "reader" and "librarian".
   */
  public void initMenuByRole(String role) {
    if (role.equalsIgnoreCase("reader")) {
      lstMenu.getItems().addAll("Available Book", "History Borrow", "Request Borrow");
      loadPane("Information");
    }

    if (role.equalsIgnoreCase("librarian")) {
      lstMenu
          .getItems()
          .addAll("Home", "Book Manage", "Reader Manage", "Borrow Manage", "Request Manage", "Return Book");
    }

    lstMenu.getSelectionModel().selectFirst();
    loadPane(lstMenu.getSelectionModel().getSelectedItem());
  }

  /**
   * Handles the event when a menu item is selected from the list.
   * It retrieves the selected menu item and loads the corresponding pane.
   *
   * @param mouseEvent the mouse event that triggered the selection action.
   */
  public void onSelected(MouseEvent mouseEvent) {
    String selectedItem = lstMenu.getSelectionModel().getSelectedItem();
    if (selectedItem != null) {
      loadPane(selectedItem);
    }
  }

  /**
   * Handles the event when the "Information" menu item is selected.
   * It loads the Information pane.
   *
   * @param actionEvent the action event that triggered the "Information" menu item selection.
   */
  public void onShowInformation(ActionEvent actionEvent) {
    loadPane("Information");
  }

  private void loadPane(String selected) {
    String frm = "";

    switch (selected) {
      case "Information":
        frm = "/com/example/library/InformationFrm.fxml";
        break;
      case "Book Manage":
        frm = "/com/example/library/BookManagementFrm.fxml";
        break;
      case "Reader Manage":
        frm = "/com/example/library/ReaderManagementFrm.fxml";
        break;
      case "Borrow Manage":
        frm = "/com/example/library/BorrowManagementFrm.fxml";
        break;
      case "Home":
        frm = "/com/example/library/StaticFrm.fxml";
        break;

      case "Request Manage":
        frm = "/com/example/library/RequestFrm.fxml";
        break;

      case "Return Book":
        frm = "/com/example/library/ReturnFrm.fxml";
        break;
      // client
      case "Available Book":
        frm = "/com/example/library/BookManagementFrm.fxml";
        break;
      case "History Borrow":
        frm = "/com/example/library/BorrowHistoryFrm.fxml";
        BorrowHistoryController.setReaderId(UserContext.getInstance().getReaderId());
        break;
      case "Request Borrow":
        frm = "/com/example/library/RequestFrm.fxml";
        break;
      default:
        break;
    }

    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(frm));
      pane.getChildren().clear();
      AnchorPane newPane = loader.load();
      pane.getChildren().add(newPane);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Handles the logout action by clearing the user's context and navigating to the login form.
   *
   * @param actionEvent the action event that triggered the logout action.
   * @throws IOException if an input or output exception occurs during the navigation to
   *                     the login form.
   */
  public void onClickLogout(ActionEvent actionEvent) throws IOException {
    UserContext.getInstance().clearContext();

    App.setRoot("LoginFrm");
  }

  /**
   * Handles the event when the "Change Password" action is triggered.
   * It opens a dialog for changing the user's password.
   *
   * @param actionEvent the action event that triggered the change password action
   * @throws IOException if an input or output exception occurs during the loading of the change
   *                     password form
   */
  public void onClickChangePassword(ActionEvent actionEvent) throws IOException {
    App.setRootPop("ChangePassFrm", "Change password", false);
  }
}
