import React, { useState, useEffect, useCallback } from 'react';
import './BookmarkList.css';
import Button from '@mui/material/Button';
import Modal from '@mui/material/Modal';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import FormControl from "@mui/material/FormControl";
import InputLabel from "@mui/material/InputLabel";
import Select from "@mui/material/Select";
import MenuItem from "@mui/material/MenuItem";
function BookmarkList({ bookmarks, setBookmarks, fetchBookmarks, selectedTag }) {

  const [editOpen, setEditOpen] = useState(false);
  const [currentBookmark, setCurrentBookmark] = useState(null);
  const [categories, setCategories] = useState([]);
  const fetchCategories = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/categories');
      const data = await response.json();
      setCategories(data);
    } catch (error) {
      console.error('Error fetching categories:', error);
    }
  };

  const memoizedFetchBookmarks = useCallback(fetchBookmarks, []);

  useEffect(() => {
    const fetchData = async () => {
      await memoizedFetchBookmarks();
      const response = await fetch('http://localhost:8080/api/bookmarks');
      const data = await response.json();
      setBookmarks(data);
    };
    fetchData();
    fetchCategories();
  }, [memoizedFetchBookmarks, setBookmarks]);

  const handleEditClick = (bookmark) => {
    setCurrentBookmark(bookmark);
    setEditOpen(true);
  };

  const handleEditClose = () => {
    setEditOpen(false);
    setCurrentBookmark(null);
  };

  const handleEditChange = (event) => {
    const { name, value } = event.target;
    setCurrentBookmark({ ...currentBookmark, [name]: value });
  };

  const handleEditTagChange = (event) => {
    setCurrentBookmark({ ...currentBookmark, tag: event.target.value });
  };

  const handleUpdate = async (event) => {
    event.preventDefault();
    try {
      const response = await fetch(`http://localhost:8080/api/bookmark/update`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(currentBookmark),
      });

      if (response.ok) {
        await fetchBookmarks();
        handleEditClose();
      } else {
        console.error('Error updating bookmark:', response.statusText);
      }
    } catch (error) {
      console.error('Error updating bookmark:', error);
    }
  };

  const handleRemoveClick = async (id) => {

    try {
      const response = await fetch(`http://localhost:8080/api/bookmark/delete/${id}`, {
        method: 'DELETE',
      });

      if (response.ok) {
        setBookmarks(bookmarks.filter(bookmark => bookmark.id !== id));
      } else {
        console.error('Error deleting bookmark:', response.statusText);
      }
    } catch (error) {
      console.error('Error deleting bookmark:', error);
    }
  };

  const filteredBookmarks = selectedTag
      ? bookmarks.filter((bookmark) => bookmark.tag === selectedTag)
      : bookmarks;

  return (
      <div>
      <ul className="bookmarks">
        {filteredBookmarks.map(bookmark => (
            <li key={`bookmark-${bookmark.id}`}>
              <a href={bookmark.url}>{bookmark.name}</a> - {bookmark.tag}
              <Button onClick={() => handleEditClick(bookmark)}>Edit</Button>
              <Button key={`remove-${bookmark.id}`} onClick={() => handleRemoveClick(bookmark.id)}>Remove</Button>
            </li>
        ))}
      </ul>
      <Modal open={editOpen} onClose={handleEditClose}>
        <Box className="modal">
          <Typography variant="h6" component="h2">
            Edit Bookmark
          </Typography>
          {currentBookmark && (
            <form onSubmit={handleUpdate}>
              <TextField
                label="Name"
                name="name"
                value={currentBookmark.name}
                onChange={handleEditChange}
                fullWidth
                margin="normal"
                required
              />
              <TextField
                label="URL"
                name="url"
                value={currentBookmark.url}
                onChange={handleEditChange}
                fullWidth
                margin="normal"
                required
              />
              <FormControl fullWidth>
                <InputLabel>Tag</InputLabel>
                <Select
                    id="tag"
                    value={currentBookmark.tag}
                    label="Tag"
                    onChange={handleEditTagChange}
                >
                  {categories.map(category => (
                      <MenuItem value={category}>{category}</MenuItem>
                  ))}
                </Select>
              </FormControl>
              <Button type="submit" variant="contained" color="primary">
                Update Bookmark
              </Button>
            </form>
        )}
      </Box>
    </Modal>
      </div>)
}

export default BookmarkList;
