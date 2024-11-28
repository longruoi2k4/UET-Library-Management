package library.services.impl;

import com.example.library.models.Borrow;
import com.example.library.repositories.IBookRepository;
import com.example.library.repositories.IBorrowRepository;
import com.example.library.repositories.IReaderRepository;
import com.example.library.services.impl.BorrowServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class BorrowServiceImplTest {

  @Test
  void BorrowServiceImpl() {
    // Arrange
    IBorrowRepository borrowRepository = mock(IBorrowRepository.class);
    IBookRepository bookRepository = mock(IBookRepository.class);
    IReaderRepository readerRepository = mock(IReaderRepository.class);
    BorrowServiceImpl borrowService =
        new BorrowServiceImpl(borrowRepository, bookRepository, readerRepository);
    ObservableList<Borrow> borrows = FXCollections.observableArrayList();
    Borrow borrow = new Borrow();
    borrow.setStatus(" ");
    borrows.add(borrow);

    when(borrowRepository.getBorrowByReaderId("readerId")).thenReturn(borrows);

    // Act
    ObservableList<Borrow> result = borrowService.getBorrowByReaderId("readerId");

    // Assert
    assertTrue(result.contains(borrow));
  }

  @Test
  void testGetAllBookBorrowed() {
    // Arrange
    IBorrowRepository borrowRepository = mock(IBorrowRepository.class);
    IBookRepository bookRepository = mock(IBookRepository.class);
    IReaderRepository readerRepository = mock(IReaderRepository.class);
    BorrowServiceImpl borrowService =
        new BorrowServiceImpl(borrowRepository, bookRepository, readerRepository);
    ObservableList<Borrow> borrows = FXCollections.observableArrayList();
    Borrow borrow = new Borrow();
    borrow.setStatus(" ");
    borrows.add(borrow);

    when(borrowRepository.getAllBookBorrowed()).thenReturn(borrows);

    // Act
    ObservableList<Borrow> result = borrowService.getAllBookBorrowed();

    // Assert
    assertTrue(result.contains(borrow));
  }

  @Test
  void testReturnBook() throws Exception {
    // Arrange
    IBorrowRepository borrowRepository = mock(IBorrowRepository.class);
    IBookRepository bookRepository = mock(IBookRepository.class);
    IReaderRepository readerRepository = mock(IReaderRepository.class);
    BorrowServiceImpl borrowService =
        new BorrowServiceImpl(borrowRepository, bookRepository, readerRepository);
    Borrow borrow = new Borrow();
    borrow.setBookId("bookId");
    borrow.setReaderId("readerId");

    when(borrowRepository.isAlreadyRequest("readerId", "bookId")).thenReturn(true);

    // Act
    borrowService.returnBook(borrow);

    // Assert
    verify(borrowRepository).returnBook(borrow);
    verify(bookRepository).increaseQuantity("bookId");
  }

  @Test
  void testBorrowBook() {
    // Arrange
    IBorrowRepository borrowRepository = mock(IBorrowRepository.class);
    IBookRepository bookRepository = mock(IBookRepository.class);
    IReaderRepository readerRepository = mock(IReaderRepository.class);
    BorrowServiceImpl borrowService =
        new BorrowServiceImpl(borrowRepository, bookRepository, readerRepository);
    Borrow borrow = new Borrow();

    // Act
    borrowService.borrowBook(borrow);

    // Assert
    verify(borrowRepository).save(borrow);
    verify(bookRepository).decreaseQuantity(borrow.getBookName());
  }

  @Test
  void testGetTotalBorrowByReaderId() {
    // Arrange
    IBorrowRepository borrowRepository = mock(IBorrowRepository.class);
    IBookRepository bookRepository = mock(IBookRepository.class);
    IReaderRepository readerRepository = mock(IReaderRepository.class);
    BorrowServiceImpl borrowService =
        new BorrowServiceImpl(borrowRepository, bookRepository, readerRepository);

    when(borrowRepository.getTotalBorrowByReaderId("readerId")).thenReturn(5);

    // Act
    int totalBorrows = borrowService.getTotalBorrowByReaderId("readerId");

    // Assert
    assertEquals(5, totalBorrows);
  }

  @Test
  void testIsReaderLate() {
    // Arrange
    IBorrowRepository borrowRepository = mock(IBorrowRepository.class);
    IBookRepository bookRepository = mock(IBookRepository.class);
    IReaderRepository readerRepository = mock(IReaderRepository.class);
    BorrowServiceImpl borrowService =
        new BorrowServiceImpl(borrowRepository, bookRepository, readerRepository);

    when(borrowRepository.isReaderLate("readerId")).thenReturn(true);

    // Act
    boolean isLate = borrowService.isReaderLate("readerId");

    // Assert
    assertTrue(isLate);
  }

  @Test
  void testRequestBorrow() throws Exception {
    // Arrange
    IBorrowRepository borrowRepository = mock(IBorrowRepository.class);
    IBookRepository bookRepository = mock(IBookRepository.class);
    IReaderRepository readerRepository = mock(IReaderRepository.class);
    BorrowServiceImpl borrowService =
        new BorrowServiceImpl(borrowRepository, bookRepository, readerRepository);

    String bookId = "bookId";
    LocalDate returnDate = LocalDate.now();
    when(borrowRepository.isAlreadyRequest(anyString(), eq(bookId))).thenReturn(false);

    // Act
    borrowService.requestBorrow(bookId, returnDate);

    // Assert
    verify(borrowRepository).requestBorrow(bookId, returnDate);
  }

  @Test
  void testGetAllRequestByReaderId() {
    // Arrange
    IBorrowRepository borrowRepository = mock(IBorrowRepository.class);
    IBookRepository bookRepository = mock(IBookRepository.class);
    IReaderRepository readerRepository = mock(IReaderRepository.class);
    BorrowServiceImpl borrowService =
        new BorrowServiceImpl(borrowRepository, bookRepository, readerRepository);
    ObservableList<Borrow> borrows = FXCollections.observableArrayList();
    Borrow borrow = new Borrow();
    borrow.setReaderId("readerId");
    borrow.setStatus("REQUEST");
    borrows.add(borrow);

    when(borrowRepository.getAllBookBorrowed()).thenReturn(borrows);

    // Act
    ObservableList<Borrow> result = borrowService.getAllRequestByReaderId("readerId");

    // Assert
    assertTrue(result.contains(borrow));
  }

  @Test
  void testGetAllRequestBorrow() {
    // Arrange
    IBorrowRepository borrowRepository = mock(IBorrowRepository.class);
    IBookRepository bookRepository = mock(IBookRepository.class);
    IReaderRepository readerRepository = mock(IReaderRepository.class);
    BorrowServiceImpl borrowService =
        new BorrowServiceImpl(borrowRepository, bookRepository, readerRepository);
    ObservableList<Borrow> borrows = FXCollections.observableArrayList();
    Borrow borrow = new Borrow();
    borrow.setStatus("REQUEST");
    borrows.add(borrow);

    when(borrowRepository.getAllBookBorrowed()).thenReturn(borrows);

    // Act
    ObservableList<Borrow> result = borrowService.getAllRequestBorrow();

    // Assert
    assertTrue(result.contains(borrow));
  }

  @Test
  void testDeleteRequest() {
    // Arrange
    IBorrowRepository borrowRepository = mock(IBorrowRepository.class);
    IBookRepository bookRepository = mock(IBookRepository.class);
    IReaderRepository readerRepository = mock(IReaderRepository.class);
    BorrowServiceImpl borrowService =
        new BorrowServiceImpl(borrowRepository, bookRepository, readerRepository);
    List<String> borrowIds = List.of("1", "2");

    // Act
    borrowService.deleteRequest(borrowIds);

    // Assert
    verify(borrowRepository).deleteRequest(borrowIds);
  }

  @Test
  void testGetAllEmailByBorrowIds() {
    // Arrange
    IBorrowRepository borrowRepository = mock(IBorrowRepository.class);
    IBookRepository bookRepository = mock(IBookRepository.class);
    IReaderRepository readerRepository = mock(IReaderRepository.class);
    BorrowServiceImpl borrowService =
        new BorrowServiceImpl(borrowRepository, bookRepository, readerRepository);
    List<String> borrowIds = List.of("1", "2");
    List<String> emails = List.of("email1", "email2");

    when(borrowRepository.getAllEmailByBorrowIds(borrowIds)).thenReturn(emails);

    // Act
    List<String> result = borrowService.getAllEmailByBorrowIds(borrowIds);

    // Assert
    assertEquals(emails, result);
  }

  @Test
  void testGetAllEmailWithMessagesByBorrowIds() {
    // Arrange
    IBorrowRepository borrowRepository = mock(IBorrowRepository.class);
    IBookRepository bookRepository = mock(IBookRepository.class);
    IReaderRepository readerRepository = mock(IReaderRepository.class);
    BorrowServiceImpl borrowService =
        new BorrowServiceImpl(borrowRepository, bookRepository, readerRepository);
    List<String> borrowIds = List.of("1", "2");
    Map<String, String> emailMessages = Map.of("email1", "message1", "email2", "message2");

    when(borrowRepository.getAllEmailWithMessagesByBorrowIds(borrowIds)).thenReturn(emailMessages);

    // Act
    Map<String, String> result = borrowService.getAllEmailWithMessagesByBorrowIds(borrowIds);

    // Assert
    assertEquals(emailMessages, result);
  }

  @Test
  void testDeclineRequest() {
    // Arrange
    IBorrowRepository borrowRepository = mock(IBorrowRepository.class);
    IBookRepository bookRepository = mock(IBookRepository.class);
    IReaderRepository readerRepository = mock(IReaderRepository.class);
    BorrowServiceImpl borrowService =
        new BorrowServiceImpl(borrowRepository, bookRepository, readerRepository);
    List<String> borrowIds = List.of("1", "2");

    // Act
    borrowService.declineRequest(borrowIds);

    // Assert
    verify(borrowRepository).declineRequest(borrowIds);
  }

  @Test
  void testUpdateBorrowDate() {
    // Arrange
    IBorrowRepository borrowRepository = mock(IBorrowRepository.class);
    IBookRepository bookRepository = mock(IBookRepository.class);
    IReaderRepository readerRepository = mock(IReaderRepository.class);
    BorrowServiceImpl borrowService =
        new BorrowServiceImpl(borrowRepository, bookRepository, readerRepository);
    List<String> borrowIds = List.of("1", "2");

    // Act
    borrowService.updateBorrowDate(borrowIds);

    // Assert
    verify(borrowRepository).updateBorrowDate(borrowIds);
  }

  @Test
  void testApproveRequest() {
    // Arrange
    IBorrowRepository borrowRepository = mock(IBorrowRepository.class);
    IBookRepository bookRepository = mock(IBookRepository.class);
    IReaderRepository readerRepository = mock(IReaderRepository.class);
    BorrowServiceImpl borrowService =
        new BorrowServiceImpl(borrowRepository, bookRepository, readerRepository);
    List<String> borrowIds = List.of("1", "2");
    List<String> bookIds = List.of("book1", "book2");

    when(borrowRepository.getAllBookIdByBorrowId(borrowIds)).thenReturn(bookIds);

    // Act
    borrowService.approveRequest(borrowIds);

    // Assert
    verify(borrowRepository).approveRequest(borrowIds);
    verify(borrowRepository).updateBorrowDate(borrowIds);

    ArgumentCaptor<String> bookIdCaptor = ArgumentCaptor.forClass(String.class);
    verify(bookRepository, times(2)).decreaseQuantity(bookIdCaptor.capture());

    assertEquals(bookIds, bookIdCaptor.getAllValues());
  }
}
