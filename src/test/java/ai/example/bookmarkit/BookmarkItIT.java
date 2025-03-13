package ai.example.bookmarkit;

import ai.example.bookmarkit.adapters.AdapterConstants;
import ai.example.bookmarkit.adapters.api.dto.BookmarkCreateRequestDto;
import ai.example.bookmarkit.adapters.api.dto.BookmarkMutationRequestDto;
import ai.example.bookmarkit.core.ports.BookmarkRepository;
import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

@Slf4j
@ActiveProfiles("test")
@Testcontainers
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class BookmarkItIT {

  private static final String SCHEMA_NAME = "piktiv_bookmark_it";
  private static final String TABLE_NAME = "bookmarks";

  @LocalServerPort
  private int port;

  @Autowired
  private BookmarkRepository bookmarkRepository;

  @Autowired
  private NamedParameterJdbcTemplate jdbcTemplate;


  @BeforeEach
  void setup() {
    truncateTable();
  }

  @Test
  void testWeCanFetchCategories() {

    final var response = RestAssured.given()
        .port(port)
        .get("/api/categories");

    Assertions.assertThat(response.statusCode()).isEqualTo(200);

    final var responseBody = response.getBody().as(JsonNode.class);
    Assertions.assertThat(responseBody.size()).isEqualTo(4);
  }

  @Test
  void testWeCanInsertNewBookmark() {

    final var request = BookmarkCreateRequestDto.builder()
        .name("test")
        .url("http://example.se")
        .tags("Tech")
        .build();

    final var response = RestAssured
        .given()
        .port(port)
        .body(request)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .post(AdapterConstants.API_ROOT + AdapterConstants.BOOKMARK_PATH + AdapterConstants.CREATE_PATH);

    Assertions.assertThat(response.statusCode()).isEqualTo(204);
  }

  @Test
  void testWeCanUpdateExistingBookmark() {

    final var bookmark = BookmarkCreateRequestDto.builder()
        .name("Before Update")
        .url("http://example.se")
        .tags("Tech")
        .build();

    bookmarkRepository.createBookmark(bookmark.toCoreModel());

    final var bookmarks = bookmarkRepository.getAllBookmarks();
    Assertions.assertThat(bookmarks).hasSize(1);
    final var insertedBookmark = bookmarks.getFirst();
    Assertions.assertThat(insertedBookmark.getName()).isEqualTo("Before Update");

    final var mutationRequest = BookmarkMutationRequestDto.builder()
        .id(insertedBookmark.getId())
        .name("After Update")
        .url(insertedBookmark.getUrl())
        .tags(insertedBookmark.getTag())
        .build();

    final var response = RestAssured
        .given()
        .port(port)
        .body(mutationRequest)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .put(AdapterConstants.API_ROOT + AdapterConstants.BOOKMARK_PATH + AdapterConstants.UPDATE_PATH);

    Assertions.assertThat(response.statusCode()).isEqualTo(200);

    final var responseBody = response.getBody().as(JsonNode.class);
    Assertions.assertThat(responseBody.get("name").asText()).isEqualTo("After Update");
  }

  @Test
  void testWeCanDeleteExistingBookmark() {

    final var bookmark = BookmarkCreateRequestDto.builder()
        .name("aName")
        .url("http://example.se")
        .tags("Tech")
        .build();

    bookmarkRepository.createBookmark(bookmark.toCoreModel());
    final var bookmarks = bookmarkRepository.getAllBookmarks();
    Assertions.assertThat(bookmarks).hasSize(1);
    final var insertedBookmark = bookmarks.getFirst();

    final var response = RestAssured
        .given()
        .port(port)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .delete(AdapterConstants.API_ROOT + AdapterConstants.BOOKMARK_PATH + AdapterConstants.DELETE_PATH + "/" + insertedBookmark.getId());

    Assertions.assertThat(response.statusCode()).isEqualTo(204);

    final var remainingBookmarks = bookmarkRepository.getAllBookmarks();
    Assertions.assertThat(remainingBookmarks).isEmpty();
  }

  @Test
  void testWeHaveValidationOnUrl() {

    final var request = BookmarkCreateRequestDto.builder()
        .name("test")
        .url("notaccepted")
        .tags("Tech")
        .build();

    final var errorResponse = RestAssured
        .given()
        .port(port)
        .body(request)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .post(AdapterConstants.API_ROOT + AdapterConstants.BOOKMARK_PATH + AdapterConstants.CREATE_PATH);

    Assertions.assertThat(errorResponse.statusCode()).isEqualTo(400);

    request.setUrl("http://accepted.se");

    final var successResponse = RestAssured
        .given()
        .port(port)
        .body(request)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .post(AdapterConstants.API_ROOT + AdapterConstants.BOOKMARK_PATH + AdapterConstants.CREATE_PATH);

    Assertions.assertThat(successResponse.statusCode()).isEqualTo(204);
  }

  private void truncateTable() {

    log.info("Table: {} will be truncating", TABLE_NAME);
    try {
      jdbcTemplate.getJdbcOperations().execute(
          "TRUNCATE TABLE %s.%s RESTART IDENTITY CASCADE;".formatted(SCHEMA_NAME, TABLE_NAME));
    } catch (Exception ex) {
      log.error("Could not truncate table '%s'".formatted(TABLE_NAME), ex);
    }
  }
}
