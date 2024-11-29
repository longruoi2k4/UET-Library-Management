package com.example.library.controllers;

import com.example.library.App;
import com.example.library.models.Book;
import com.example.library.services.IBookService;
import com.example.library.services.IBorrowService;
import com.example.library.services.impl.BookServiceImpl;
import com.example.library.services.impl.BorrowServiceImpl;
import com.example.library.utils.AlertUtil;
import com.example.library.utils.GoogleBooksAPI;
import com.example.library.utils.UserContext;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static com.example.library.common.Regex.isValid;

/**
 * The {@code BookManagementController} class manages book-related operations in the UI.
 * It provides various functionalities such as adding, updating, deleting, searching, and requesting
 * to borrow books.
 * This class initializes the user interface components and handles events triggered
 * by user interactions.
 */
public class BookManagementController implements Initializable {
  @FXML private Button btnRequestBorrow;
  @FXML private DatePicker dpReturn;
  @FXML private GridPane grForReader;
  @FXML private Text lbCategory;
  @FXML private Text lbQuantity;
  @FXML private Text lbIsbn;
  @FXML private Text lbPublishDate;
  @FXML private Text lbBookId;
  @FXML private Text lbBookName;
  @FXML private Text lbAuthor;
  @FXML private TextField txtAuthor;
  @FXML private Button btnAddAuthor;
  @FXML private TextField txtSearch;
  @FXML private Button btnAddCategory;
  @FXML private ComboBox<String> cbCategory;
  @FXML private Button btnAdd;
  @FXML private Button btnDelete;
  @FXML private Button btnUpdate;
  @FXML private Button btnSearchByApi;
  @FXML private TextField txtBookId;
  @FXML private TextField txtBookName;
  @FXML private TextField txtCategory;
  @FXML private TextField txtQuantity;
  @FXML private TextField txtIsbn;
  @FXML private DatePicker dpPublish;
  @FXML private ComboBox<String> cbAuthor;
  @FXML private TableView<Book> tbBooks;
  @FXML private TableColumn<Book, String> colBookId;
  @FXML private TableColumn<Book, String> colBookName;
  @FXML private TableColumn<Book, String> colAuthorName;
  @FXML private TableColumn<Book, String> colCategory;
  @FXML private TableColumn<Book, Integer> colQuantity;
  @FXML private TableColumn<Book, LocalDate> colPublishDate;
  @FXML private TableColumn<Book, String> colIsbn;
  @FXML private ProgressBar progressBar;

  private final IBookService bookService;
  private final IBorrowService borrowService;
  private boolean isAddingCategory = false;
  private boolean isAddingAuthor = false;

  /**
   * Default constructor for the BookManagementController class.
   * It initializes the BookServiceImpl and BorrowServiceImpl instances,
   * which are used to manage book-related and borrowing operations within the application.
   * This constructor is responsible for setting up these services
   * to facilitate communication between the controller and the underlying data repositories.
   */
  public BookManagementController() {
    this.bookService = new BookServiceImpl(); // dependency injection
    this.borrowService = new BorrowServiceImpl();
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) { // khoi tao ban dau

    progressBar.setProgress(0);

    txtCategory.setVisible(false);
    cbCategory.setVisible(true);

    txtAuthor.setVisible(false);
    cbAuthor.setVisible(true);

    loadBooksOnTable();
    initComboBox();
    customDatePicker();

    btnRequestBorrow.setDisable(true);

    txtBookId.setText(bookService.getBookId());

    if (UserContext.getInstance().getRole().equalsIgnoreCase("reader")) {
      setupForReader();
    } else {
      grForReader.setVisible(false);
      btnRequestBorrow.setVisible(false);
    }
  }

