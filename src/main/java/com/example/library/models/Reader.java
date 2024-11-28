package com.example.library.models;

import java.time.LocalDate;
import lombok.*;

/**
 * The Reader class represents a library reader.
 * It contains information related to the reader such as the reader's unique
 * identifier, name, email, phone number, date of birth, address, blockage status,
 * username, and user ID.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Reader {
  private String readerId;
  private String readerName;
  private String readerEmail;
  private String readerPhone;
  private LocalDate readerDOB;
  private String readerAddress;
  private boolean isBlocked;
  private String username;
  private int userId;
}
