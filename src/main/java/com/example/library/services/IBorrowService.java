package com.example.library.services;

import com.example.library.models.Borrow;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import javafx.collections.ObservableList;

/**
 * Interface representing borrow-related services in a library system.
 */
public interface IBorrowService {

  /**
   * Retrieves a list of Borrow records associated with the provided reader ID.
   *
   * @param readerId The unique identifier of the reader whose borrow records are to be retrieved.
   * @return An ObservableList containing Borrow objects associated with the provided reader ID.
   */
  ObservableList<Borrow> getBorrowByReaderId(String readerId);

  /**
   * Retrieves a list of all book borrowing records in the library system.
   *
   * @return An ObservableList containing all Borrow objects representing the borrowed books in the
   *         library system.
   */
  ObservableList<Borrow> getAllBookBorrowed();

  /**
   * Processes the return of a borrowed book.
   *
   * @param borrow The Borrow object containing information about the borrowing transaction
   *               that is to be returned.
   * @throws Exception If an error occurs while processing the book return.
   */
  void returnBook(Borrow borrow) throws Exception;

  /**
   * Executes the borrowing of a book based on the provided Borrow object.
   *
   * @param borrow The Borrow object containing necessary details for executing the borrow operation.
   */
  void borrowBook(Borrow borrow);

  /**
   * Calculates the total number of books currently borrowed by a specific reader.
   *
   * @param readerId The unique identifier of the reader for whom the borrow count is to be calculated.
   * @return The total number of books borrowed by the reader.
   */
  int getTotalBorrowByReaderId(String readerId);

  /**
   * Checks if a reader is late in returning borrowed books.
   *
   * @param readerId The unique identifier of the reader to be checked.
   * @return true if the reader has overdue books, false otherwise.
   */
  boolean isReaderLate(String readerId);

  /**
   * Requests to borrow a book identified by its book ID and specifies the return date.
   *
   * @param bookId The unique identifier of the book to be borrowed.
   * @param returnDate The date by which the borrowed book should be returned.
   * @throws Exception If an error occurs during the borrow request process.
   */
  void requestBorrow(String bookId, LocalDate returnDate) throws Exception;

  /**
   * Retrieves all borrow requests associated with the provided reader ID.
   *
   * @param readerId The unique identifier of the reader whose borrow requests are to be retrieved.
   * @return An ObservableList containing Borrow objects that represent the borrow requests for
   *          the specified reader.
   */
  ObservableList<Borrow> getAllRequestByReaderId(String readerId);

  /**
   * Approves the list of borrow requests identified by their unique borrow IDs.
   *
   * @param borrowId A list of strings representing the unique identifiers of the borrow requests to be approved.
   */
  void approveRequest(List<String> borrowId);

  /**
   * Retrieves a list of all borrow requests in the library system.
   *
   * @return An ObservableList*/
  ObservableList<Borrow> getAllRequestBorrow();

  /**
   * Deletes the borrow requests identified by their unique borrow IDs.
   *
   * @param borrowId A list of strings representing the unique identifiers of the borrow requests
   *                 to be deleted.
   */
  void deleteRequest(List<String> borrowId);

  /**
   * Retrieves a list of email addresses associated with the provided list of borrow IDs.
   *
   * @param borrowIds A list of strings representing the unique identifiers of the borrow records.
   * @return A list of email addresses corresponding to the specified borrow IDs.
   */
  List<String> getAllEmailByBorrowIds(List<String> borrowIds);

  /**
   * Retrieves a map of email addresses and their corresponding messages associated with the provided list of borrow IDs.
   *
   * @param borrowIds A list of strings representing the unique identifiers of the borrow records.
   * @return A map where the keys are email addresses and the values are messages corresponding to the specified borrow IDs.
   */
  Map<String, String> getAllEmailWithMessagesByBorrowIds(List<String> borrowIds);

  /**
   * Declines a list of borrow requests identified by their unique borrow IDs.
   *
   * @param borrowId A list of strings representing the unique identifiers of the borrow requests to be declined.
   */
  void declineRequest(List<String> borrowId);

  /**
   * Updates the borrow date for the specified borrow records.
   *
   * @param borrowId A list of strings representing the unique identifiers of the borrow records to be updated.
   */
  void updateBorrowDate(List<String> borrowId);
}
