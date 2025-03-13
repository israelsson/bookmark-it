package ai.example.bookmarkit;

import ai.example.bookmarkit.config.LiquibaseRunner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class BookmarkIt {

  public static void main(String[] args) {

    if ("true".equalsIgnoreCase(System.getenv("MIGRATE_DATABASE"))) {
      runLiquibaseAndExit();
    } else {
      SpringApplication.run(BookmarkIt.class, args);
    }
  }

  public static void runLiquibaseAndExit() {
    log.info("Running liquibase migration");
    SpringApplication app = new SpringApplication(LiquibaseRunner.class);
    app.setAdditionalProfiles("liquibase");
    app.run().close();
  }
}
