package ai.example.bookmarkit.adapters.api.dto;

import ai.example.bookmarkit.core.model.Bookmark;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookmarkCreateRequestDto {

  String name;
  @Pattern(
      regexp = "^https?:\\/\\/.*\\..*",
      message = "Please enter a valid URL"
  )
  String url;
  @JsonProperty("tag")
  String tags;

  public Bookmark toCoreModel() {

    return Bookmark.builder()
        .name(this.name)
        .url(this.url)
        .tag(this.tags)
        .build();
  }
}
