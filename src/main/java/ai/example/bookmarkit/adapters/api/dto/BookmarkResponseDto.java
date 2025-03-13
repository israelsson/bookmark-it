package ai.example.bookmarkit.adapters.api.dto;


import ai.example.bookmarkit.core.model.Bookmark;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class BookmarkResponseDto {

  int id;
  String name;
  String url;
  String tag;

  public static BookmarkResponseDto fromCoreModel(Bookmark bookmark) {

    return BookmarkResponseDto.builder()
        .id(bookmark.getId())
        .name(bookmark.getName())
        .url(bookmark.getUrl())
        .tag(bookmark.getTag())
        .build();
  }

}
