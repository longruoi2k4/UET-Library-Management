package com.example.library.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Controller class for managing the visual representation of a book card.
 * This class interacts with the UI components to display book details and handles events
 * related to book cards.
 */
public class BookCardController {
  @FXML private ImageView bookCover;
  @FXML private Label bookTitle;
  @FXML private Label bookAuthors;
  @FXML private Label bookLanguage;
  @FXML private Label bookCategories;

  private String bookNameCard;
  private String categoryCard;
  private String publishDateCard;
  private String isbnCard;
  private String authorCard;

  /** Sets the book details to the UI components. */
  public void setBookData(
      String title,
      String authors,
      String language,
      String categories,
      String publishedDate,
      String thumbnailUrl,
      String isbn) {
    bookNameCard = title;
    authorCard = authors;
    categoryCard = categories;
    publishDateCard = publishedDate;
    isbnCard = isbn;

    bookTitle.setText("Title: " + (title != null && !title.isEmpty() ? title : "No Information"));
    bookAuthors.setText(
        "Authors: " + (authors != null && !authors.isEmpty() ? authors : "No Information"));
    bookLanguage.setText(
        "Language: " + (language != null && !language.isEmpty() ? language : "No Information"));
    bookCategories.setText(
        "Categories: "
            + (categories != null && !categories.isEmpty() ? categories : "No Information"));
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

  @FXML
  void onAddBookCard(ActionEvent event) {
    try {
      FXMLLoader loader =
          new FXMLLoader(getClass().getResource("/com/example/library/AddBookCard.fxml"));
      Pane root = loader.load();

      AddBookCardController controller = loader.getController();
      // Gửi dữ liệu sang controller
      controller.setBookData(bookNameCard, authorCard, categoryCard, publishDateCard, isbnCard);

      // Tạo một Stage mới cho popup
      Stage popupStage = new Stage();
      popupStage.initModality(Modality.APPLICATION_MODAL);
      popupStage.setTitle("Add Book Card");
      popupStage.setScene(new Scene(root));
      popupStage.showAndWait();
    } catch (IOException e) {
      System.out.println("Error loading FXML file: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
