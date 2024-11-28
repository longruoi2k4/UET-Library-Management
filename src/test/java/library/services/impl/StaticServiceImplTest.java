package library.services.impl;

import com.example.library.repositories.IBookRepository;
import com.example.library.repositories.IBorrowRepository;
import com.example.library.repositories.IReaderRepository;
import com.example.library.services.impl.StaticServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the StaticServiceImpl class. These tests cover the getTotalBook, getTotalReader,
 * getTotalBorrow, getTotalLate, and getTotalReturn methods of the StaticServiceImpl class.
 */
public class StaticServiceImplTest {

  /** Tests that getTotalBook returns the correct total number of books. */
  @Test
  void testGetTotalBook_returnsCorrectTotal() {
    // Arrange
    IBookRepository bookRepositoryMock = Mockito.mock(IBookRepository.class);
    Mockito.when(bookRepositoryMock.getTotalBook()).thenReturn(15);
    StaticServiceImpl staticService =
        createStaticServiceImplWithMocks(bookRepositoryMock, null, null);

    // Act
    int result = staticService.getTotalBook();

    // Assert
    assertEquals(15, result);
  }

  /** Tests that getTotalBook returns zero when there are no books. */
  @Test
  void testGetTotalBook_returnsZeroWhenNoBooks() {
    // Arrange
    IBookRepository bookRepositoryMock = Mockito.mock(IBookRepository.class);
    Mockito.when(bookRepositoryMock.getTotalBook()).thenReturn(0);
    StaticServiceImpl staticService =
        createStaticServiceImplWithMocks(bookRepositoryMock, null, null);

    // Act
    int result = staticService.getTotalBook();

    // Assert
    assertEquals(0, result);
  }

  /** Tests that getTotalReader returns the correct total number of readers. */
  @Test
  void testGetTotalReader_returnsCorrectTotal() {
    // Arrange
    IReaderRepository readerRepositoryMock = Mockito.mock(IReaderRepository.class);
    Mockito.when(readerRepositoryMock.getTotalReader()).thenReturn(30);
    StaticServiceImpl staticService =
        createStaticServiceImplWithMocks(null, readerRepositoryMock, null);

    // Act
    int result = staticService.getTotalReader();

    // Assert
    assertEquals(30, result);
  }

  /** Tests that getTotalReader returns zero when there are no readers. */
  @Test
  void testGetTotalReader_returnsZeroWhenNoReaders() {
    // Arrange
    IReaderRepository readerRepositoryMock = Mockito.mock(IReaderRepository.class);
    Mockito.when(readerRepositoryMock.getTotalReader()).thenReturn(0);
    StaticServiceImpl staticService =
        createStaticServiceImplWithMocks(null, readerRepositoryMock, null);

    // Act
    int result = staticService.getTotalReader();

    // Assert
    assertEquals(0, result);
  }

  /** Tests that getTotalBorrow returns the correct total number of borrows. */
  @Test
  void testGetTotalBorrow_returnsCorrectTotal() {
    // Arrange
    IBorrowRepository borrowRepositoryMock = Mockito.mock(IBorrowRepository.class);
    Mockito.when(borrowRepositoryMock.getTotalBorrow()).thenReturn(20);
    StaticServiceImpl staticService =
        createStaticServiceImplWithMocks(null, null, borrowRepositoryMock);

    // Act
    int result = staticService.getTotalBorrow();

    // Assert
    assertEquals(20, result);
  }

  /** Tests that getTotalBorrow returns zero when there are no borrows. */
  @Test
  void testGetTotalBorrow_returnsZeroWhenNoBorrows() {
    // Arrange
    IBorrowRepository borrowRepositoryMock = Mockito.mock(IBorrowRepository.class);
    Mockito.when(borrowRepositoryMock.getTotalBorrow()).thenReturn(0);
    StaticServiceImpl staticService =
        createStaticServiceImplWithMocks(null, null, borrowRepositoryMock);

    // Act
    int result = staticService.getTotalBorrow();

    // Assert
    assertEquals(0, result);
  }

  /** Tests that getTotalLate returns the correct total number of late borrows. */
  @Test
  void testGetTotalLate_returnsCorrectTotal() {
    // Arrange
    IBorrowRepository borrowRepositoryMock = Mockito.mock(IBorrowRepository.class);
    Mockito.when(borrowRepositoryMock.getTotalLate()).thenReturn(5);
    StaticServiceImpl staticService =
        createStaticServiceImplWithMocks(null, null, borrowRepositoryMock);

    // Act
    int result = staticService.getTotalLate();

    // Assert
    assertEquals(5, result);
  }

  /** Tests that getTotalLate returns zero when there are no late borrows. */
  @Test
  void testGetTotalLate_returnsZeroWhenNoLateBorrows() {
    // Arrange
    IBorrowRepository borrowRepositoryMock = Mockito.mock(IBorrowRepository.class);
    Mockito.when(borrowRepositoryMock.getTotalLate()).thenReturn(0);
    StaticServiceImpl staticService =
        createStaticServiceImplWithMocks(null, null, borrowRepositoryMock);

    // Act
    int result = staticService.getTotalLate();

    // Assert
    assertEquals(0, result);
  }

  /** Tests that getTotalReturn returns the correct total number of returns. */
  @Test
  void testGetTotalReturn_returnsCorrectTotal() {
    // Arrange
    IBorrowRepository borrowRepositoryMock = Mockito.mock(IBorrowRepository.class);
    Mockito.when(borrowRepositoryMock.getTotalReturn()).thenReturn(25);
    StaticServiceImpl staticService =
        createStaticServiceImplWithMocks(null, null, borrowRepositoryMock);

    // Act
    int result = staticService.getTotalReturn();

    // Assert
    assertEquals(25, result);
  }

  /** Tests that getTotalReturn returns zero when there are no returns. */
  @Test
  void testGetTotalReturn_returnsZeroWhenNoReturns() {
    // Arrange
    IBorrowRepository borrowRepositoryMock = Mockito.mock(IBorrowRepository.class);
    Mockito.when(borrowRepositoryMock.getTotalReturn()).thenReturn(0);
    StaticServiceImpl staticService =
        createStaticServiceImplWithMocks(null, null, borrowRepositoryMock);

    // Act
    int result = staticService.getTotalReturn();

    // Assert
    assertEquals(0, result);
  }

  /**
   * Utility method to create a StaticServiceImpl instance with mocked repositories.
   *
   * @param bookRepository the mock IBookRepository to inject
   * @param readerRepository the mock IReaderRepository to inject
   * @param borrowRepository the mock IBorrowRepository to inject
   * @return a StaticServiceImpl instance with the provided mocks
   */
  private StaticServiceImpl createStaticServiceImplWithMocks(
      IBookRepository bookRepository,
      IReaderRepository readerRepository,
      IBorrowRepository borrowRepository) {
    StaticServiceImpl staticServiceImpl = new StaticServiceImpl();
    if (bookRepository != null) {
      setField(staticServiceImpl, "bookRepository", bookRepository);
    }
    if (readerRepository != null) {
      setField(staticServiceImpl, "readerRepository", readerRepository);
    }
    if (borrowRepository != null) {
      setField(staticServiceImpl, "borrowRepository", borrowRepository);
    }
    return staticServiceImpl;
  }

  /**
   * Utility method to set a private field on a target object.
   *
   * @param targetObject the object whose field is to be set
   * @param fieldName the name of the field to set
   * @param fieldValue the value to set the field to
   */
  private void setField(Object targetObject, String fieldName, Object fieldValue) {
    try {
      var field = StaticServiceImpl.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(targetObject, fieldValue);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}
