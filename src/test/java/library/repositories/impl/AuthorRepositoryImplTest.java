package library.repositories.impl;

import com.example.library.models.Author;
import com.example.library.repositories.impl.AuthorRepositoryImpl;
import com.example.library.utils.DbConnect;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthorRepositoryImplTest {

  @Test
  public void testGetAllAuthor_returnsEmptyListWhenNoData()
      throws SQLException, NoSuchFieldException, IllegalAccessException {
    // Arrange
    // Mock DbConnect and ResultSet instances
    DbConnect mockDbConnect = mock(DbConnect.class);
    ResultSet mockResultSet = mock(ResultSet.class);
    // Define behavior for the mocked executeQuery and next methods
    when(mockDbConnect.executeQuery(Mockito.anyString())).thenReturn(mockResultSet);
    when(mockResultSet.next()).thenReturn(false);

    // Initialize repository with mock DbConnect instance
    AuthorRepositoryImpl repository = new AuthorRepositoryImpl();

    // Use reflection to set the dbConnect field
    Field dbConnectField = AuthorRepositoryImpl.class.getDeclaredField("dbConnect");
    dbConnectField.setAccessible(true);
    dbConnectField.set(repository, mockDbConnect);

    // Act
    // Call getAllAuthor method
    ObservableList<Author> authors = repository.getAllAuthor();

    // Assert
    // Verify that the authors list is empty
    assertEquals(0, authors.size());
  }

  @Test
  public void testGetAllAuthor_returnsSingleAuthor()
      throws SQLException, NoSuchFieldException, IllegalAccessException {
    // Arrange
    // Mock DbConnect and ResultSet instances
    DbConnect mockDbConnect = mock(DbConnect.class);
    ResultSet mockResultSet = mock(ResultSet.class);
    // Define behavior for the mocked executeQuery and next methods
    when(mockDbConnect.executeQuery(Mockito.anyString())).thenReturn(mockResultSet);
    when(mockResultSet.next()).thenReturn(true).thenReturn(false);
    // Define behavior for the mocked getString methods
    when(mockResultSet.getString("authorId")).thenReturn("1");
    when(mockResultSet.getString("authorName")).thenReturn("Author Name");

    // Initialize repository with mock DbConnect instance
    AuthorRepositoryImpl repository = new AuthorRepositoryImpl();

    // Use reflection to set the dbConnect field
    Field dbConnectField = AuthorRepositoryImpl.class.getDeclaredField("dbConnect");
    dbConnectField.setAccessible(true);
    dbConnectField.set(repository, mockDbConnect);

    // Act
    // Call getAllAuthor method
    ObservableList<Author> authors = repository.getAllAuthor();

    // Assert
    // Verify that the authors list contains one author with the expected details
    assertEquals(1, authors.size());
    assertEquals("1", authors.get(0).getAuthorId());
    assertEquals("Author Name", authors.get(0).getAuthorName());
  }

  @Test
  public void testGetAllAuthor_returnsMultipleAuthors()
      throws SQLException, NoSuchFieldException, IllegalAccessException {
    // Arrange
    // Mock DbConnect and ResultSet instances
    DbConnect mockDbConnect = mock(DbConnect.class);
    ResultSet mockResultSet = mock(ResultSet.class);
    // Define behavior for the mocked executeQuery and next methods
    when(mockDbConnect.executeQuery(Mockito.anyString())).thenReturn(mockResultSet);
    when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
    // Define behavior for the mocked getString methods
    when(mockResultSet.getString("authorId")).thenReturn("1").thenReturn("2");
    when(mockResultSet.getString("authorName")).thenReturn("Author One").thenReturn("Author Two");

    // Initialize repository with mock DbConnect instance
    AuthorRepositoryImpl repository = new AuthorRepositoryImpl();

    // Use reflection to set the dbConnect field
    Field dbConnectField = AuthorRepositoryImpl.class.getDeclaredField("dbConnect");
    dbConnectField.setAccessible(true);
    dbConnectField.set(repository, mockDbConnect);

    // Act
    // Call getAllAuthor method
    ObservableList<Author> authors = repository.getAllAuthor();

    // Assert
    // Verify that the authors list contains two authors with the expected details
    assertEquals(2, authors.size());
    assertEquals("1", authors.get(0).getAuthorId());
    assertEquals("Author One", authors.get(0).getAuthorName());
    assertEquals("2", authors.get(1).getAuthorId());
    assertEquals("Author Two", authors.get(1).getAuthorName());
  }

  @Test
  public void testGetAllAuthor_sqlException()
      throws SQLException, NoSuchFieldException, IllegalAccessException {
    // Arrange
    // Mock DbConnect and ResultSet instances
    DbConnect mockDbConnect = mock(DbConnect.class);
    ResultSet mockResultSet = mock(ResultSet.class);
    // Define behavior for the mocked executeQuery and next methods
    when(mockDbConnect.executeQuery(Mockito.anyString())).thenReturn(mockResultSet);
    // Simulate SQL exception when calling next method
    when(mockResultSet.next()).thenThrow(new SQLException("Database Error"));

    // Initialize repository with mock DbConnect instance
    AuthorRepositoryImpl repository = new AuthorRepositoryImpl();

    // Use reflection to set the dbConnect field
    Field dbConnectField = AuthorRepositoryImpl.class.getDeclaredField("dbConnect");
    dbConnectField.setAccessible(true);
    dbConnectField.set(repository, mockDbConnect);

    // Act
    // Call getAllAuthor method
    ObservableList<Author> authors = repository.getAllAuthor();

    // Assert
    // Verify that the authors list is empty since an exception is thrown
    assertEquals(0, authors.size());
  }
}
