package library.repositories.impl;

import com.example.library.models.Borrow;
import com.example.library.repositories.impl.BorrowRepositoryImpl;
import com.example.library.utils.DbConnect;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BorrowRepositoryImplTest {

  private void injectDbConnect(BorrowRepositoryImpl borrowRepository, DbConnect dbConnectMock) {
    try {
      Field field = BorrowRepositoryImpl.class.getDeclaredField("dbConnect");
      field.setAccessible(true);
      field.set(borrowRepository, dbConnectMock);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void testGetBorrowByReaderId_validId_singleResult() throws SQLException {
    // Arrange
    String readerId = "validReaderId";
    DbConnect dbConnectMock = mock(DbConnect.class);
    ResultSet rsMock = mock(ResultSet.class);

    when(dbConnectMock.executeQuery(Mockito.anyString())).thenReturn(rsMock);
    when(rsMock.next()).thenReturn(true).thenReturn(false);
    when(rsMock.getString("borrowId")).thenReturn("1");
    when(rsMock.getString("bookName")).thenReturn("Effective Java");
    when(rsMock.getString("readerName")).thenReturn("John Doe");
    when(rsMock.getDate("borrowDate")).thenReturn(Date.valueOf(LocalDate.of(2023, 9, 1)));
    when(rsMock.getDate("returnDate")).thenReturn(Date.valueOf(LocalDate.of(2023, 9, 15)));
    when(rsMock.getString("dueDate")).thenReturn("2023-09-16");
    when(rsMock.getString("status")).thenReturn("Borrowed");
    when(rsMock.getString("bookId")).thenReturn("12345");

    BorrowRepositoryImpl borrowRepository = new BorrowRepositoryImpl();
    injectDbConnect(borrowRepository, dbConnectMock);

    // Act
    ObservableList<Borrow> result = borrowRepository.getBorrowByReaderId(readerId);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.size());
    Borrow borrow = result.get(0);
    assertEquals("1", borrow.getBorrowId());
    assertEquals("Effective Java", borrow.getBookName());
    assertEquals("John Doe", borrow.getReaderName());
    assertEquals(LocalDate.of(2023, 9, 1), borrow.getBorrowDate());
    assertEquals(LocalDate.of(2023, 9, 15), borrow.getReturnDate());
    assertEquals("2023-09-16", borrow.getDueDate());
    assertEquals("Borrowed", borrow.getStatus());
    assertEquals("12345", borrow.getBookId());
  }

  @Test
  void testGetBorrowByReaderId_validId_multipleResults() throws SQLException {
    // Arrange
    String readerId = "validReaderId";
    DbConnect dbConnectMock = mock(DbConnect.class);
    ResultSet rsMock = mock(ResultSet.class);

    when(dbConnectMock.executeQuery(Mockito.anyString())).thenReturn(rsMock);
    when(rsMock.next()).thenReturn(true).thenReturn(true).thenReturn(false);
    when(rsMock.getString("borrowId")).thenReturn("1").thenReturn("2");
    when(rsMock.getString("bookName")).thenReturn("Effective Java").thenReturn("Clean Code");
    when(rsMock.getString("readerName")).thenReturn("John Doe").thenReturn("John Doe");
    when(rsMock.getDate("borrowDate"))
        .thenReturn(Date.valueOf(LocalDate.of(2023, 9, 1)))
        .thenReturn(Date.valueOf(LocalDate.of(2023, 10, 1)));
    when(rsMock.getDate("returnDate"))
        .thenReturn(Date.valueOf(LocalDate.of(2023, 9, 15)))
        .thenReturn(Date.valueOf(LocalDate.of(2023, 10, 15)));
    when(rsMock.getString("dueDate")).thenReturn("2023-09-16").thenReturn("2023-10-16");
    when(rsMock.getString("status")).thenReturn("Borrowed").thenReturn("Returned");
    when(rsMock.getString("bookId")).thenReturn("12345").thenReturn("67890");

    BorrowRepositoryImpl borrowRepository = new BorrowRepositoryImpl();
    injectDbConnect(borrowRepository, dbConnectMock);

    // Act
    ObservableList<Borrow> result = borrowRepository.getBorrowByReaderId(readerId);

    // Assert
    assertNotNull(result);
    assertEquals(2, result.size());
    Borrow borrow1 = result.get(0);
    Borrow borrow2 = result.get(1);

    assertEquals("1", borrow1.getBorrowId());
    assertEquals("Effective Java", borrow1.getBookName());
    assertEquals("John Doe", borrow1.getReaderName());
    assertEquals(LocalDate.of(2023, 9, 1), borrow1.getBorrowDate());
    assertEquals(LocalDate.of(2023, 9, 15), borrow1.getReturnDate());
    assertEquals("2023-09-16", borrow1.getDueDate());
    assertEquals("Borrowed", borrow1.getStatus());
    assertEquals("12345", borrow1.getBookId());

    assertEquals("2", borrow2.getBorrowId());
    assertEquals("Clean Code", borrow2.getBookName());
    assertEquals("John Doe", borrow2.getReaderName());
    assertEquals(LocalDate.of(2023, 10, 1), borrow2.getBorrowDate());
    assertEquals(LocalDate.of(2023, 10, 15), borrow2.getReturnDate());
    assertEquals("2023-10-16", borrow2.getDueDate());
    assertEquals("Returned", borrow2.getStatus());
    assertEquals("67890", borrow2.getBookId());
  }

  @Test
  void testGetBorrowByReaderId_noResults() throws SQLException {
    // Arrange
    String readerId = "validReaderId";
    DbConnect dbConnectMock = mock(DbConnect.class);
    ResultSet rsMock = mock(ResultSet.class);

    when(dbConnectMock.executeQuery(Mockito.anyString())).thenReturn(rsMock);
    when(rsMock.next()).thenReturn(false);

    BorrowRepositoryImpl borrowRepository = new BorrowRepositoryImpl();
    injectDbConnect(borrowRepository, dbConnectMock);

    // Act
    ObservableList<Borrow> result = borrowRepository.getBorrowByReaderId(readerId);

    // Assert
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void testGetBorrowByReaderId_sqlException() throws SQLException {
    // Arrange
    String readerId = "validReaderId";
    DbConnect dbConnectMock = mock(DbConnect.class);
    ResultSet rsMock = mock(ResultSet.class);

    when(dbConnectMock.executeQuery(Mockito.anyString())).thenReturn(rsMock);
    when(rsMock.next()).thenThrow(new SQLException());

    BorrowRepositoryImpl borrowRepository = new BorrowRepositoryImpl();
    injectDbConnect(borrowRepository, dbConnectMock);

    // Act & Assert
    assertDoesNotThrow(
        () -> {
          ObservableList<Borrow> result = borrowRepository.getBorrowByReaderId(readerId);
          assertNotNull(result);
          assertTrue(result.isEmpty());
        });
  }
}
