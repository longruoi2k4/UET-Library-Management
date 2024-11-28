package com.example.library.controllers;

import com.example.library.controllers.BookCardController;
import com.example.library.utils.GoogleBooksAPI;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.util.Duration;


/**
 * The AddBookApiController class is responsible for managing the add book functionality
 * in the application. It handles user input in the search field to query book data
 * using the Google Books API and updates the UI with search suggestions.
 */
public class AddBookApiController implements Initializable {

  @FXML private TextField searchField;
  @FXML private ListView<HBox> suggestList;

  private Timeline searchTimeline;
  private final ExecutorService executorService = Executors.newSingleThreadExecutor();

  @Override
  public void initialize(URL url, ResourceBundle resources) {
    suggestList.setVisible(false);
    searchFieldListener();
  }

  private void searchFieldListener() {
    searchField
        .textProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue.isEmpty()) {
                // Khi TextField trống, ListView tắt
                suggestList.setVisible(false);
              } else {
                // Nếu có sự thay đổi trong nội dung, hủy timeline cũ
                if (searchTimeline != null) {
                  searchTimeline.stop();
                }
                // Tạo một timeline mới với thời gian trì hoãn
                searchTimeline =
                    new Timeline(new KeyFrame(Duration.seconds(1), event -> handleSearch()));
                searchTimeline.playFromStart();
              }
            });
  }

  private void handleSearch() {
    String query = searchField.getText().trim();
    if (query.isEmpty()) {
      suggestList.setItems(FXCollections.observableArrayList());
      suggestList.setVisible(false);
      return;
    }
    // Chạy tìm kiếm trong một luồng nền
    executorService.submit(() -> searchBooks(query));
  }

  private void searchBooks(String query) {
    try {
      // Gọi API để tìm kiếm tài liệu
      List<String> books =
          GoogleBooksAPI.searchBooksByKeyword(
              query); // Tạo danh sách các item HBox từ kết quả tìm kiếm
      ObservableList<HBox> suggestions = FXCollections.observableArrayList();
      for (String book : books) {
        suggestions.add(createItem(book));
      }

      // Cập nhật giao diện người dùng trong JavaFX thread
      Platform.runLater(
          () -> {
            if (suggestions.isEmpty()) {
              suggestList.setVisible(false);
            } else {
              suggestList.setItems(suggestions);
              suggestList.setVisible(true);
            }
          });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private String getThumbnailUrl(String detail) {
    if (detail != null && detail.startsWith("Thumbnail:")) {
      // Loại bỏ phần "Thumbnail:" và trả về URL
      return detail.substring("Thumbnail:".length()).trim();
    }
    return "No Image"; // Trả về giá trị mặc định nếu không có URL
  }

  private HBox createItem(String book) {
    String[] bookData = book.split("\n");
    String thumbnailUrl = getThumbnailUrl(bookData[7]);
    try {
      FXMLLoader loader =
          new FXMLLoader(getClass().getResource("/com/example/library/BookCard.fxml"));
      HBox node = loader.load();
      BookCardController bookCardController = loader.getController();
      bookCardController.setBookData(
          bookData[0].split(":")[1].trim(),
          bookData[1].split(":")[1].trim(),
          bookData[2].split(":")[1].trim(),
          bookData[3].split(":")[1].trim(),
          bookData[4].split(":")[1].trim(),
          thumbnailUrl,
          bookData[8].split(":")[1].trim());
      return node;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
}
