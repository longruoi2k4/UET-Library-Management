package library.repositories.impl;

import com.example.library.models.Reader;
import com.example.library.repositories.impl.ReaderRepositoryImpl;
import com.example.library.utils.DbConnect;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReaderRepositoryImplTest {

  /**
   * Helper method to inject the mock DbConnect instance into ReaderRepositoryImpl using reflection.
   * This method sets the dbConnect field of ReaderRepositoryImpl to the provided mock instance.
   *
   * @param repository the ReaderRepositoryImpl instance
   * @param dbConnectMock the mock DbConnect instance
   */
  private void injectDbConnect(ReaderRepositoryImpl repository, DbConnect dbConnectMock) {
    try {
      Field field = ReaderRepositoryImpl.class.getDeclaredField("dbConnect");
      field.setAccessible(true);
      field.set(repository, dbConnectMock);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Test Case: Ensure getAllReaders() method retrieves all records correctly. The method should
   * return a list of Reader objects populated from the ResultSet.
   */
  @Test
  void testGetAllReaders() throws SQLException {
    // Mock DbConnect and ResultSet
    DbConnect dbConnectMock = mock(DbConnect.class);
    ResultSet resultSetMock = mock(ResultSet.class);

    // Mock the behavior of the resultSet to simulate one row being returned
    when(resultSetMock.next()).thenReturn(true).thenReturn(false);
    when(resultSetMock.getString("readerId")).thenReturn("R001");
    when(resultSetMock.getString("readerName")).thenReturn("John Doe");
    when(resultSetMock.getString("readerEmail")).thenReturn("john.doe@example.com");
    when(resultSetMock.getString("readerPhoneNumber")).thenReturn("1234567890");
    when(resultSetMock.getDate("readerDOB"))
        .thenReturn(java.sql.Date.valueOf(LocalDate.of(1990, 1, 1)));
    when(resultSetMock.getString("address")).thenReturn("123 Fake Street");
    when(resultSetMock.getBoolean("isBlock")).thenReturn(false);
    when(resultSetMock.getString("username")).thenReturn("johndoe");

    // Mock the executeQuery method of DbConnect to return the mock ResultSet
    when(dbConnectMock.executeQuery(anyString())).thenReturn(resultSetMock);

    // Instantiate the repository and inject the mock DbConnect
    ReaderRepositoryImpl repository = new ReaderRepositoryImpl();
    injectDbConnect(repository, dbConnectMock);

    // Call getAllReaders and get the list of Reader objects
    ObservableList<Reader> readers = repository.getAllReaders();

    // Validate the results
    assertNotNull(readers);
    assertEquals(1, readers.size());

    Reader reader = readers.get(0);
    // Validate the fields of the returned Reader object
    assertEquals("R001", reader.getReaderId());
    assertEquals("John Doe", reader.getReaderName());
    assertEquals("john.doe@example.com", reader.getReaderEmail());
    assertEquals("1234567890", reader.getReaderPhone());
    assertEquals(LocalDate.of(1990, 1, 1), reader.getReaderDOB());
    assertEquals("123 Fake Street", reader.getReaderAddress());
    assertFalse(reader.isBlocked());
    assertEquals("johndoe", reader.getUsername());
  }

  /**
   * Test Case: Ensure getAllReaders() method handles empty ResultSet correctly. The method should
   * return an empty list when there are no records in the database.
   */
  @Test
  void testGetAllReadersEmptyResultSet() throws SQLException {
    // Mock DbConnect and ResultSet
    DbConnect dbConnectMock = mock(DbConnect.class);
    ResultSet resultSetMock = mock(ResultSet.class);

    // Mock the behavior of the resultSet to simulate no rows being returned
    when(resultSetMock.next()).thenReturn(false);

    // Mock the executeQuery method of DbConnect to return the mock ResultSet
    when(dbConnectMock.executeQuery(anyString())).thenReturn(resultSetMock);

    // Instantiate the repository and inject the mock DbConnect
    ReaderRepositoryImpl repository = new ReaderRepositoryImpl();
    injectDbConnect(repository, dbConnectMock);

    // Call getAllReaders and get the list of Reader objects
    ObservableList<Reader> readers = repository.getAllReaders();

    // Validate the results
    assertNotNull(readers);
    assertTrue(readers.isEmpty());
  }

  /**
   * Test Case: Ensure getAllReaders() method handles SQLException correctly. The method should
   * return an empty list when an SQLException is thrown.
   */
  @Test
  void testGetAllReadersSQLException() throws SQLException {
    // Mock DbConnect and ResultSet
    DbConnect dbConnectMock = mock(DbConnect.class);
    ResultSet resultSetMock = mock(ResultSet.class);

    // Mock the behavior of the resultSet to throw SQLException when next() is called
    when(resultSetMock.next()).thenThrow(new SQLException());

    // Mock the executeQuery method of DbConnect to return the mock ResultSet
    when(dbConnectMock.executeQuery(anyString())).thenReturn(resultSetMock);

    // Instantiate the repository and inject the mock DbConnect
    ReaderRepositoryImpl repository = new ReaderRepositoryImpl();
    injectDbConnect(repository, dbConnectMock);

    // Call getAllReaders and get the list of Reader objects
    ObservableList<Reader> readers = repository.getAllReaders();

    // Validate the results
    assertNotNull(readers);
    assertTrue(readers.isEmpty());
  }
}
