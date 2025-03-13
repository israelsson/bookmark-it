import React from 'react';
import FormControl from '@mui/material/FormControl';
import InputLabel from '@mui/material/InputLabel';
import Select from '@mui/material/Select';
import MenuItem from '@mui/material/MenuItem';

function TagFilter({ categories, selectedTag, setSelectedTag }) {
  const handleChange = (event) => {
    setSelectedTag(event.target.value);
  };

  return (
      <FormControl variant={"outlined"}>
        <InputLabel>Filter by Tag</InputLabel>
        <Select value={selectedTag} onChange={handleChange}>
          <MenuItem value="">
            <em>All</em>
          </MenuItem>
          {categories.map((category) => (
              <MenuItem key={category} value={category}>
                {category}
              </MenuItem>
          ))}
        </Select>
      </FormControl>
  );
}

export default TagFilter;
