package com.example.library.repositories;

import com.example.library.models.Borrow;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import javafx.collections.ObservableList;

/**
 * The IBorrowRepository interface defines the methods required for managing
 * borrowing transactions within a repository. It includes functionalities
 * for retrieving, saving, updating, and deleting borrowing records as well
 * as processing borrowing requests.
 */
public interface IBorrowRepository {

  /**
   * Retrieves a list of borrowing transactions associated with the specified reader ID.
   *
   * @param readerId the unique identifier of the reader whose borrowing transactions are
   *                 to be retrieved
   * @return an observable list of Borrow objects representing the borrowing transactions
   *         associated with the specified reader ID
   */
  ObservableList<Borrow> getBorrowByReaderId(String readerId);

  /**
   * Retrieves a list of all books currently borrowed from the repository.
   *
   * @return an observable list of Borrow objects representing all borrowed books.
   */
  ObservableList<Borrow> getAllBookBorrowed();

  /**
   * Updates the repository to reflect that a borrowed book has been returned.
   *
   * @param borrow the borrowing transaction information including details
   *               of the book being returned.
   */
  void returnBook(Borrow borrow);

  /**
   * Saves the provided Borrow object to the repository.
   *
   * @param borrow the Borrow object to be saved
   */
  void save(Borrow borrow);

  /**
   * Retrieves the total number of borrow transactions in the repository.
   *
   * @return the total number of borrow transactions.
   */
  int getTotalBorrow();

  /**
   * Retrieves the total number of late borrow transactions in the repository.
   *
   * @return the total number of late borrow transactions
   */
  int getTotalLate();

  /**
   * Retrieves the total number of returned borrow transactions in the repository.
   *
   * @return the total number of returned borrow transactions.
   */
  int getTotalReturn();

  /**
   * Retrieves the total number of borrowing transactions associated with a specific reader ID.
   *
   * @param readerId the unique identifier of the reader whose total borrowing transactions
   *                 are to be retrieved
   * @return the total number of borrow transactions for the specified reader ID
   */
  int getTotalBorrowByReaderId(String readerId);

  /**
   * Checks if the reader with the specified ID has any late borrow transactions.
   *
   * @param readerId the unique identifier of the reader whose late status is to be checked
   * @return true if the reader has late borrow transactions, false otherwise
   */
  boolean isReaderLate(String readerId);

  /**
   * Submits a request to borrow a book identified by the given book ID and specifies
   * the intended return date.
   *
   * @param bookId the unique identifier of the book to be borrowed
   */
  void requestBorrow(String bookId, LocalDate returnDate);

  /**
   * Approves a list of borrowing requests identified by their unique borrow IDs.
   *
   * @param borrowId a list of unique identifiers for the borrowing requests to be approved
   */
  void approveRequest(List<String> borrowId);

  /**
   * Deletes a list of borrowing requests identified by their unique borrow IDs.
   *
   * @param borrowId a list of unique identifiers for the borrowing requests to be deleted
   */
  void deleteRequest(List<String> borrowId);

  /**
   * Retrieves a list of email addresses associated with the given borrow IDs.
   *
   * @param borrowIds the unique identifiers of the borrow transactions whose emails are
   *                  to be retrieved
   * @return a list of email addresses corresponding to the provided borrow IDs
   */
  List<String> getAllEmailByBorrowIds(List<String> borrowIds);

  /**
   * Retrieves a list of all book IDs associated with the specified borrow IDs.
   *
   * @param borrowId a list of unique identifiers for the borrowing transactions whose
   *                 associated book IDs are to be retrieved
   * @return a list of book IDs associated with the given borrow IDs
   */
  List<String> getAllBookIdByBorrowId(List<String> borrowId);

  /**
   * Checks if there is already a request to borrow a specific book
   * by a specific reader.
   *
   * @param readerId the unique identifier of the reader
   * @param bookId the unique identifier of the book
   * @return true if there is already a request for the book by the reader, false otherwise
   */
  boolean isAlreadyRequest(String readerId, String bookId);

  /**
   * Retrieves a map of email addresses associated with their corresponding messages
   * for the provided list of borrow transaction IDs.
   *
   * @param borrowIds the unique identifiers of the borrow transactions whose emails and
   *                  messages are to be retrieved
   * @return a map where the key is the email address and the value is the message associated
   *         with the borrow transaction
   */
  Map<String, String> getAllEmailWithMessagesByBorrowIds(List<String> borrowIds);

  /**
   * Declines a list of borrowing requests identified by their unique borrow IDs.
   *
   * @param borrowId a list of unique identifiers for the borrowing requests to be declined
   */
  void declineRequest(List<String> borrowId);

  /**
   * Updates the borrow date for a list of borrowing transactions identified
   * by their unique borrow IDs.
   *
   * @param borrowId a list of unique identifiers for the borrowing transactions
   *                 whose dates are to be updated
   */
  void updateBorrowDate(List<String> borrowId);

  List<String> getTop3MostBorrowedBooks();
}
