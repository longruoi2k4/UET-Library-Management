package library.repositories.impl;

import com.example.library.models.Book;
import com.example.library.repositories.impl.BookRepositoryImpl;
import com.example.library.utils.DbConnect;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BookRepositoryImplTest {

  private void setMockDbConnect(BookRepositoryImpl bookRepository, DbConnect mockDbConnect) {
    try {
      Field dbConnectField = BookRepositoryImpl.class.getDeclaredField("dbConnect");
      dbConnectField.setAccessible(true);
      dbConnectField.set(bookRepository, mockDbConnect);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testSave_NewBook_InsertsBook() throws SQLException {
    // Arrange
    DbConnect mockDbConnect = mock(DbConnect.class);
    ResultSet mockResultSet = mock(ResultSet.class);

    BookRepositoryImpl bookRepository = new BookRepositoryImpl();
    setMockDbConnect(bookRepository, mockDbConnect);

    Book book =
        Book.builder()
            .bookId("B001")
            .author("1")
            .category("1")
            .bookName("Test Book")
            .quantity(5)
            .publisher(LocalDate.now())
            .isbn("1234567890123")
            .build();

    when(mockDbConnect.executeQuery(anyString())).thenReturn(mockResultSet);
    when(mockResultSet.next()).thenReturn(false);

    // Act
    bookRepository.save(book);

    // Assert
    Mockito.verify(mockDbConnect).executeUpdate(anyString());
  }

  @Test
  public void testSave_ExistingBook_UpdatesBook() throws SQLException {
    // Arrange
    DbConnect mockDbConnect = mock(DbConnect.class);
    ResultSet mockResultSet = mock(ResultSet.class);

    BookRepositoryImpl bookRepository = new BookRepositoryImpl();
    setMockDbConnect(bookRepository, mockDbConnect);

    Book book =
        Book.builder()
            .bookId("B002")
            .author("2")
            .category("2")
            .bookName("Existing Book")
            .quantity(10)
            .publisher(LocalDate.now().minusDays(1))
            .isbn("0123456789012")
            .build();

    when(mockDbConnect.executeQuery(anyString())).thenReturn(mockResultSet);
    when(mockResultSet.next()).thenReturn(true);

    // Act
    bookRepository.save(book);

    // Assert
    Mockito.verify(mockDbConnect).executeUpdate(anyString());
  }

  @Test
  public void testSave_DbException_ThrowsRuntimeException() throws SQLException {
    // Arrange
    DbConnect mockDbConnect = mock(DbConnect.class);
    ResultSet mockResultSet = mock(ResultSet.class);

    BookRepositoryImpl bookRepository = new BookRepositoryImpl();
    setMockDbConnect(bookRepository, mockDbConnect);

    Book book =
        Book.builder()
            .bookId("B003")
            .author("3")
            .category("3")
            .bookName("Exception Book")
            .quantity(7)
            .publisher(LocalDate.now().plusDays(1))
            .isbn("9876543210987")
            .build();

    when(mockDbConnect.executeQuery(anyString())).thenReturn(mockResultSet);
    when(mockResultSet.next()).thenThrow(new SQLException());

    // Act & Assert
    assertThrows(RuntimeException.class, () -> bookRepository.save(book));
  }
}
