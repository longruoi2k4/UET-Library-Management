package library.repositories.impl;

import com.example.library.models.Reader;
import com.example.library.repositories.impl.AccountRepositoryImpl;
import com.example.library.utils.DbConnect;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountRepositoryImplTest {

  private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
    Field field = target.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  @Test
  public void testGetInformation_UserExists() throws SQLException, Exception {
    // Arrange
    String username = "test_user";
    DbConnect mockDbConnect = mock(DbConnect.class);
    ResultSet mockResultSet = mock(ResultSet.class);

    when(mockDbConnect.executeQuery(anyString())).thenReturn(mockResultSet);
    when(mockResultSet.next()).thenReturn(true);
    when(mockResultSet.getString("readerId")).thenReturn("r1");
    when(mockResultSet.getString("readerName")).thenReturn("John Doe");
    when(mockResultSet.getString("readerEmail")).thenReturn("johndoe@example.com");
    when(mockResultSet.getString("readerPhoneNumber")).thenReturn("1234567890");
    when(mockResultSet.getDate("readerDOB"))
        .thenReturn(java.sql.Date.valueOf(LocalDate.of(1990, 1, 1)));
    when(mockResultSet.getString("address")).thenReturn("123 Main St");

    AccountRepositoryImpl accountRepository = new AccountRepositoryImpl();
    setPrivateField(accountRepository, "dbConnect", mockDbConnect);

    // Act
    Reader result = accountRepository.getInformation(username);

    // Assert
    assertNotNull(result); // Ensures that the result is not null
    assertEquals("r1", result.getReaderId()); // Checks if readerId is as expected
    assertEquals("John Doe", result.getReaderName()); // Checks if readerName is as expected
    assertEquals(
        "johndoe@example.com", result.getReaderEmail()); // Checks if readerEmail is as expected
    assertEquals("1234567890", result.getReaderPhone()); // Checks if readerPhone is as expected
    assertEquals(
        LocalDate.of(1990, 1, 1), result.getReaderDOB()); // Checks if readerDOB is as expected
    assertEquals(
        "123 Main St", result.getReaderAddress()); // Checks if readerAddress is as expected
  }

  @Test
  public void testGetInformation_UserDoesNotExist() throws SQLException, Exception {
    // Arrange
    String username = "non_existing_user";
    DbConnect mockDbConnect = mock(DbConnect.class);
    ResultSet mockResultSet = mock(ResultSet.class);

    when(mockDbConnect.executeQuery(anyString())).thenReturn(mockResultSet);
    when(mockResultSet.next()).thenReturn(false);

    AccountRepositoryImpl accountRepository = new AccountRepositoryImpl();
    setPrivateField(accountRepository, "dbConnect", mockDbConnect);

    // Act
    Reader result = accountRepository.getInformation(username);

    // Assert
    assertNull(result); // Ensures that the result is null for a non-existing user
  }

  @Test
  public void testGetInformation_SQLException() throws SQLException, Exception {
    // Arrange
    String username = "error_user";
    DbConnect mockDbConnect = mock(DbConnect.class);
    ResultSet mockResultSet = mock(ResultSet.class);

    when(mockDbConnect.executeQuery(anyString())).thenReturn(mockResultSet);
    when(mockResultSet.next()).thenThrow(new SQLException("Database error"));

    AccountRepositoryImpl accountRepository = new AccountRepositoryImpl();
    setPrivateField(accountRepository, "dbConnect", mockDbConnect);

    // Act and Assert
    RuntimeException exception =
        assertThrows(
            RuntimeException.class,
            () -> {
              accountRepository.getInformation(username);
            });

    assertEquals(
        "java.sql.SQLException: Database error",
        exception.getMessage()); // Ensures that the exception message is as expected
  }
}
