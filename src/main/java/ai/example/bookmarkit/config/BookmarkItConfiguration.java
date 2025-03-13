package ai.example.bookmarkit.config;

import ai.example.bookmarkit.adapters.spi.PostgresBookmarkRepository;
import ai.example.bookmarkit.core.BookmarkService;
import ai.example.bookmarkit.core.ports.BookmarkRepository;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;


@Configuration
public class BookmarkItConfiguration {

  @Bean
  public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
    return new NamedParameterJdbcTemplate(dataSource);
  }

  @Bean
  BookmarkRepository bookmarkRepository(NamedParameterJdbcTemplate jdbcTemplate) {
    return new PostgresBookmarkRepository(jdbcTemplate);
  }

  @Bean
  BookmarkService bookmarkService(BookmarkRepository bookmarkRepository) {
    return new BookmarkService(bookmarkRepository);
  }
}
