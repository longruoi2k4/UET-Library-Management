package com.example.library.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A utility class for interacting with the Google Books API to fetch book information.
 */
public class GoogleBooksAPI {

  private static final String GOOGLE_BOOKS_API_BASE_URL =
      "https://www.googleapis.com/books/v1/volumes?q=isbn:";
  private static final String GOOGLE_BOOKS_API_BASE_URL2 =
      "https://www.googleapis.com/books/v1/volumes?q=";

  /**
   * Fetches book information from Google Books API based on the given ISBN.
   *
   * @param isbn The ISBN of the book to search.
   * @return Formatted string containing book information.
   */
  public static String getBookInfoByIsbn(String isbn) {
    OkHttpClient client = new OkHttpClient();
    String url = GOOGLE_BOOKS_API_BASE_URL + isbn;

    Request request = new Request.Builder().url(url).build();

    try (Response response = client.newCall(request).execute()) {
      if (response.isSuccessful() && response.body() != null) {
        String responseBody = response.body().string();
        JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();

        if (json.has("items")) {
          JsonObject volumeInfo =
              json.getAsJsonArray("items").get(0).getAsJsonObject().getAsJsonObject("volumeInfo");
          return formatBookInfo(volumeInfo);
        } else {
          return "No book found for ISBN: " + isbn;
        }
      } else {
        return "Error: Unable to fetch book information. HTTP Code: " + response.code();
      }
    } catch (IOException e) {
      return "Error: " + e.getMessage();
    }
  }

  /**
   * Formats book information from the JSON response.
   *
   * @param volumeInfo The volumeInfo JSON object containing book details.
   * @return Formatted string containing book details.
   */
  private static String formatBookInfo(JsonObject volumeInfo) {
    String title = volumeInfo.has("title") ? volumeInfo.get("title").getAsString() : "N/A";
    String authors =
        volumeInfo.has("authors")
            ? volumeInfo.get("authors").toString().replaceAll("[\\[\\]\"]", "").trim()
            : "N/A";
    String language = volumeInfo.has("language") ? volumeInfo.get("language").getAsString() : "N/A";
    String categories =
        volumeInfo.has("categories")
            ? volumeInfo.get("categories").toString().replaceAll("[\\[\\]\"]", "").trim()
            : "N/A";
    String publishedDate =
        volumeInfo.has("publishedDate") ? volumeInfo.get("publishedDate").getAsString() : "N/A";
    String pageCount =
        volumeInfo.has("pageCount") ? volumeInfo.get("pageCount").getAsString() : "N/A";
    String description =
        volumeInfo.has("description") ? volumeInfo.get("description").getAsString() : "N/A";
    String thumbnail =
        volumeInfo.has("imageLinks") && volumeInfo.getAsJsonObject("imageLinks").has("thumbnail")
            ? volumeInfo.getAsJsonObject("imageLinks").get("thumbnail").getAsString()
            : "No Image";
    String isbn = "N/A"; // Giá trị mặc định nếu không có ISBN

    if (volumeInfo.has("industryIdentifiers")) {
      JsonArray identifiers = volumeInfo.getAsJsonArray("industryIdentifiers");
      for (int i = 0; i < identifiers.size(); i++) {
        JsonObject identifier = identifiers.get(i).getAsJsonObject();
        if (identifier.has("type")) {
          String type = identifier.get("type").getAsString();
          // Kiểm tra loại ISBN_13 và ISBN_10
          if (type.equals("ISBN_13")) {
            isbn = identifier.get("identifier").getAsString();
            break; // Dừng lại nếu tìm thấy ISBN-13
          } else if (type.equals("ISBN_10") && isbn.equals("N/A")) {
            // Nếu chưa có ISBN-13 thì lấy ISBN-10
            isbn = identifier.get("identifier").getAsString();
          }
        }
      }
    }
    // Trả về chuỗi thông tin sách, bao gồm cả ISBN
    return String.format(
        "Title: %s\nAuthors: %s\nLanguage: %s\nCategories: %s\nPublished Date: %s\nPage Count: %s\nDescription: %s\nThumbnail: %s\nISBN: %s",
        title,
        authors,
        language,
        categories,
        publishedDate,
        pageCount,
        description,
        thumbnail,
        isbn);
  }

  /**
   * Searches for books using a keyword and returns a list of up to 5 book titles.
   *
   * @param keyword The keyword to search for books.
   * @return List of book titles.
   */
  public static List<String> searchBooksByKeyword(String keyword) {
    OkHttpClient client = new OkHttpClient();
    String url = GOOGLE_BOOKS_API_BASE_URL2 + keyword;

    Request request = new Request.Builder().url(url).build();
    List<String> bookDetails = new ArrayList<>();
    try (Response response = client.newCall(request).execute()) {
      if (response.isSuccessful() && response.body() != null) {
        String responseBody = response.body().string();
        JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();

        if (json.has("items")) {
          JsonArray items = json.getAsJsonArray("items");

          for (int i = 0; i < Math.min(items.size(), 5); i++) {
            JsonObject volumeInfo = items.get(i).getAsJsonObject().getAsJsonObject("volumeInfo");
            String formattedInfo = formatBookInfo(volumeInfo);
            bookDetails.add(formattedInfo);
          }
        }
      }
    } catch (IOException e) {
      System.out.println("Error: " + e.getMessage());
    }
    return bookDetails.isEmpty() ? List.of("No books found for keyword: " + keyword) : bookDetails;
  }
}
