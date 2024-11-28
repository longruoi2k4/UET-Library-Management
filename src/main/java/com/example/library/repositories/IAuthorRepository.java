package com.example.library.repositories;

import com.example.library.models.Author;
import javafx.collections.ObservableList;

/**
 * The IAuthorRepository interface defines the methods for managing
 * author information within a repository.
 */
public interface IAuthorRepository {

  /**
   * Retrieves a list of all authors from the repository.
   *
   * @return an observable list of Author objects representing all authors in the repository.
   */
  ObservableList<Author> getAllAuthor();

  /**
   * Retrieves the unique identifier of an author based on the provided author name.
   *
   * @param author the name of the author whose ID is to be retrieved
   * @return the unique identifier of the author, or {@code null} if the author does not exist
   *         in the repository
   */
  String getAuthorIdByName(String author);
}
