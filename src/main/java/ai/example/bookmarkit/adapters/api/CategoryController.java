package ai.example.bookmarkit.adapters.api;

import ai.example.bookmarkit.adapters.AdapterConstants;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(AdapterConstants.API_ROOT)
public class CategoryController {

  @GetMapping(
      path = AdapterConstants.CATEGORIES_PATH,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public List<String> getCategories() {

    return List.of("Tech", "Design", "Work", "Learning");
  }
}
