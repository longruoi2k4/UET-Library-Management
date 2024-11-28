package com.example.library.controllers;

import com.example.library.models.Borrow;
import com.example.library.services.*;
import com.example.library.services.impl.BookServiceImpl;
import com.example.library.services.impl.BorrowServiceImpl;
import com.example.library.services.impl.ReaderServiceImpl;
import com.example.library.utils.AlertUtil;
import com.example.library.utils.SettingUtils;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

/**
 * Controller for managing borrow operations such as borrowing and returning books.
 * This class is responsible for initializing the borrow management view, handling user actions,
 * and interacting with the services for borrow, book, and reader management.
 */
public class BorrowManagementController implements Initializable {
  @FXML private ComboBox<String> cbFilterLate;
  @FXML private ComboBox<String> cbReaderId;
  @FXML private ComboBox<String> cbBookId;
  @FXML private DatePicker dpReturnDate;
  @FXML private Label lbReaderName;
  @FXML private Label lbBookName;
  @FXML private TableView<Borrow> tbBorrows;
  @FXML private TableColumn colId;
  @FXML private TableColumn colBookName;
  @FXML private TableColumn colReaderName;
  @FXML private TableColumn colBorrowDate;
  @FXML private TableColumn colReturnDate;
  @FXML private TableColumn colDueDate;

  private final IBorrowService borrowService;
  private final IBookService bookService;
  private final IReaderService readerService;
  private final SettingUtils settingUtils = SettingUtils.getInstance();

  /**
   * Constructs a new BorrowManagementController.
   * Initializes the services required for managing borrow operations.
   * These services include:
   * - BorrowService: Handles the core borrow logic and interactions with the borrow repository.
   * - BookService: Manages operations related to books, such as fetching book details.
   * - ReaderService: Manages operations related to readers, such as fetching reader information.
   */
  public BorrowManagementController() {
    borrowService = new BorrowServiceImpl();
    bookService = new BookServiceImpl();
    readerService = new ReaderServiceImpl();
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    loadBorrows();
    initComboBox();
    customDatePickers();

    tbBorrows.setRowFactory(
        tv ->
            new TableRow<Borrow>() {
              @Override
              protected void updateItem(Borrow borrow, boolean empty) {
                super.updateItem(borrow, empty);
                if (borrow == null) {
                  setStyle("");
                } else {
                  if (borrow.getDueDate() != null && settingUtils.isHighlightReturn()) {
                    setStyle("-fx-background-color: green");
                  } else {
                    setStyle("");
                  }
                  if (borrow.getDueDate() == null
                      && borrow.getReturnDate().isBefore(LocalDate.now())
                      && settingUtils.isHighlightLate()) {
                    setStyle("-fx-background-color: red;");
                  }
                }
              }
            });
  }

  private void initComboBox() {
    cbFilterLate.getItems().addAll("Quá hạn", "Chưa trả", "Đã trả", "Không");
    cbFilterLate.getSelectionModel().selectLast();
    cbBookId.getItems().addAll(bookService.getAllBookId());
    cbReaderId.getItems().addAll(readerService.getAllReaderId());
  }

