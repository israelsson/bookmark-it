package ai.example.bookmarkit.adapters.api;

import ai.example.bookmarkit.adapters.AdapterConstants;
import ai.example.bookmarkit.adapters.api.dto.BookmarkCreateRequestDto;
import ai.example.bookmarkit.adapters.api.dto.BookmarkMutationRequestDto;
import ai.example.bookmarkit.adapters.api.dto.BookmarkResponseDto;
import ai.example.bookmarkit.core.BookmarkService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(AdapterConstants.API_ROOT)
public class BookmarkItController {

  private final BookmarkService bookmarkService;

  @GetMapping(
      path = AdapterConstants.BOOKMARKS_PATH,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<List<BookmarkResponseDto>> getBookmarks() {

    log.info("Incoming request to get all bookmarks");
    final var bookmarks = bookmarkService.getAllBookmarks();
    return ResponseEntity.ok(bookmarks.stream()
        .map(BookmarkResponseDto::fromCoreModel)
        .toList());
  }

  @GetMapping(
      path = AdapterConstants.BOOKMARK_PATH + AdapterConstants.ID_PARAM,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<BookmarkResponseDto> getBookmarkById(@PathVariable final int id) {

    log.info("Incoming request to get bookmark by ID: {}", id);
    final var bookmark = bookmarkService.getBookmarkById(id);
    return ResponseEntity.ok(BookmarkResponseDto.fromCoreModel(bookmark));
  }

  @PostMapping(
      path = AdapterConstants.BOOKMARK_PATH + AdapterConstants.CREATE_PATH,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Void> createBookmark(@Valid @RequestBody BookmarkCreateRequestDto requestDto) {

    log.info("Incoming request to create new bookmark");
    bookmarkService.createBookmark(requestDto.toCoreModel());
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping(
      path = AdapterConstants.BOOKMARK_PATH + AdapterConstants.DELETE_PATH + AdapterConstants.ID_PARAM
  )
  public ResponseEntity<Void> deleteBookmark(@PathVariable final int id) {

    log.info("Incoming request to delete bookmark by ID: {}", id);
    bookmarkService.deleteBookmark(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping(
      path = AdapterConstants.BOOKMARK_PATH + AdapterConstants.UPDATE_PATH,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<BookmarkResponseDto> updateBookmark(@RequestBody BookmarkMutationRequestDto requestDto) {

    log.info("Incoming request to update bookmark by ID: {}", requestDto.getId());
    final var updatedBookmark = bookmarkService.updateBookmark(requestDto.toCoreModel());
    return ResponseEntity.ok(BookmarkResponseDto.fromCoreModel(updatedBookmark));
  }
}
