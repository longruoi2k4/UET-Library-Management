package com.example.library.controllers;

import com.example.library.models.Book;
import com.example.library.services.IBookService;
import com.example.library.services.impl.BookServiceImpl;
import com.example.library.utils.AlertUtil;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import static com.example.library.common.Regex.isValid;

/**
 * Controller class responsible for handling the addition of new book cards.
 * Implements the Initializable interface for JavaFX initializations.
 */
public class AddBookCardController implements Initializable {

  @FXML private TextField txtQuantity;
  @FXML private TextField txtBookName;
  @FXML private TextField txtCategory;
  @FXML private TextField txtIsbn;
  @FXML private TextField txtAuthor;
  @FXML private DatePicker dpPublish;

  private String bookId;

  private final IBookService bookService;

  /**
   * Controller class responsible for handling the addition of new book cards.
   * This class utilizes the BookServiceImpl to manage book-related operations.
   */
  public AddBookCardController() {
    this.bookService = new BookServiceImpl(); // dependency injection
  }

  /**
   * Sets the data for the book including its name, author, category, published date, and ISBN.
   *
   * @param bookNameCard the name of the book
   * @param authorCard the author of the book
   * @param categoryCard the category of the book
   * @param publishedDateCard the published date of the book in the format of "yyyy", "yyyy-MM"
   *                          or "yyyy-MM-dd"
   * @param isbnCard the ISBN of the book
   */
  public void setBookData(
      String bookNameCard,
      String authorCard,
      String categoryCard,
      String publishedDateCard,
      String isbnCard) {

    txtBookName.setText(bookNameCard);
    txtCategory.setText(categoryCard);
    txtIsbn.setText(isbnCard);

    int commaIndex = authorCard.indexOf(',');
    // Nếu có dấu phẩy, lấy phần văn bản trước dấu phẩy
    if (commaIndex != -1) {
      String result = authorCard.substring(0, commaIndex);
      txtAuthor.setText(result);
    } else {
      txtAuthor.setText(authorCard);
    }

    if (publishedDateCard != null && !publishedDateCard.isEmpty()) {
      try {
        // Kiểm tra xem chuỗi có đầy đủ định dạng năm-tháng-ngày không
        if (publishedDateCard.length() == 4) {
          // Nếu chỉ có năm, không làm gì, giữ giá trị null
          dpPublish.setValue(
              null); // Hoặc bạn có thể bỏ qua nếu không muốn hiển thị giá trị mặc định
        } else if (publishedDateCard.length() == 7) {
          // Nếu chỉ có năm-tháng (Ví dụ: "2009-05"), không làm gì
          dpPublish.setValue(null); // Hoặc bạn có thể xử lý theo cách khác, nếu muốn
        } else if (publishedDateCard.length() == 10) {
          // Nếu có đủ ngày-tháng-năm (Ví dụ: "2009-05-01"), chuyển đổi thành LocalDate
          dpPublish.setValue(LocalDate.parse(publishedDateCard));
        } else {
          dpPublish.setValue(null); // Nếu không có định dạng hợp lệ, đặt là null
        }
      } catch (DateTimeParseException e) {
        System.out.println("Error parsing date: " + e.getMessage());
        dpPublish.setValue(null); // Nếu không thể phân tích, đặt giá trị null
      }
    } else {
      dpPublish.setValue(null); // Nếu không có ngày, đặt là null
    }
  }

  /**
   * Initializes the controller class. This method is automatically called after
   * the FXML file has been loaded.
   * It retrieves the book ID using the book service.
   *
   * @param url the location used to resolve relative paths for the root object, or null if
   *            the location is not known
   * @param resourceBundle the resources used to localize the root object, or null if
   *                       the root object was not localized
   */
  public void initialize(URL url, ResourceBundle resourceBundle) {
    bookId = bookService.getBookId();
  }

  @FXML
  void onAddBookCard(ActionEvent actionEvent) {
    String quantity = txtQuantity.getText();
    String bookName = txtBookName.getText();
    String category = txtCategory.getText();
    String author = txtAuthor.getText();
    String isbn = txtIsbn.getText();
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
            .publisher(dpPublish.getValue())
            .author(author)
            .isbn(isbn)
            .build();
    if (book.getIsbn() == null) {
      book.setIsbn("N/A");
    }
    if (book.getPublisher() == null) {
      AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", null, "Please fill the publisher date!");
      return;
    }
    try {
      bookService.saveBook(book);
      AlertUtil.showAlert(
          Alert.AlertType.INFORMATION, "Information", null, "Add new book successfully!");
    } catch (Exception e) {
      AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", null, e.getMessage());
    }
  }
}
