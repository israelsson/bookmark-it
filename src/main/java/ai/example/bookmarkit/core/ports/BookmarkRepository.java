package ai.example.bookmarkit.core.ports;

import ai.example.bookmarkit.core.model.Bookmark;
import java.util.List;

public interface BookmarkRepository {
  List<Bookmark> getAllBookmarks();

  Bookmark getBookmarkById(int id);

  boolean createBookmark(Bookmark bookmark);

  boolean deleteBookmark(int id);

  boolean updateBookmark(Bookmark bookmark);

}
