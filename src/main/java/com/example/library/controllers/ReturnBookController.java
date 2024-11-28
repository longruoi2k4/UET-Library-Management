package com.example.library.controllers;

import com.example.library.models.Borrow;
import com.example.library.services.IBorrowService;
import com.example.library.services.impl.BorrowServiceImpl;
import com.example.library.utils.AlertUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

/**
 * Controller for managing the return of books in the library system.
 * Provides functionality to handle the return process when a user triggers the return action.
 */
public class ReturnBookController {
  @FXML private TextField txtBookId;
  @FXML private TextField txtReaderId;

  private final IBorrowService borrowService;

  /**
   * Initializes a new instance of the ReturnBookController.
   * This constructor sets up the necessary services to handle book return operations.
   */
  public ReturnBookController() {
    this.borrowService = new BorrowServiceImpl();
  }

  /**
   * Handles the event when the OK button is clicked for returning a book.
   * This method validates the input fields for book ID and reader ID,
   * creates a borrow object, and processes the book return through the borrow service.
   * If there are any errors or exceptions, appropriate alerts are shown to the user.
   *
   * @param actionEvent The action event triggered by clicking the OK button.
   */
  public void onClickOk(ActionEvent actionEvent) {
    String bookId = txtBookId.getText();
    String readerId = txtReaderId.getText();

    if (bookId.isEmpty() || readerId.isEmpty()) {
      AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", null, "Please fill all fields");
      return;
    }

    Borrow borrow = Borrow.builder().bookId(bookId).readerId(readerId).build();

    try {
      borrowService.returnBook(borrow);
    } catch (Exception e) {
      AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", null, e.getMessage());
      return;
    }

    AlertUtil.showAlert(
        Alert.AlertType.INFORMATION, "Information", null, "Return book successfully");
  }
}
