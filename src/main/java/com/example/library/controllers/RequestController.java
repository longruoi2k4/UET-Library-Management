package com.example.library.controllers;

import com.example.library.models.Borrow;
import com.example.library.services.IBorrowService;
import com.example.library.services.impl.BorrowServiceImpl;
import com.example.library.services.impl.MailService;
import com.example.library.utils.AlertUtil;
import com.example.library.utils.UserContext;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

/**
 * Controller for handling book borrow requests in a library system.
 * This controller is responsible for loading the requests into the table, handling search
 * functionality, and processing approve/reject/delete actions for the requests.
 */
public class RequestController implements Initializable {
  @FXML private TableColumn colBookId;
  @FXML private TableColumn colSelect;
  @FXML private final IBorrowService borrowService;
  @FXML private TableColumn<Borrow, Integer> colId;
  @FXML private TableView<Borrow> tbRequest;
  @FXML private TableColumn colReaderName;
  @FXML private TableColumn colBookName;
  @FXML private TableColumn colBorrowDate;
  @FXML private TableColumn colReturnDate;
  @FXML private Button btnApprove;
  @FXML private TextField txtSearch;
  @FXML private Button btnReject;
  private final List<String> selectedBorrowId;
  private final MailService mailService;

  /**
   * Constructs a new RequestController with initialized MailService and BorrowServiceImpl.
   * This controller handles various operations related to borrow requests such as approving,
   * rejecting, and deleting requests.
   * MailService is set up with "smtp.gmail.com".
   */
  public RequestController() {
    this.mailService = new MailService("smtp.gmail.com");
    this.borrowService = new BorrowServiceImpl();
    selectedBorrowId = new ArrayList<>();
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    setDataToTable();

    if (UserContext.getInstance().getRole().equals("Reader")) {
      setUpForReader();
    } else {
      btnApprove.setOnAction(this::onClickApprove);
      txtSearch.setPromptText("Search by Book Name or Reader Name");
      btnReject.setVisible(true);
    }
  }

