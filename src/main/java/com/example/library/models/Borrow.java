package com.example.library.models;

import java.time.LocalDate;
import lombok.*;

/**
 * The Borrow class represents a borrowing transaction in the library system.
 * It contains details about the borrow such as IDs, names, dates, and status of the borrowed item.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Borrow {
  private String borrowId;
  private String readerId;
  private String bookName;
  private String readerName;
  private LocalDate borrowDate;
  private LocalDate returnDate;
  private String dueDate;
  private String status;
  private String bookId;
}
