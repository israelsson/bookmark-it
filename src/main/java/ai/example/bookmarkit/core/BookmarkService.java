package ai.example.bookmarkit.core;

import ai.example.bookmarkit.core.model.Bookmark;
import ai.example.bookmarkit.core.ports.BookmarkRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class BookmarkService {

  BookmarkRepository bookmarkRepository;

  public List<Bookmark> getAllBookmarks() {
    return bookmarkRepository.getAllBookmarks();
  }

  public void createBookmark(Bookmark bookmark) {

    final var success = bookmarkRepository.createBookmark(bookmark);
    if (!success) {
      throw new BookmarkServiceException("Failed to create bookmark");
    }
  }

  public Bookmark getBookmarkById(int id) {

    final var bookmark = bookmarkRepository.getBookmarkById(id);

    return bookmark;

  }

  public void deleteBookmark(int id) {

    final var success = bookmarkRepository.deleteBookmark(id);
    if (!success) {
      throw new BookmarkServiceException("Failed to delete bookmark");
    }
  }

  public Bookmark updateBookmark(Bookmark bookmark) {

    final var success = bookmarkRepository.updateBookmark(bookmark);
    if (success) {

      return getBookmarkById(bookmark.getId());
    } else {
      throw new BookmarkServiceException("Failed to update bookmark");
    }
  }

  private static class BookmarkServiceException extends RuntimeException {

    BookmarkServiceException(String message) {
      super(message);
    }
  }
}
