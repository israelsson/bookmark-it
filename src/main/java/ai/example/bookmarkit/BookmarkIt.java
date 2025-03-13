package ai.example.bookmarkit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class BookmarkIt {

  public static void main(String[] args) {

    SpringApplication.run(BookmarkIt.class, args);
  }
}
