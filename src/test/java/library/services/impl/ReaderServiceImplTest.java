package library.services.impl;

import com.example.library.models.Reader;
import com.example.library.repositories.IReaderRepository;
import com.example.library.services.impl.ReaderServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReaderServiceImplTest {

  @Test
  void testUpdateReader_Success() throws Exception {
    IReaderRepository repository = Mockito.mock(IReaderRepository.class);
    ReaderServiceImpl readerService = new ReaderServiceImpl(repository);

    Reader existingReader =
        Reader.builder().username("testUser").readerId("existingReaderId").build();

    Reader updatedReader =
        Reader.builder()
            .username("testUser")
            .readerId("existingReaderId")
            .readerEmail("newemail@example.com")
            .readerPhone("123456789")
            .build();

    when(repository.getReaderByUsername("testUser")).thenReturn(existingReader);
    when(repository.existsByEmailAndNotId(any(), any())).thenReturn(false);
    when(repository.existsByPhoneAndNotId(any(), any())).thenReturn(false);

    readerService.updateReader(updatedReader);

    verify(repository).save(updatedReader);
  }

  @Test
  void testUpdateReader_EmailExists() throws Exception {
    IReaderRepository repository = Mockito.mock(IReaderRepository.class);
    ReaderServiceImpl readerService = new ReaderServiceImpl(repository);

    Reader existingReader =
        Reader.builder().username("testUser").readerId("existingReaderId").build();

    Reader updatedReader =
        Reader.builder()
            .username("testUser")
            .readerId("existingReaderId")
            .readerEmail("newemail@example.com")
            .readerPhone("123456789")
            .build();

    when(repository.getReaderByUsername("testUser")).thenReturn(existingReader);
    when(repository.existsByEmailAndNotId("newemail@example.com", "existingReaderId"))
        .thenReturn(true);

    IllegalArgumentException thrown =
        Assertions.assertThrows(
            IllegalArgumentException.class, () -> readerService.updateReader(updatedReader));

    Assertions.assertEquals("Email is already exist", thrown.getMessage());
    verify(repository, never()).save(any());
  }

  @Test
  void testUpdateReader_PhoneExists() throws Exception {
    IReaderRepository repository = Mockito.mock(IReaderRepository.class);
    ReaderServiceImpl readerService = new ReaderServiceImpl(repository);

    Reader existingReader =
        Reader.builder().username("testUser").readerId("existingReaderId").build();

    Reader updatedReader =
        Reader.builder()
            .username("testUser")
            .readerId("existingReaderId")
            .readerEmail("newemail@example.com")
            .readerPhone("123456789")
            .build();

    when(repository.getReaderByUsername("testUser")).thenReturn(existingReader);
    when(repository.existsByEmailAndNotId(any(), any())).thenReturn(false);
    when(repository.existsByPhoneAndNotId("123456789", "existingReaderId")).thenReturn(true);

    IllegalArgumentException thrown =
        Assertions.assertThrows(
            IllegalArgumentException.class, () -> readerService.updateReader(updatedReader));

    Assertions.assertEquals("Phone number is already exist", thrown.getMessage());
    verify(repository, never()).save(any());
  }

  @Test
  void testUpdateReader_RepositoryException() {
    IReaderRepository repository = Mockito.mock(IReaderRepository.class);
    ReaderServiceImpl readerService = new ReaderServiceImpl(repository);

    Reader updatedReader = Reader.builder().username("testUser").build();

    when(repository.getReaderByUsername(any())).thenThrow(new RuntimeException("Repository error"));

    Assertions.assertThrows(Exception.class, () -> readerService.updateReader(updatedReader));
  }
}
