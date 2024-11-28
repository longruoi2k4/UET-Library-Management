package com.example.library.repositories;

import com.example.library.models.Reader;
import java.util.Optional;
import javafx.collections.ObservableList;

/**
 * The IReaderRepository interface defines the methods for managing
 * reader information within a repository. It includes functionalities
 * for retrieving, saving, deleting, and querying readers by various
 * attributes such as ID, name, email, and phone number.
 */
public interface IReaderRepository {

  /**
   * Retrieves a list of all readers from the repository.
   *
   * @return an observable list of Reader objects representing all readers in the repository.
   */
  ObservableList<Reader> getAllReaders();

  /**
   * Saves the provided Reader object to the repository.
   *
   * @param reader the Reader object to be saved
   */
  void save(Reader reader);

  /**
   * Deletes the specified Reader object from the repository.
   *
   * @param reader the Reader object to be deleted
   */
  void delete(Reader reader);

  /**
   * Retrieves a list of all reader IDs from the repository.
   *
   * @return an observable list of strings representing all reader IDs in the repository.
   */
  ObservableList<String> getAllReaderId();

  /**
   * Retrieves the name of a reader based on their unique identifier.
   *
   * @param readerId the unique identifier of the reader whose name is to be retrieved
   * @return the name of the reader, or {@code null} if the reader does not exist in the repository
   */
  String getReaderNameById(String readerId);

  /**
   * Retrieves the unique identifier of a reader based on the provided reader name.
   *
   * @param readerName the name of the reader whose ID is to be retrieved
   * @return the unique identifier of the reader, or {@code null} if the reader
   *         does not exist in the repository
   */
  String getReaderIdByName(String readerName);

  /**
   * Retrieves the total number of readers present in the repository.
   *
   * @return the total number of readers.
   */
  int getTotalReader();

  /**
   * Retrieves the unique identifier of a reader.
   *
   * @return the unique identifier of the reader.
   */
  String getReaderId();

  /**
   * Retrieves the Reader object corresponding to the given unique identifier.
   *
   * @param readerId the unique identifier of the reader to be retrieved
   * @return an Optional containing the Reader object if found, otherwise an empty Optional
   */
  Optional<Reader> getReaderById(String readerId);

  /**
   * Checks if a reader with the given phone number exists in the repository.
   *
   * @param readerPhoneNumber the phone number of the reader to check for existence
   * @return true if a reader with the specified phone number exists, false otherwise
   */
  boolean isExistReaderPhoneNumber(String readerPhoneNumber);

  /**
   * Checks if a reader with the specified email exists in the repository.
   *
   * @param readerEmail the email of the reader to check for existence
   * @return true if a reader with the specified email exists, false otherwise
   */
  boolean isExistReaderEmail(String readerEmail);

  /**
   * Retrieves the Reader object corresponding to the given username.
   *
   * @param username the username of the reader to be retrieved
   * @return the Reader object associated with the specified username
   */
  Reader getReaderByUsername(String username);

  /**
   * Checks if a reader with the specified email exists in the repository,
   * excluding the reader with the provided reader ID.
   *
   * @param email the email of the reader to check for existence
   * @param readerId the unique identifier of the reader to be excluded
   * @return true if a reader with the specified email exists and has a different
   *         ID than the given reader ID, false otherwise
   */
  boolean existsByEmailAndNotId(String email, String readerId);

  /**
   * Checks if a reader with the specified phone number exists in the repository,
   * excluding the reader with the provided reader ID.
   *
   * @param phone the phone number of the reader to check for existence
   * @param readerId the unique identifier of the reader to be excluded
   * @return true if a reader with the specified phone number exists and has a
   *         different ID than the given reader ID, false otherwise
   */
  boolean existsByPhoneAndNotId(String phone, String readerId);
}
