package com.example.library.services;

import com.example.library.models.Author;
import com.example.library.models.Book;
import javafx.collections.ObservableList;

/**
 * Interface representing book-related services.
 */
public interface IBookService {

  /**
   * Retrieves a list of all books available in the library.
   *
   * @return an ObservableList containing all Book objects.
   */
  ObservableList<Book> getAllBook();

  /**
   * Retrieves a list of all book category names available in the library.
   *
   * @return an ObservableList containing all category names as strings.
   */
  ObservableList<String> getAllCategoryName();

  /**
   * Retrieves a list of all book IDs available in the library.
   *
   * @return an ObservableList containing all book IDs as strings.
   */
  ObservableList<String> getAllBookId();

  /**
   * Retrieves a list of all authors available in the library.
   *
   * @return an ObservableList containing all Author objects.
   */
  ObservableList<Author> getAllAuthors();

  /**
   * Retrieves the name of a book given its unique identifier.
   *
   * @param bookId The unique identifier of the book for which the name is to be retrieved.
   * @return The name of the book associated with the provided book ID.
   */
  String getBookNameById(String bookId);

  /**
   * Persists a given Book object to the storage.
   *
   * @param book The Book object to be saved. It*/
  void saveBook(Book book) throws Exception;

  /**
   * Deletes a specified book from the library system.
   *
   * @param book The Book object to be deleted.
   */
  void deleteBook(Book book);

  /**
   * Updates the details of an existing book in the library system.
   *
   * @param book The Book object containing the updated information for the book.
   */
  void updateBook(Book book);

  /**
   * Retrieves the unique identifier of a book.
   *
   * @return The unique identifier of the book as a String.
   */
  String getBookId();

  /**
   * Checks if the quantity of a book is sufficient given its unique identifier.
   *
   * @param bookId The unique identifier of the book to be checked.
   * @return true if the book quantity is enough, false otherwise.
   */
  boolean isQuantityEnough(String bookId);
}
