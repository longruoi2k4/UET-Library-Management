package com.example.library.services.impl;

import com.example.library.repositories.*;
import com.example.library.repositories.impl.BookRepositoryImpl;
import com.example.library.repositories.impl.BorrowRepositoryImpl;
import com.example.library.repositories.impl.ReaderRepositoryImpl;
import com.example.library.services.IStaticService;

import java.util.List;

/**
 * An implementation of the IStaticService interface that provides
 * methods to retrieve statistical information about books, readers,
 * borrowing transactions, and returns in a library system. It uses
 * repositories to manage and access the underlying data.
 */
public class StaticServiceImpl implements IStaticService {

  private final IReaderRepository readerRepository;
  private final IBorrowRepository borrowRepository;
  private final IBookRepository bookRepository;

  /**
   * Constructs a new instance of StaticServiceImpl, initializing its
   * internal repositories for readers, borrowing transactions, and books.
   * This implementation creates new instances of ReaderRepositoryImpl,
   * BorrowRepositoryImpl, and BookRepositoryImpl to manage the respective data.
   */
  public StaticServiceImpl() {
    readerRepository = new ReaderRepositoryImpl();
    borrowRepository = new BorrowRepositoryImpl();
    bookRepository = new BookRepositoryImpl();
  }

  @Override
  public int getTotalBook() {
    return bookRepository.getTotalBook();
  }

  @Override
  public int getTotalReader() {
    return readerRepository.getTotalReader();
  }

  @Override
  public int getTotalBorrow() {
    return borrowRepository.getTotalBorrow();
  }

  @Override
  public int getTotalLate() {
    return borrowRepository.getTotalLate();
  }

  @Override
  public int getTotalReturn() {
    return borrowRepository.getTotalReturn();
  }

  @Override
  public List<String> getTop3MostBorrowedBooks() { return borrowRepository.getTop3MostBorrowedBooks();}
}
