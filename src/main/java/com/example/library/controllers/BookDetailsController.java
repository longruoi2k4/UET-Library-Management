package com.example.library.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * The BookDetailsController is responsible for managing and displaying the details of a book
 * in the user interface.
 */
public class BookDetailsController {
  @FXML private ImageView bookCover;
  @FXML private Label bookTitle;
  @FXML private Label bookAuthors;
  @FXML private Label bookLanguage;
  @FXML private Label bookCategories;
  @FXML private Label bookPublishedDate;
  @FXML private Label bookPageCount;
  @FXML private TextArea bookDescription;

  /**
   * Sets the book details to the UI components.
   *
   * @param title The title of the book.
   * @param authors The authors of the book.
   * @param language The language of the book.
   * @param categories The categories/genres of the book.
   * @param publishedDate The published date of the book.
   * @param pageCount The page count of the book.
   * @param description The description of the book.
   * @param thumbnailUrl The URL of the book's cover image.
   */
  public void setBookData(
      String title,
      String authors,
      String language,
      String categories,
      String publishedDate,
      String pageCount,
      String description,
      String thumbnailUrl) {
    bookTitle.setText(
        "Tiêu đề: " + (title != null && !title.isEmpty() ? title : "Không có thông tin"));
    bookAuthors.setText(
        "Tác giả: " + (authors != null && !authors.isEmpty() ? authors : "Không có thông tin"));
    bookLanguage.setText(
        "Ngôn ngữ: " + (language != null && !language.isEmpty() ? language : "Không có thông tin"));
    bookCategories.setText(
        "Thể loại: "
            + (categories != null && !categories.isEmpty() ? categories : "Không có thông tin"));
    bookPublishedDate.setText(
        "Ngày xuất bản: "
            + (publishedDate != null && !publishedDate.isEmpty()
                ? publishedDate
                : "Không có thông tin"));
    bookPageCount.setText(
        "Số trang: "
            + (pageCount != null && !pageCount.isEmpty() ? pageCount : "Không có thông tin"));
    bookDescription.setText(
        description != null && !description.isEmpty() ? description : "Không có mô tả.");

    // Set the book cover image
    if (thumbnailUrl != null && !thumbnailUrl.isEmpty()) {
      try {
        System.out.println("URL: " + thumbnailUrl);
        bookCover.setImage(new Image(thumbnailUrl, true));
      } catch (Exception e) {
        System.out.println("Error loading image from URL: " + thumbnailUrl);
        e.printStackTrace(); // In chi tiết lỗi
        bookCover.setImage(
            new Image(getClass().getResourceAsStream("/img/No-image-available.jpg")));
      }

    } else {
      bookCover.setImage(new Image(getClass().getResourceAsStream("/img/No-image-available.jpg")));
    }
  }
}