  private void setDataToTable() {
    colId.setCellValueFactory(
        cellData ->
            new SimpleIntegerProperty(tbRequest.getItems().indexOf(cellData.getValue()) + 1)
                .asObject());
    colReaderName.setCellValueFactory(new PropertyValueFactory<>("readerName"));
    colBookName.setCellValueFactory(new PropertyValueFactory<>("bookName"));
    colBookId.setCellValueFactory(new PropertyValueFactory<>("bookId"));
    colBorrowDate.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
    colReturnDate.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
    colSelect.setCellFactory(
        tc ->
            new TableCell<Borrow, Boolean>() {
              private final CheckBox checkBox = new CheckBox();

              {
                checkBox.setOnAction(
                    event -> {
                      Borrow borrow = getTableRow().getItem();
                      if (checkBox.isSelected()) {
                        selectedBorrowId.add(borrow.getBorrowId());
                      } else {
                        selectedBorrowId.remove(borrow.getBorrowId());
                      }

                      System.out.println(selectedBorrowId);
                    });
              }

              @Override
              protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                  setGraphic(null);
                } else {
                  setGraphic(checkBox);
                }
              }
            });

    if (UserContext.getInstance().getRole().equals("Reader")) {
      tbRequest.setItems(
          borrowService.getAllRequestByReaderId(UserContext.getInstance().getReaderId()));
    } else {
      tbRequest.setItems(borrowService.getAllRequestBorrow());
    }
  }

  private void setUpForReader() {
    colReaderName.setVisible(false);
    btnApprove.setText("Delete");
    btnApprove.setOnAction(this::onClickDeleteRequest);
    btnReject.setVisible(false);
    txtSearch.setPromptText("Search by Book Name");
  }

  /**
   * Handles the click event for approving a borrow request. This method performs
   * several operations including verifying if a request is selected, retrieving
   * email messages for the selected request, sending approval emails, approving
   * the request in the system, updating the data table, and displaying an alert
   * for the action performed.
   *
   * @param actionEvent the action event that triggers this method, typically a button click.
   */
  public void onClickApprove(ActionEvent actionEvent) {
    if (selectedBorrowId.isEmpty()) {
      AlertUtil.showAlert(
          Alert.AlertType.ERROR, "No Request Selected", null, "Please select a request to approve");
      return;
    }

    Map<String, String> emailMessages =
        borrowService.getAllEmailWithMessagesByBorrowIds(selectedBorrowId);

    mailService.sendMail(emailMessages, "Request Approved");

    borrowService.approveRequest(selectedBorrowId);
    setDataToTable();
    AlertUtil.showAlert(
        Alert.AlertType.INFORMATION, "Request Approved", null, "Request Approved Successfully");
    selectedBorrowId.clear();
  }

  /**
   * Handles the click event for rejecting a borrow request. This method performs
   * several operations including verifying if a request is selected, declining the request,
   * retrieving email messages for the rejected request*/
  public void onClickReject(ActionEvent actionEvent) {
    if (selectedBorrowId.isEmpty()) {
      AlertUtil.showAlert(
          Alert.AlertType.ERROR, "No Request Selected", null, "Please select a request to reject");
      return;
    }

    borrowService.declineRequest(selectedBorrowId);

    Map<String, String> emailMessages =
        borrowService.getAllEmailWithMessagesByBorrowIds(selectedBorrowId);

    mailService.sendMail(emailMessages, "Request Declined");

    setDataToTable();
    borrowService.deleteRequest(selectedBorrowId);

    AlertUtil.showAlert(
        Alert.AlertType.INFORMATION, "Request Declined", null, "Request Declined Successfully");
    selectedBorrowId.clear();
  }

  /**
   * Handles the click event for deleting a borrow request. This method performs
   * several operations including verifying if a request is selected, confirming
   * the delete action, deleting the request through the borrow service,
   * updating the data table, and displaying an alert for the action performed.
   *
   * @param actionEvent the action event that triggers this method, typically a button click.
   */
  public void onClickDeleteRequest(ActionEvent actionEvent) {
    if (selectedBorrowId.isEmpty()) {
      AlertUtil.showAlert(
          Alert.AlertType.ERROR, "No Request Selected", null, "Please select a request to delete");
      return;
    }

    if (!AlertUtil.showConfirmation("Are you sure you want to delete this request?")) {
      return;
    }

    borrowService.deleteRequest(selectedBorrowId);
    setDataToTable();

    AlertUtil.showAlert(
        Alert.AlertType.INFORMATION, "Request Deleted", null, "Request Deleted Successfully");

    selectedBorrowId.clear();
  }

  /**
   * Handles the search functionality for borrow requests. Depending on the role of the
   * user (Reader or other), it filters the borrow requests based on the search keyword
   * entered in the text field and updates the table with the filtered results.
   *
   * @param keyEvent the key event that triggers this method, typically a key press in the search
   *                 field.
   */
  public void onSearch(KeyEvent keyEvent) {
    String keyword = txtSearch.getText();
    if (keyword.isEmpty()) {
      setDataToTable();
      return;
    }
    if (UserContext.getInstance().getRole().equals("Reader")) {
      ObservableList<Borrow> borrows =
          borrowService.getAllRequestByReaderId(UserContext.getInstance().getReaderId());
      ObservableList<Borrow> filteredBorrows =
          borrows.filtered(
              borrow -> borrow.getBookName().toLowerCase().contains(keyword.toLowerCase()));
      tbRequest.setItems(filteredBorrows);
    } else {
      ObservableList<Borrow> borrows = borrowService.getAllRequestBorrow();
      ObservableList<Borrow> filteredBorrows =
          borrows.filtered(
              borrow ->
                  borrow.getBookName().toLowerCase().contains(keyword.toLowerCase())
                      || borrow.getReaderName().toLowerCase().contains(keyword.toLowerCase()));

      tbRequest.setItems(filteredBorrows);
    }
  }
}
