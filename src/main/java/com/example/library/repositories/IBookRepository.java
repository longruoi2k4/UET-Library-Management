package com.example.library.repositories;

import com.example.library.models.Book;
import javafx.collections.ObservableList;

/**
 * The IBookRepository interface defines the methods for managing
 * book information within a repository. It includes methods for
 * saving, deleting, and retrieving book-related data such as
 * book categories, authors, and quantities.
 */
public interface IBookRepository {

  /**
   * Saves the provided Book object to the repository.
   *
   * @param book the Book object to be saved
   */
  void save(Book book);

  /**
   * Deletes the specified Book object from the repository.
   *
   * @param book the Book object to be deleted
   */
  void delete(Book book);

  /**
   * Saves the specified book category to the repository.
   *
   * @param category the name of the category to be saved
   */
  void saveCategory(String category);

  /**
   * Saves the specified author's name to the repository.
   *
   * @param author the name of the author to be saved
   */
  void saveAuthor(String author);

  /**
   * Retrieves the unique identifier of a category based on the provided category name.
   *
   * @param categoryName the name of the category whose ID is to be retrieved
   * @return the unique identifier of the category, or {@code null} if the category does not exist
   *         in the repository
   */
  String getCategoryIdByName(String categoryName);

  /**
   * Retrieves a list of all books from the repository.
   *
   * @return an observable list of Book objects representing all books in the repository.
   */
  ObservableList<Book> getAllBook();

  /**
   * Retrieves a list of all category names from the repository.
   *
   * @return an observable list of strings representing all category names in the repository.
   */
  ObservableList<String> getAllCategoryName();

  /**
   * Retrieves the unique identifier of a book based on the provided book name.
   *
   * @param bookName the name of the book whose ID is to be retrieved
   * @return the unique identifier of the book, or {@code null} if the book does not exist
   *         in the repository
   */
  String getBookIdByName(String bookName);

  /**
   * Increases the quantity of a book in the repository based on the provided book ID.
   *
   * @param bookId the unique identifier of the book whose quantity is to be increased
   */
  void increaseQuantity(String bookId);

  /**
   * Decreases the quantity of a book in the repository based on the provided book ID.
   *
   * @param bookId the unique identifier of the book whose quantity is to be decreased
   */
  void decreaseQuantity(String bookId);

  /**
   * Retrieves a list of all book IDs from the repository.
   *
   * @return an observable list of strings representing all book IDs in the repository.
   */
  ObservableList<String> getAllBookId();

  /**
   * Retrieves the name of a book based on its unique identifier.
   *
   * @param bookId the unique identifier of the book whose name is to be retrieved
   * @return the name of the book
   */
  String getBookNameById(String bookId);

  /**
   * Retrieves the total number of books available in the repository.
   *
   * @return the total number of books.
   */
  int getTotalBook();

  /**
   * Retrieves the unique identifier of a book.
   *
   * @return the unique identifier of the book.
   */
  String getBookId();

  /**
   * Retrieves the quantity of a book based on its unique identifier.
   *
   * @param bookId the unique identifier of the book whose quantity is to be retrieved
   * @return the quantity of the specified book
   */
  int getBookQuantity(String bookId);

  /**
   * Checks if a book with the specified unique identifier exists in the repository.
   *
   * @param bookId the unique identifier of the book to check for existence
   * @return true if the book exists in the repository, false otherwise
   */
  boolean isExistBook(String bookId);
}
