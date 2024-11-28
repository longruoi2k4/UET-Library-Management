package com.example.library.services;

/**
 * Interface representing a service for retrieving statistical information
 * about books, readers, borrow transactions, and returns in a library system.
 */
public interface IStaticService {

  /**
   * Calculates and returns the total number of books in the library system.
   *
   * @return the total number of books available.
   */
  int getTotalBook();

  /**
   * Calculates and returns the total number of readers registered in the library system.
   *
   * @return the total number of registered readers.
   */
  int getTotalReader();

  /**
   * Calculates and returns the total number of books borrowed in the library system.
   *
   * @return the total number of borrowed books.
   */
  int getTotalBorrow();

  /**
   * Calculates and returns the total number of late book returns in the library system.
   *
   * @return the total number of late book returns.
   */
  int getTotalLate();

  /**
   * Calculates and returns the total number of book returns in the library system.
   *
   * @return the total number of book returns.
   */
  int getTotalReturn();
}
