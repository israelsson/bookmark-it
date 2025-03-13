import React, { useState, useEffect } from 'react';
import './App.css';
import BookmarkList from "./components/BookmarkList";
import TagFilter from './components/TagFilter';
import Button from '@mui/material/Button';
import Modal from '@mui/material/Modal';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select, { SelectChangeEvent } from '@mui/material/Select';
function App() {

  const [categories, setCategories] = useState([]);
  const [bookmarks, setBookmarks] = useState([]);
  const [selectedTag, setSelectedTag] = useState('');

  useEffect(() => {
    fetchCategories();
    fetchBookmarks();
  }, []);

  const fetchCategories = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/categories');
      const data = await response.json();
      setCategories(data);
    } catch (error) {
      console.error('Error fetching categories:', error);
    }
  };

  const fetchBookmarks = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/bookmarks');
      const data = await response.json();
      setBookmarks(data);
    } catch (error) {
      console.error('Error fetching bookmarks:', error);
    }
  };

  const [open, setOpen] = useState(false);
  const [name, setName] = useState('');
  const [url, setUrl] = useState('');
  const [tag, setTag] = useState('');

  const handleChange = (event) => {
    setTag(event.target.value)
  };
  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);

  const handleSubmit = async (event) => {

    event.preventDefault();
    const urlPattern = new RegExp('^https?:\\/\\/.*\\..*');

    if (!urlPattern.test(url)) {
      alert('Please enter a valid URL.');
      return;
    }

    if (!urlPattern.test(url)) {
      alert('Please enter a valid URL.');
      return;
    }

    const newBookmark = { name, url, tag };

    try {
      const response = await fetch('http://localhost:8080/api/bookmark/create', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(newBookmark),
      });

      if (response.ok) {
        setName('');
        setUrl('');
        setTag('');
        handleClose();
        fetchBookmarks();
      } else {
        console.error('Error creating bookmark:', response.statusText);
      }
    } catch (error) {
      console.error('Error creating bookmark:', error);
    }
  };

  return (
      <div className="App">
        <header className="App-header">
          <h1>My bookmarks</h1>
          <TagFilter categories={categories} selectedTag={selectedTag} setSelectedTag={setSelectedTag} />
          <BookmarkList bookmarks={bookmarks} setBookmarks={setBookmarks} fetchBookmarks={fetchBookmarks} selectedTag={selectedTag}  />
        </header>
        <Button onClick={handleOpen} variant="contained">Add new bookmark</Button>
        <Modal
            open={open}
            onClose={handleClose}
            aria-labelledby="modal-title"
            aria-describedby="modal-title"
        >
          <Box className="modal">
            <Typography id="modal-title" variant="h6" component="h2">
              Add a new bookmark
            </Typography>
            <form onSubmit={handleSubmit}>
              <TextField
                  label="Name"
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                  fullWidth
                  margin="normal"
                  required
              />
              <TextField
                  label="URL"
                  value={url}
                  onChange={(e) => setUrl(e.target.value)}
                  fullWidth
                  margin="normal"
                  required
              />
              <FormControl fullWidth>
                <InputLabel id="new-bookmark-tag-label">Tag</InputLabel>
                <Select
                    id="tag"
                    value={tag}
                    label="Tag"
                    onChange={handleChange}
                >
                  {categories.map(category => (
                      <MenuItem value={category}>{category}</MenuItem>
                  ))}
                </Select>
              </FormControl>
              <Button type="submit" variant="contained" color="primary">
                Add Bookmark
              </Button>
            </form>
          </Box>
        </Modal>
      </div>
  );
}

export default App;
