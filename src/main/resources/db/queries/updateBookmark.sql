UPDATE piktiv_bookmark_it.bookmarks
SET url = :url, name = :name, tags = :tags
WHERE bookmarks.id = :id;
