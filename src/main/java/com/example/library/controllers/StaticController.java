package com.example.library.controllers;

import com.example.library.services.IStaticService;
import com.example.library.services.impl.StaticServiceImpl;
import com.example.library.utils.AlertUtil;
import com.example.library.utils.GoogleBooksAPI;
import com.example.library.utils.SettingUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * The StaticController class handles the initialization and interaction logic
 * for the static view in the application.
 * It implements the Initializable interface.
 */
public class StaticController implements Initializable {
  @FXML private Text txtNumberReader;
  @FXML private Text txtTotalBorrow;
  @FXML private Text txtTotalQuantityBook;
  @FXML private Text txtTotalLate;
  @FXML private PieChart pieLate;
  @FXML private ListView<HBox> top3Book;
  @FXML private Text txtTop3;
  private final IStaticService staticService;
  private final SettingUtils settingUtils = SettingUtils.getInstance();

  /**
   * Default constructor for the StaticController class.
   * This constructor initializes an instance of StaticServiceImpl and assigns it to
   * the staticService field.
   */
  public StaticController() {
    staticService = new StaticServiceImpl();
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    txtNumberReader.setText(String.valueOf(staticService.getTotalReader()));
    txtTotalBorrow.setText(String.valueOf(staticService.getTotalBorrow()));
    txtTotalQuantityBook.setText(String.valueOf(staticService.getTotalBook()));
    txtTotalLate.setText(String.valueOf(staticService.getTotalLate()));
    showTop3();
    drawChart();
  }

  private void showTop3() {
    try {
      // Gọi API để tìm kiếm tài liệu
      List<String> topIsbnList = staticService.getTop3MostBorrowedBooks();

      // Clear ListView và tạo ObservableList cho các mục tìm kiếm
      top3Book.getItems().clear();
      ObservableList<HBox> suggestions = FXCollections.observableArrayList();

      // Hiển thị thông báo "Đang tải" lên UI
      txtTop3.setText("Wait to Loading...");
      // Tạo Task để gọi API bất đồng bộ
      Task<Void> task = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
          for (String isbn : topIsbnList) {
            // Lấy thông tin sách từ Google Books API
            String bookInfo = GoogleBooksAPI.getBookInfoByIsbn(isbn);
            // Tạo box hiển thị sách
            HBox bookInfoBox = createBookInfoBox(bookInfo);
            // Thêm vào danh sách kết quả
            suggestions.add(bookInfoBox);
          }
          return null;
        }
      };
      // Cập nhật UI khi Task hoàn thành
      task.setOnSucceeded(e -> {
        if (suggestions.isEmpty()) {
          top3Book.setVisible(false);
        } else {
          top3Book.setItems(suggestions);
          top3Book.setVisible(true);
        }

        // Sau khi tải xong, xóa thông báo "Đang tải"
        txtTop3.setText("Top 3 Most Borrowed Books");
      });
      // Cập nhật thông báo nếu Task thất bại
      task.setOnFailed(e -> {
        e.getSource().getException().printStackTrace();
        txtTop3.setText("Load Failed");
      });
      // Chạy Task trong một luồng riêng biệt
      new Thread(task).start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }



  private HBox createBookInfoBox(String book) {
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

  private String getThumbnailUrl(String detail) {
    if (detail != null && detail.startsWith("Thumbnail:")) {
      // Loại bỏ phần "Thumbnail:" và trả về URL
      return detail.substring("Thumbnail:".length()).trim();
    }
    return "No Image"; // Trả về giá trị mặc định nếu không có URL
  }



  private void drawChart() {
    pieLate.getData().clear();
    pieLate.getData().add(new PieChart.Data("Total Borrow", staticService.getTotalBorrow()));
    pieLate.getData().add(new PieChart.Data("Total Return Late", staticService.getTotalLate()));
  }

  /**
   * Handles the click event on the "Return On Time" button.
   * This method toggles the highlight return option based on the current state.
   * A confirmation dialog is presented to the user before changing the setting.
   *
   * @param mouseEvent The mouse event triggered by clicking the "Return On Time" button.
   */
  public void onClickReturnOnTime(MouseEvent mouseEvent) {
    if (settingUtils.isHighlightReturn()) {
      if (AlertUtil.showConfirmation("Bạn có muốn tắt tuỳ chọn này không?")) {
        settingUtils.setHighlightReturn(false);
      }
    } else if (AlertUtil.showConfirmation("Bạn có muốn bật tuỳ chọn này không?")) {
      settingUtils.setHighlightReturn(true);
    }
  }

  /**
   * Handles the click event on the "Return Late" button.
   * This method toggles the highlight late return option based on the current state.
   * A confirmation dialog is presented to the user before changing the setting.
   *
   * @param mouseEvent The mouse event triggered by clicking the "Return Late" button.
   */
  public void onClickReturnLate(MouseEvent mouseEvent) {
    if (settingUtils.isHighlightLate()) {
      if (AlertUtil.showConfirmation("Bạn có muốn tắt tuỳ chọn này không?")) {
        settingUtils.setHighlightLate(false);
      }
    } else if (AlertUtil.showConfirmation("Bạn có muốn bật tuỳ chọn này không?")) {
      settingUtils.setHighlightLate(true);
    }
  }
}