  private void loadBorrows() {
    colId.setCellValueFactory(new PropertyValueFactory<>("borrowId"));
    colBookName.setCellValueFactory(new PropertyValueFactory<>("bookName"));
    colReaderName.setCellValueFactory(new PropertyValueFactory<>("readerName"));
    colBorrowDate.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
    colReturnDate.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
    colDueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));

    tbBorrows.setItems(borrowService.getAllBookBorrowed());
  }

  /**
   * Handles the event triggered when a reader ID is selected from the combo box.
   * This method retrieves the selected reader ID from the combo box (cbReaderId),
   * fetches the associated reader name using the readerService, and updates the
   * label (lbReaderName) with the fetched reader name.
   *
   * @param actionEvent the event that triggers the method, typically associated
   *                    with selecting an item in a combo box.
   */
  public void onChooseReaderId(ActionEvent actionEvent) {
    String readerId = cbReaderId.getValue();
    lbReaderName.setText(readerService.getReaderNameById(readerId));
  }

  /**
   * Handles the event triggered when a book ID is selected from the combo box.
   * This method retrieves the selected book ID from the combo box (cbBookId),
   * fetches the associated book name using the bookService, and updates the
   * label (lbBookName) with the fetched book name.
   *
   * @param actionEvent the event that triggers the method, typically associated
   *                    with selecting an item in a combo box.
   */
  public void onChooseBookId(ActionEvent actionEvent) {
    String bookId = cbBookId.getValue();
    lbBookName.setText(bookService.getBookNameById(bookId));
  }

  private void customDatePickers() {
    dpReturnDate.setDayCellFactory(
        new Callback<DatePicker, DateCell>() {
          @Override
          public DateCell call(DatePicker datePicker) {
            return new DateCell() {
              @Override
              public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item.isBefore(LocalDate.now())) {
                  setDisable(true);
                  setStyle("-fx-background-color: #ffc0cb;");
                }
              }
            };
          }
        });
  }

  /**
   * Handles the borrow book action event. This method performs the following checks and actions:
   * 1. Validates that the reader, book, and return date are selected.
   * 2. Ensures the selected reader has not exceeded the borrow limit of 3 books.
   * 3. Checks if the selected book is available in sufficient quantity.
   * If all checks pass,it creates and registers a new borrow instance and updates the borrow table.
   *
   * @param actionEvent The event that triggers this method, typically associated with clicking
   *                    the borrow button.
   */
  public void onClickBorrow(ActionEvent actionEvent) {

    if (isNull(cbReaderId.getValue(), cbBookId.getValue(), dpReturnDate.getValue())) {
      AlertUtil.showAlert(
          Alert.AlertType.ERROR, "Lỗi", null, "Vui lòng chọn độc giả, sách và ngày trả");
      return;
    }

    if (borrowService.getTotalBorrowByReaderId(cbReaderId.getValue()) >= 3) {
      AlertUtil.showAlert(
          Alert.AlertType.ERROR, "Lỗi", null, "Độc giả đã mượn quá số lượng cho phép (<=3)");
      return;
    }

    if (!bookService.isQuantityEnough(cbBookId.getValue())) {
      AlertUtil.showAlert(Alert.AlertType.ERROR, "Lỗi", null, "Số lượng sách không đủ");
      return;
    }

    Borrow borrow =
        Borrow.builder()
            .bookName(cbBookId.getValue())
            .readerName(cbReaderId.getValue())
            .borrowDate(LocalDate.now())
            .returnDate(dpReturnDate.getValue())
            .dueDate(null)
            .build();

    borrowService.borrowBook(borrow);
    AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Thông báo", null, "Mượn sách thành công");
    tbBorrows.setItems(borrowService.getAllBookBorrowed());
  }

  private boolean isNull(Object... o) {
    for (Object obj : o) {
      if (obj == null || obj.toString().isEmpty()) {
        return true;
      }
    }
    return false;
  }

  public void onSelected(MouseEvent mouseEvent) {}

  /**
   * Handles the event triggered when a filter option is selected from the filter combo box.
   * This method retrieves the selected filter value and filters the list of borrowed book records
   * based on the selected criteria. The criteria include filtering by books not yet returned,
   * books already returned, and overdue books.
   *
   * @param actionEvent the event that triggers the method, typically associated with selecting
   *                    an item in a combo box.
   */
  public void onChooseFilter(ActionEvent actionEvent) {
    String filter = cbFilterLate.getValue();
    ObservableList<Borrow> borrows = borrowService.getAllBookBorrowed();

    switch (filter) {
      case "Chưa trả":
        borrows = borrows.filtered(borrow -> borrow.getDueDate() == null);
        break;
      case "Đã trả":
        borrows = borrows.filtered(borrow -> borrow.getDueDate() != null);
        break;
      case "Quá hạn":
        LocalDate today = LocalDate.now();
        borrows =
            borrows.filtered(
                borrow -> {
                  String returnDate = String.valueOf(borrow.getReturnDate());
                  return returnDate != null
                      && !returnDate.isEmpty()
                      && LocalDate.parse(returnDate).isBefore(today);
                });
        break;
      default:
        break;
    }

    tbBorrows.setItems(borrows);
  }

  /**
   * Handles the action event triggered by clicking the refresh button.
   * This method resets the selection and input fields in the borrow management view.
   *
   * @param actionEvent the event that triggers this method, typically associated
   *                    with clicking*/
  public void onClickRefresh(ActionEvent actionEvent) {
    tbBorrows.getSelectionModel().clearSelection();
    cbFilterLate.getSelectionModel().selectLast();
    cbBookId.getSelectionModel().clearSelection();
    cbReaderId.getSelectionModel().clearSelection();
    dpReturnDate.setValue(null);
    lbBookName.setText("Tên sách");
    lbReaderName.setText("Tên độc giả");
  }
}