  private void setupForReader() {
    btnAdd.setVisible(false);
    btnDelete.setVisible(false);
    btnUpdate.setVisible(false);
    txtAuthor.setVisible(false);
    txtCategory.setVisible(false);
    txtBookId.setVisible(false);
    txtBookName.setVisible(false);
    txtQuantity.setVisible(false);
    txtIsbn.setVisible(false);
    dpPublish.setVisible(false);
    cbAuthor.setVisible(false);
    cbCategory.setVisible(false);
    btnAddCategory.setVisible(false);
    btnAddAuthor.setVisible(false);
    btnSearchByApi.setVisible(false);

    lbBookId.setVisible(false);
    lbBookName.setVisible(false);
    lbCategory.setVisible(false);
    lbQuantity.setVisible(false);
    lbPublishDate.setVisible(false);
    lbAuthor.setVisible(false);
    lbIsbn.setVisible(false);

    grForReader.setVisible(true);
    btnRequestBorrow.setVisible(true);
    tbBooks.setMinSize(847, 400);
  }

  private void customDatePicker() {
    dpPublish.setDayCellFactory(
        picker ->
            new DateCell() {
              @Override
              public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(date.isAfter(LocalDate.now()));
              }
            });

    dpReturn.setDayCellFactory(
        picker ->
            new DateCell() {
              @Override
              public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(date.isBefore(LocalDate.now()));
              }
            });
  }

  private void initComboBox() {
    cbCategory.getItems().addAll(bookService.getAllCategoryName());
    bookService.getAllAuthors().forEach(author -> cbAuthor.getItems().add(author.getAuthorName()));
  }

  private void loadBooksOnTable() {
    colBookId.setCellValueFactory(new PropertyValueFactory<>("bookId"));
    colBookName.setCellValueFactory(new PropertyValueFactory<>("bookName"));
    colAuthorName.setCellValueFactory(new PropertyValueFactory<>("author"));
    colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
    colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    colPublishDate.setCellValueFactory(new PropertyValueFactory<>("publisher"));
    colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));

    tbBooks.setItems(bookService.getAllBook());
  }

  /**
   * Fills the text fields and other UI controls with the details of the selected book from
   * the table.
   *
   * @param mouseEvent the MouseEvent that triggers this method, typically a mouse click on
   *                   a table row
   */
  public void fillToTextField(MouseEvent mouseEvent) {
    Optional<Book> tblBook = Optional.ofNullable(tbBooks.getSelectionModel().getSelectedItem());

    tblBook.ifPresent(
        book -> {
          btnRequestBorrow.setDisable(false);

          txtBookId.setText(book.getBookId());
          txtBookName.setText(book.getBookName());
          cbCategory.setValue(book.getCategory());
          txtQuantity.setText(String.valueOf(book.getQuantity()));
          dpPublish.setValue(book.getPublisher());
          cbAuthor.setValue(book.getAuthor());
          txtIsbn.setText(book.getIsbn());
        });
  }

  /**
   * Handles the event of adding a new book.
   * This method is triggered when the 'Add' button is clicked. It performs the following steps:
   * 1. Collects data from the input fields (text fields, combo boxes, date picker).
   * 2. Validates the collected data.
   * 3. Constructs a new `Book` object.
   * 4. Saves the new `Book` object using the book service.
   * 5. Updates the table view with the latest books and clears the input fields.
   * In case of validation errors or exceptions during the save operation, appropriate alert
   * messages are displayed.
   *
   * @param actionEvent the `ActionEvent` that triggers this method, typically a click event
   *                    on the 'Add' button.
   */
  public void onClickAdd(ActionEvent actionEvent) {
    String bookId = txtBookId.getText();
    String bookName = txtBookName.getText();
    String category =
        txtCategory.getText().isBlank() ? cbCategory.getValue() : txtCategory.getText();
    String quantity = txtQuantity.getText();
    LocalDate publishDate = dpPublish.getValue();
    String isbn = txtIsbn.getText();
    String author = txtAuthor.getText().isBlank() ? cbAuthor.getValue() : txtAuthor.getText();
    // check null
    if (isNull(bookId, bookName, category, quantity, publishDate, author, isbn)) {
      AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", null, "Please fill all fields!");
      return;
    }

    // validate
    if (!isValid("INTEGER_NUMBER", quantity)) {
      AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", null, "Quantity must be an integer!");
      return;
    }

    if (!isValid("ISBN", isbn)) {
      AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", null, "Please enter a valid ISBN!");
      return;
    }

    Book book =
        Book.builder()
            .bookId(bookId)
            .bookName(bookName)
            .category(category)
            .quantity(Integer.parseInt(quantity))
            .publisher(publishDate)
            .author(author)
            .isbn(isbn)
            .build();

    try {
      bookService.saveBook(book);

      AlertUtil.showAlert(
          Alert.AlertType.INFORMATION, "Information", null, "Add new book successfully!");
      tbBooks.setItems(bookService.getAllBook());
      clear();

    } catch (Exception e) {
      AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", null, e.getMessage());
    }
  }

  /**
   * Handles the event of deleting a selected book.
   * This method performs the following steps:
   * 1. Checks if a book is selected in the table.
   * 2. If a book is selected, prompts the user for confirmation.
   * 3. If the user confirms, deletes the book using the book service.
   * 4. If no book is selected, shows an error alert prompting the user to select a book.
   * 5. Updates the table view with the latest list of books.
   *
   * @param actionEvent the `ActionEvent` that triggers this method, typically a click event
   *                    on the 'Delete' button.
   */
  public void onClickDelete(ActionEvent actionEvent) {
    Optional<Book> selectedBook =
        Optional.ofNullable(tbBooks.getSelectionModel().getSelectedItem());

    if (selectedBook.isPresent()
        && AlertUtil.showConfirmation(
            "Are you sure you want to delete?\nThis action cannot be undone!")) {
      bookService.deleteBook(selectedBook.get());
    } else if (selectedBook.isEmpty()) {
      AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", null, "Please select a book to delete!");
    }
    tbBooks.setItems(bookService.getAllBook());
  }

  /**
   * Handles the event of updating the selected book.
   * This method performs the following steps:
   * 1. Retrieves the selected book from the table.
   * 2. Collects data from the input fields (text fields, combo boxes, date picker).
   * 3. Validates the collected data to ensure that all fields are filled and the input formats
   * are correct.
   * 4. If validation passes, updates the book's details using the book service.
   * 5. Updates the table view with the latest list of books and clears the input fields.
   * 6. If validation fails or no book is selected, displays appropriate alert messages.
   *
   * @param actionEvent the ActionEvent that triggers this method, typically a click event
   *                    on the 'Update' button.
   */
  public void onClickUpdate(ActionEvent actionEvent) {
    Optional<Book> selectedBook =
        Optional.ofNullable(tbBooks.getSelectionModel().getSelectedItem());

    String bookId = txtBookId.getText();
    String bookName = txtBookName.getText();
    String category =
        txtCategory.getText().isBlank() ? cbCategory.getValue() : txtCategory.getText();
    String quantity = txtQuantity.getText();
    LocalDate publishDate = dpPublish.getValue();
    String author = cbAuthor.getValue();
    String isbn = txtIsbn.getText();

    // check null
    if (isNull(bookId, bookName, category, quantity, publishDate, author, isbn)) {
      AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", null, "Please fill all fields!");
      return;
    }

    // validate
    if (!isValid("INTEGER_NUMBER", quantity)) {
      AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", null, "Quantity must be an integer!");
      return;
    }

    if (!isValid("ISBN", isbn)) {
      AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", null, "Please enter a valid ISBN!");
      return;
    }

    if (selectedBook.isPresent()) {
      selectedBook.get().setBookName(bookName);
      selectedBook.get().setCategory(category);
      selectedBook.get().setQuantity(Integer.parseInt(quantity));
      selectedBook.get().setPublisher(publishDate);
      selectedBook.get().setIsbn(isbn);
      selectedBook.get().setAuthor(author);
      bookService.updateBook(selectedBook.get());
      tbBooks.setItems(bookService.getAllBook());
      AlertUtil.showAlert(
          Alert.AlertType.INFORMATION, "Success", null, "Update book successfully!");
      clear();
    } else {
      AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", null, "Please select a book to update!");
    }
  }

  /**
   * Handles the event of refreshing the book table.
   * This method performs the following steps:
   * 1. Clears the current input fields and selection.
   * 2. Reloads the book table with the latest data from the book service.
   * 3. Disables the "Request Borrow" button.
   *
   * @param actionEvent the ActionEvent that triggers this method, typically a click event
   *                    on the 'Refresh' button.
   */
  public void onClickRefresh(ActionEvent actionEvent) {
    clear();
    tbBooks.setItems(bookService.getAllBook());
    if (UserContext.getInstance().getRole().equalsIgnoreCase("reader")) {
      setupForReader();
    }
    btnRequestBorrow.setDisable(true);
  }

  /**
   * Handles the event of adding a new category.
   * This method toggles between showing the category text field and the category combo box
   * depending on the current state of `isAddingCategory`. It updates the UI elements
   * accordingly to either allow the user to input a new category or cancel the addition process.
   *
   * @param actionEvent the `ActionEvent` that triggers this method, typically a click event
   *                    on the 'Add Category' button.
   */
  public void onClickAddCategory(ActionEvent actionEvent) {
    if (isAddingCategory) {
      txtCategory.setVisible(false);
      cbCategory.setVisible(true);
      txtCategory.clear();
      btnAddCategory.setText("Add Category");
      cbCategory.getItems().clear();
      cbCategory.getItems().addAll(bookService.getAllCategoryName());
    } else {
      txtCategory.setVisible(true);
      cbCategory.setVisible(false);
      cbCategory.getSelectionModel().clearSelection();
      btnAddCategory.setText("Cancel");
    }
    isAddingCategory = !isAddingCategory;
  }

  /**
   * Handles the event of toggling between adding a new author and selecting an existing author.
   * This method changes the visibility of the author text field and author combo box,
   * as well as updates the button text and the state of `isAddingAuthor`.
   * When the user decides to add a new author:
   * - Shows the author text field for input.
   * - Hides the author combo box.
   * - Clears any selection in the author combo box.
   * - Changes the button text to "Cancel".
   * When the user cancels adding a new author:
   * - Hides the author text field.
   * - Shows the author combo box.
   * - Clears the text field input.
   * - Changes the button text to "Add Author".
   * - Reloads the combo box with the updated list of authors.
   *
   * @param actionEvent the ActionEvent that triggers this method, typically a click event on the
   *                    'Add Author' button.
   */
  public void onClickAddAuthor(ActionEvent actionEvent) {
    if (isAddingAuthor) {
      txtAuthor.setVisible(false);
      cbAuthor.setVisible(true);
      txtAuthor.clear();
      btnAddAuthor.setText("Add Author");
      cbAuthor.getItems().clear();
      bookService
          .getAllAuthors()
          .forEach(author -> cbAuthor.getItems().add(author.getAuthorName()));
    } else {
      txtAuthor.setVisible(true);
      cbAuthor.setVisible(false);
      cbAuthor.getSelectionModel().clearSelection();
      btnAddAuthor.setText("Cancel");
    }
    isAddingAuthor = !isAddingAuthor;
  }

  private boolean isNull(Object... objects) {
    for (Object obj : objects) {
      if (obj == null || obj.toString().isBlank()) {
        return true;
      }
    }
    return false;
  }

  private void clear() {
    txtBookId.setText(bookService.getBookId());
    txtBookName.clear();
    txtCategory.clear();
    txtQuantity.clear();
    txtIsbn.clear();
    dpPublish.setValue(null);
    cbAuthor.setValue(null);
    cbCategory.setValue(null);

    txtAuthor.clear();
    txtCategory.clear();

    btnAddAuthor.setText("Add Author");
    btnAddCategory.setText("Add Category");

    txtCategory.setVisible(false);
    txtAuthor.setVisible(false);

    cbCategory.setVisible(true);
    cbAuthor.setVisible(true);

    isAddingAuthor = false;
    isAddingCategory = false;

    tbBooks.getSelectionModel().clearSelection();
  }

  /**
   * Handles the search operation in the book management application.
   * This method filters the list of books based on the user's input in the search text field.
   * If the search field is empty, it displays all books. Otherwise, it filters the books
   * by matching the search keyword with the book's ID, name, author, category, or ISBN.
   *
   * @param keyEvent the KeyEvent that triggers this method, typically a key press in the search
   *                 text field
   */
  public void onSearch(KeyEvent keyEvent) {
    String keyword = txtSearch.getText();
    if (keyword.isEmpty()) {
      tbBooks.setItems(bookService.getAllBook());
    } else {
      tbBooks.setItems(
          bookService
              .getAllBook()
              .filtered(
                  book -> {
                    return (book.getBookId() != null
                            && book.getBookId().toLowerCase().contains(keyword.toLowerCase()))
                        || (book.getBookName() != null
                            && book.getBookName().toLowerCase().contains(keyword.toLowerCase()))
                        || (book.getAuthor() != null
                            && book.getAuthor().toLowerCase().contains(keyword.toLowerCase()))
                        || (book.getCategory() != null
                            && book.getCategory().toLowerCase().contains(keyword.toLowerCase()))
                        || (book.getIsbn() != null
                            && book.getIsbn().toLowerCase().contains(keyword.toLowerCase()));
                  }));
    }
  }

  /**
   * Handles the event of requesting to borrow a book.
   * This method performs the following steps:
   * 1. Retrieves the selected book from the table.
   * 2. Checks if a book is selected; if not, returns immediately.
   * 3. Verifies if the selected book is out of stock; if yes, shows an error alert and returns.
   * 4. Ensures that a return date is selected; if not, shows an error alert and returns.
   * 5. Attempts to request a borrow using the borrow service and shows an appropriate success or
   * error alert.
   *
   * @param actionEvent the ActionEvent that triggers this method, typically a click event on the
   *                    'Request Borrow' button.
   */
  public void onClickRequest(ActionEvent actionEvent) {
    Book selectedBook = tbBooks.getSelectionModel().getSelectedItem();

    if (selectedBook == null) {
      return;
    }

    if (selectedBook.getQuantity() == 0) {
      AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", null, "This book is out of stock!");
      return;
    }

    LocalDate returnDate = dpReturn.getValue();

    if (returnDate == null) {
      AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", null, "Please choose a return date!");
      return;
    }

    try {
      borrowService.requestBorrow(selectedBook.getBookId(), returnDate);
    } catch (Exception e) {
      AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", null, e.getMessage());
      return;
    }
    AlertUtil.showAlert(
        Alert.AlertType.INFORMATION, "Success", null, "Request borrow successfully!");
  }

  @FXML
  void onClickGetInfo(ActionEvent event) {
    String isbn = txtIsbn.getText();

    Task<String[]> loadBookTask =
        new Task<>() {
          @Override
          protected String[] call() throws Exception {
            updateProgress(0.2, 1); // Cập nhật tiến trình
            String rawBookInfo = GoogleBooksAPI.getBookInfoByIsbn(isbn);

            // Tách dữ liệu thành mảng
            String[] bookData = rawBookInfo.split("\n");
            // Giả lập quá trình tải thông tin
            for (int i = 1; i <= 5; i++) {
              Thread.sleep(200); // Tạm dừng để mô phỏng tiến trình tải
              updateProgress(i * 0.2, 1); // Cập nhật tiến trình
            }
            return bookData;
          }
        };

    // Khi hoàn thành, hiển thị popup với thông tin sách
    loadBookTask.setOnSucceeded(
        successEvent -> {
          String[] bookDetails = loadBookTask.getValue();
          String[] bookDetailsCopy = Arrays.copyOf(bookDetails, bookDetails.length);
          // Kiểm tra nếu không có thông tin sách
          if (bookDetails == null
              || bookDetails.length == 0
              || bookDetails[0].startsWith("No book found")) {
            // Hiển thị popup cảnh báo không tìm thấy thông tin sách
            showAlertNoBookFound();
          } else {
            try {
              showBookDetailsPopup(bookDetailsCopy); // Truyền bản sao vào popup

            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        });

    // Khi thất bại, hiển thị lỗi
    loadBookTask.setOnFailed(
        failureEvent -> {
          System.out.println("Error loading book information.");
          progressBar.setProgress(0);
        });

    // Gắn task vào thanh tiến trình
    progressBar.progressProperty().bind(loadBookTask.progressProperty());
    // Chạy task trong một luồng mới
    new Thread(loadBookTask).start();
  }

  private void showBookDetailsPopup(String[] bookDetails) throws IOException {

    String thumbnailUrl = getThumbnailUrl(bookDetails[7]);

    // Tiến hành hiển thị popup
    FXMLLoader loader =
        new FXMLLoader(getClass().getResource("/com/example/library/BookDetails.fxml"));
    Pane root = loader.load();

    BookDetailsController controller = loader.getController();

    // Gửi dữ liệu sang controller
    controller.setBookData(
        bookDetails[0].split(":")[1].trim(), // title
        bookDetails[1].split(":")[1].trim(), // authors
        bookDetails[2].split(":")[1].trim(), // language
        bookDetails[3].split(":")[1].trim(), // categories
        bookDetails[4].split(":")[1].trim(), // publishedDate
        bookDetails[5].split(":")[1].trim(), // pageCount
        bookDetails[6].split(":")[1].trim(), // description
        thumbnailUrl // thumbnail
    );

    // Tạo một Stage mới cho popup
    Stage popupStage = new Stage();
    popupStage.initModality(Modality.APPLICATION_MODAL);
    popupStage.setTitle("Thông tin sách");
    popupStage.setScene(new Scene(root));
    popupStage.showAndWait();
  }

  private String getThumbnailUrl(String detail) {
    if (detail != null && detail.startsWith("Thumbnail:")) {
      // Loại bỏ phần "Thumbnail:" và trả về URL
      return detail.substring("Thumbnail:".length()).trim();
    }
    return "No Image"; // Trả về giá trị mặc định nếu không có URL
  }

  private void showAlertNoBookFound() {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle("Không tìm thấy sách");
    alert.setHeaderText(null);
    alert.setContentText("Không tìm thấy thông tin cho ISBN này trên Google Books API.");
    alert.showAndWait(); // Hiển thị popup và chờ người dùng đóng
  }

  /**
   * Handles the event of searching for a book by its ISBN.
   * This method is triggered by an action event such as a button click.
   * It opens a new window for adding book information via an external API.
   *
   * @param event the ActionEvent that triggers this method, typically a click event on a
   *              'Search by ISBN' button.
   * @throws IOException if an input or output exception occurs while setting the root of window.
   */
  public void onSearchByIsbn(ActionEvent event) throws IOException {
    App.setRootPop("AddBookApiFrm", "Add Book By API", false);
  }


  public void onClickQrBook(ActionEvent event) {
    String isbn = txtIsbn.getText();
    String linkBook = GoogleBooksAPI.getLinkBookByIsbn(isbn);

    try {
      // Tiến hành hiển thị popup
      FXMLLoader loader =
              new FXMLLoader(getClass().getResource("/com/example/library/QrBook.fxml"));
      Pane root = loader.load();
      QrBookController controller = loader.getController();

      // Gửi dữ liệu sang controller
      controller.setBookData(linkBook);

      // Tạo một Stage mới cho popup
      Stage popupStage = new Stage();
      popupStage.initModality(Modality.APPLICATION_MODAL);
      popupStage.setTitle("Qr Book");
      popupStage.setScene(new Scene(root));
      popupStage.showAndWait();
    } catch (IOException e) {
      System.err.println("Error loading QrBook.fxml: " + e.getMessage());
      e.printStackTrace();
    }
  }


}
