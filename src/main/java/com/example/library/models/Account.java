package com.example.library.models;

import lombok.*;

/**
 * The Account class represents a user account in the system.
 * It contains the username, password, and role associated with the account.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Account {
  private String username;
  private String password;
  private String role;
}
