package com.example.library.services;

import com.example.library.models.Reader;
import javafx.collections.ObservableList;

/**
 * Interface representing reader-related services.
 */
public interface IReaderService {

  /**
   * Retrieves a list of all readers registered in the library system.
   *
   * @return an ObservableList containing all Reader objects.
   */
  ObservableList<Reader> getAllReaders();

  /**
   * Retrieves a list of all reader IDs registered in the library system.
   *
   * @return an ObservableList containing all reader IDs as strings.
   */
  ObservableList<String> getAllReaderId();

  /**
   * Retrieves the name of a reader given their unique identifier.
   *
   * @param readerId The unique identifier of the reader for whom the name is to be retrieved.
   * @return The name of the reader associated with the provided reader ID.
   */
  String getReaderNameById(String readerId);

  /**
   * Persists a given Reader object to the storage.
   *
   * @param reader The Reader object to be saved.
   */
  void saveReader(Reader reader);

  /**
   * Updates the details of an existing reader in the library system.
   *
   * @param reader The Reader object containing the updated information for the reader.
   * @throws Exception If an error occurs while updating the reader's details.
   */
  void updateReader(Reader reader) throws Exception;

  /**
   * Deletes a specified reader from the library system.
   *
   * @param reader The Reader object representing the reader to be deleted.
   */
  void deleteReader(Reader reader);

  /**
   * Retrieves the unique identifier of a reader.
   *
   * @return The unique identifier of the reader as a String.
   */
  String getReaderId();

  /**
   * Retrieves a Reader object corresponding to the provided username.
   *
   * @param username The username of the reader to be retrieved.
   * @return The Reader object associated with the provided username.
   */
  Reader getReaderByUsername(String username);
}
