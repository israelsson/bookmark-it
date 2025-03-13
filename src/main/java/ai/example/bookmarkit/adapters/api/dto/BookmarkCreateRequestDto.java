package ai.example.bookmarkit.adapters.api.dto;

import ai.example.bookmarkit.core.model.Bookmark;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookmarkCreateRequestDto {

  String name;
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
