package com.example.library.repositories;

import com.example.library.models.Account;
import java.util.Map;
import java.util.Optional;

/**
 * The IAccountRepository interface defines the methods required for
 * managing Account information within a repository.
 */
public interface IAccountRepository {

  /**
   * Retrieves an Account along with its associated role based on the provided username.
   *
   * @param username the username associated with the Account to be retrieved
   * @return an Optional containing the Account if found, or an empty Optional if no account
   *         exists with the provided username
   */
  Optional<Account> getAccountAndRoleByUsername(String username);

  /**
   * Checks if an account with the specified username exists.
   *
   * @param username the username to be checked
   * @return true if an account with the specified username exists, false otherwise
   */
  boolean isExistUsername(String username);

  /**
   * Saves the provided Account to the repository.
   *
   * @param account the Account object to be saved
   */
  void save(Account account);

  /**
   * Checks if the account associated with the given username is blocked.
   *
   * @param username the username of the account to be checked
   * @return {@code true} if the account is blocked, {@code false} otherwise
   */
  boolean isBlocked(String username);

  /**
   * Retrieves account information based on the provided email address.
   *
   * @param email the email address associated with the account whose information is to be retrieved
   * @return a map containing account information, with keys representing attribute names
   *          (e.g., "username", "readerName") and values as their corresponding data.
   *          Returns null if no account is found with the provided email.
   */
  Map<String, String> getAccountInfoByEmail(String email);

  /**
   * Retrieves the user ID associated with the provided username.
   *
   * @param username the username of the account whose user ID is to be retrieved
   * @return the user ID associated with the specified username
   */
  int getUserIdByUsername(String username);
}
