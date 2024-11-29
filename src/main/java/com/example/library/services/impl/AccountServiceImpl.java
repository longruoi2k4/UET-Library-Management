package com.example.library.services.impl;

import com.example.library.models.Account;
import com.example.library.models.Reader;
import com.example.library.repositories.IAccountRepository;
import com.example.library.repositories.IReaderRepository;
import com.example.library.repositories.impl.AccountRepositoryImpl;
import com.example.library.repositories.impl.ReaderRepositoryImpl;
import com.example.library.services.IAccountService;
import com.example.library.utils.PasswordHasher;
import com.example.library.utils.UserContext;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of the IAccountService interface providing functionality for
 * account-related operations.
 * This class manages account registration, login checks, password changes, password resets,
 * and checking account status.
 */
public class AccountServiceImpl implements IAccountService {
  private final IAccountRepository accountRepository;
  private final IReaderRepository readerRepository;
  private final MailService mailService;

  /**
   * Constructor for the AccountServiceImpl class.
   * Initializes the service with default implementations of required repositories
   * and a MailService.
   */
  public AccountServiceImpl() {
    this.accountRepository = new AccountRepositoryImpl();
    this.readerRepository = new ReaderRepositoryImpl();
    this.mailService = new MailService("smtp.gmail.com");
  }

  /**
   * Constructor for the AccountServiceImpl class.
   * Initializes the service with specific implementations of required repositories and
   * a MailService.
   *
   * @param accountRepository the repository for managing account information
   * @param readerRepository the repository for managing reader information
   * @param mailService the service for handling mail operations
   */
  public AccountServiceImpl(
      IAccountRepository accountRepository,
      IReaderRepository readerRepository,
      MailService mailService) {
    this.accountRepository = accountRepository;
    this.readerRepository = readerRepository;
    this.mailService = mailService;
  }

  /**
   * Checks if the account associated with the given username is blocked.
   *
   * @param username the username of the account to be checked
   * @return {@code true} if the account is blocked, {@code false} otherwise
   */
  public boolean isBlocked(String username) {
    return accountRepository.isBlocked(username);
  }

  @Override
  public boolean checkAccount(Account account) {
    Optional<Account> accountFromDb =
        accountRepository.getAccountAndRoleByUsername(account.getUsername());
    if (accountFromDb.isPresent()) {
      Account accountGet = accountFromDb.get();

      String hashedPassword = PasswordHasher.hashPassword(account.getPassword());

      return hashedPassword.equals(accountGet.getPassword())
          && account.getRole().equalsIgnoreCase(accountGet.getRole());
    }

    return false;
  }

  @Override
  public boolean registerAccount(Account account, Reader reader) throws Exception {
    if (accountRepository.isExistUsername(account.getUsername())) {
      throw new IllegalArgumentException("Username already exists");
    }

    boolean isExistReaderPhoneNumber =
        readerRepository.isExistReaderPhoneNumber(reader.getReaderPhone());
    boolean isExistReaderEmail = readerRepository.isExistReaderEmail(reader.getReaderEmail());

    if (isExistReaderPhoneNumber || isExistReaderEmail) {
      throw new IllegalArgumentException("Phone number or email already exists");
    }

    account.setPassword(PasswordHasher.hashPassword(account.getPassword()));

    accountRepository.save(account);

    int userId = accountRepository.getUserIdByUsername(account.getUsername());
    reader.setUserId(userId);
    try {
      readerRepository.save(reader);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return true;
  }

  /**
   * Resets the password for an account identified by the given email address.
   * If the account with the specified email exists, it generates a new password,
   * sends it to the email, and updates the account with the new password.
   *
   * @param email the email address associated with the account whose password is to be reset
   * @throws Exception if the email is not found in the account repository
   */
  @Override
  public void resetPassword(String email) throws Exception {
    Map<String, String> account = accountRepository.getAccountInfoByEmail(email);

    if (account == null) {
      throw new Exception("Email not found");
    } else {
      String username = account.get("username");
      String name = account.get("readerName");
      String password = UUID.randomUUID().toString().substring(0, 8);

      mailService.sendMail(
          Map.of(
              email,
              String.format(
                  "Hello <b>%s</b>,"
                      + " your new password is <b>%s</b> and username is <b>%s</b>,"
                      + " please change after login",
                  name, password, username)),
          "Reset password");
      String passwordHash = PasswordHasher.hashPassword(password);
      accountRepository.save(new Account(username, passwordHash, "reader"));
    }
  }

  @Override
  public void changePassword(Account account, String newPassword) throws Exception {
    Account existAccount =
        accountRepository.getAccountAndRoleByUsername(account.getUsername()).orElseThrow();

    String oldPasswordHash = PasswordHasher.hashPassword(account.getPassword());
    if (existAccount.getPassword().equals(oldPasswordHash)) {
      newPassword = PasswordHasher.hashPassword(newPassword);
      accountRepository.save(
          new Account(account.getUsername(), newPassword, UserContext.getInstance().getRole()));

    } else {
      throw new Exception("Current password not match");
    }
  }
}
