package com.example.library.models;

import java.time.LocalDate;
import lombok.*;

/**
 * The Book class represents a book in the library system.
 * It contains information related to the book such as its unique identifier,
 * name, author, category, ISBN, quantity, and the date it was published.
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@ToString
public class Book {
  private String bookId;
  private String bookName;
  private String author;
  private String category;
  private String isbn;
  private int quantity;
  private LocalDate publisher;
}
