package com.example.library.services;

import com.example.library.models.Account;
import com.example.library.models.Reader;

/**
 * Interface representing account-related services.
 */
public interface IAccountService {

  /**
   * Checks if the provided account credentials are valid.
   *
   * @param account The Account object containing username, password, and role.
   * @return true if the account credentials are valid, false otherwise.
   */
  boolean checkAccount(Account account);

  /**
   * Registers a new user account and associates it with a reader profile.
   *
   * @param account The Account object containing the new account's credentials and role.
   * @param reader The Reader object containing the reader's profile information.
   * @return true if the account registration is successful, false otherwise.
   * @throws Exception if an error occurs during the registration process.
   */
  boolean registerAccount(Account account, Reader reader) throws Exception;

  /**
   * Checks if the user account with the provided username is blocked.
   *
   * @param username The username of the account to check.
   * @return true if the account is blocked, false otherwise.
   */
  boolean isBlocked(String username);

  /**
   * Resets the password for the account associated with the given email.
   *
   * @param email The email address associated with the account for which the password is
   *              to be reset.
   * @throws Exception If an error occurs during the password reset process.
   */
  void resetPassword(String email) throws Exception;

  /**
   * Changes the password for the given account to the new specified password.
   *
   * @param account The Account object for which the password is to be changed. The account object
   *                should have the current credentials.
   * @param newPassword The new password to be set for the account.
   * @throws Exception if the password change operation encounters an error.
   */
  void changePassword(Account account, String newPassword) throws Exception;
}
