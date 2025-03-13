package ai.example.bookmarkit.adapters.spi;

import ai.example.bookmarkit.core.model.Bookmark;
import ai.example.bookmarkit.core.ports.BookmarkRepository;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.FileCopyUtils;

@Slf4j
@AllArgsConstructor
public class PostgresBookmarkRepository implements BookmarkRepository {

  private static final String GET_ALL_BOOKMARKS = readFileContent("getAllBookmarks.sql");
  private static final String INSERT_BOOKMARK = readFileContent("insertBookmark.sql");
  private static final String GET_BOOKMARK_BY_ID = readFileContent("getBookmarkById.sql");
  private static final String DELETE_BOOKMARK_BY_ID = readFileContent("deleteBookmark.sql");
  private static final String UPDATE_BOOKMARK_BY_ID = readFileContent("updateBookmark.sql");

  private NamedParameterJdbcTemplate jdbcTemplate;

  @Override
  public List<Bookmark> getAllBookmarks() {

    return jdbcTemplate.query(GET_ALL_BOOKMARKS, new BookMarkRowMapper());
  }

  @Override
  public boolean createBookmark(Bookmark bookmark) {

    Map<String, Object> parameters = new HashMap<>();
    parameters.put("id", bookmark.getId());
    parameters.put("url", bookmark.getUrl());
    parameters.put("name", bookmark.getName());
    parameters.put("tags", bookmark.getTag());

    final var effectedRows = jdbcTemplate.update(INSERT_BOOKMARK, parameters);

    return effectedRows == 1;
  }

  @Override
  public Bookmark getBookmarkById(int id) {

    Map<String, Object> parameters = new HashMap<>();
    parameters.put("id", id);

    log.info("Executing query: {} with parameters: {}", GET_BOOKMARK_BY_ID, parameters);

    try {
      return jdbcTemplate.query(GET_BOOKMARK_BY_ID, parameters, new BookMarkRowMapper()).getFirst();
    } catch (Exception e) {
      log.error("Error fetching bookmark by id: {}", id, e);
      return null;
    }
  }

  @Override
  public boolean deleteBookmark(int id) {

    Map<String, Object> parameters = new HashMap<>();
    parameters.put("id", id);

    log.info("Executing query: {} with parameters: {}", DELETE_BOOKMARK_BY_ID, parameters);

    try {
      final var effectedRows = jdbcTemplate.update(DELETE_BOOKMARK_BY_ID, parameters);
      return effectedRows == 1;
    } catch (Exception e) {
      log.error("Error deleting bookmark by id: {}", id, e);
      return false;
    }
  }

  @Override
  public boolean updateBookmark(Bookmark bookmark) {

    Map<String, Object> parameters = new HashMap<>();
    parameters.put("id", bookmark.getId());
    parameters.put("name", bookmark.getName());
    parameters.put("url", bookmark.getUrl());
    parameters.put("tags", bookmark.getTag());

    log.info("Executing query: {} with parameters: {}", UPDATE_BOOKMARK_BY_ID, parameters);

    try {
      final var effectedRows = jdbcTemplate.update(UPDATE_BOOKMARK_BY_ID, parameters);
      return effectedRows == 1;
    } catch (Exception e) {
      log.error("Error deleting bookmark by id: {}", bookmark.getId(), e);
      return false;
    }
  }

  private static String readFileContent(String sqlFile) {
    var resource = new ClassPathResource(String.format("/db/queries/%s", sqlFile));
    try (var reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
      return FileCopyUtils.copyToString(reader);
    } catch (IOException e) {
      throw new FailedToReadSqlFileException(e);
    }
  }

  private static class FailedToReadSqlFileException extends RuntimeException {

    FailedToReadSqlFileException(Exception e) {
      super(e);
    }
  }

  private static class BookMarkRowMapper implements RowMapper<Bookmark> {
    @Override
    public Bookmark mapRow(ResultSet rs, int rowNum) throws SQLException {

      return Bookmark.builder()
          .id(rs.getInt("id"))
          .url(rs.getString("url"))
          .name(rs.getString("name"))
          .tag(rs.getString("tags"))
          .build();
    }
  }
}
