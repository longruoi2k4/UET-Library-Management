package library.services.impl;

import com.example.library.models.Account;
import com.example.library.repositories.IAccountRepository;
import com.example.library.repositories.IReaderRepository;
import com.example.library.services.impl.AccountServiceImpl;
import com.example.library.services.impl.MailService;
import com.example.library.utils.PasswordHasher;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for AccountServiceImpl. This class contains unit tests for the method checkAccount in
 * the AccountServiceImpl class.
 */
public class AccountServiceImplTest {

  /**
   * Tests the checkAccount method with a valid account. The method should return true when the
   * account exists and the password and role match.
   */
  @Test
  public void testCheckAccount_ValidAccount() {
    IAccountRepository accountRepository = mock(IAccountRepository.class);
    IReaderRepository readerRepository = mock(IReaderRepository.class);
    MailService mailService = mock(MailService.class);
    AccountServiceImpl accountService =
        new AccountServiceImpl(accountRepository, readerRepository, mailService);

    Account accountInput =
        Account.builder().username("testuser").password("password").role("ROLE_USER").build();
    Account accountFromDb =
        Account.builder()
            .username("testuser")
            .password(PasswordHasher.hashPassword("password"))
            .role("ROLE_USER")
            .build();

    when(accountRepository.getAccountAndRoleByUsername("testuser"))
        .thenReturn(Optional.of(accountFromDb));

    boolean result = accountService.checkAccount(accountInput);

    assertTrue(result, "Expected checkAccount to return true for valid account");
  }

  /**
   * Tests the checkAccount method with an account having an invalid password. The method should
   * return false when the account password does not match the one in the database.
   */
  @Test
  public void testCheckAccount_InvalidPassword() {
    IAccountRepository accountRepository = mock(IAccountRepository.class);
    IReaderRepository readerRepository = mock(IReaderRepository.class);
    MailService mailService = mock(MailService.class);
    AccountServiceImpl accountService =
        new AccountServiceImpl(accountRepository, readerRepository, mailService);

    Account accountInput =
        Account.builder().username("testuser").password("wrongpassword").role("ROLE_USER").build();
    Account accountFromDb =
        Account.builder()
            .username("testuser")
            .password(PasswordHasher.hashPassword("password"))
            .role("ROLE_USER")
            .build();

    when(accountRepository.getAccountAndRoleByUsername("testuser"))
        .thenReturn(Optional.of(accountFromDb));

    boolean result = accountService.checkAccount(accountInput);

    assertFalse(result, "Expected checkAccount to return false for invalid password");
  }

  /**
   * Tests the checkAccount method with a nonexistent account. The method should return false when
   * the account does not exist in the database.
   */
  @Test
  public void testCheckAccount_NonexistentAccount() {
    IAccountRepository accountRepository = mock(IAccountRepository.class);
    IReaderRepository readerRepository = mock(IReaderRepository.class);
    MailService mailService = mock(MailService.class);
    AccountServiceImpl accountService =
        new AccountServiceImpl(accountRepository, readerRepository, mailService);

    Account accountInput =
        Account.builder()
            .username("nonexistentuser")
            .password("password")
            .role("ROLE_USER")
            .build();

    when(accountRepository.getAccountAndRoleByUsername("nonexistentuser"))
        .thenReturn(Optional.empty());

    boolean result = accountService.checkAccount(accountInput);

    assertFalse(result, "Expected checkAccount to return false for nonexistent account");
  }

  /**
   * Tests the checkAccount method with an account having an invalid role. The method should return
   * false when the account role does not match the one in the database.
   */
  @Test
  public void testCheckAccount_InvalidRole() {
    IAccountRepository accountRepository = mock(IAccountRepository.class);
    IReaderRepository readerRepository = mock(IReaderRepository.class);
    MailService mailService = mock(MailService.class);
    AccountServiceImpl accountService =
        new AccountServiceImpl(accountRepository, readerRepository, mailService);

    Account accountInput =
        Account.builder().username("testuser").password("password").role("ROLE_ADMIN").build();
    Account accountFromDb =
        Account.builder()
            .username("testuser")
            .password(PasswordHasher.hashPassword("password"))
            .role("ROLE_USER")
            .build();

    when(accountRepository.getAccountAndRoleByUsername("testuser"))
        .thenReturn(Optional.of(accountFromDb));

    boolean result = accountService.checkAccount(accountInput);

    assertFalse(result, "Expected checkAccount to return false for invalid role");
  }
}
