package ai.example.bookmarkit.core.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Bookmark {
  int id;
  String name;
  String url;
  String tag;
}
