package com.example.library.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Author class represents an author in the library system.
 * It contains the author's unique identifier and name.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Author {
  private String authorId;
  private String authorName;
}
