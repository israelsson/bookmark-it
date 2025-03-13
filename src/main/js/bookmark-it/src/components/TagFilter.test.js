import { render, screen } from '@testing-library/react';
import TagFilter from './TagFilter';

test('renders heading', () => {
  render(<TagFilter categories={[]} selectedTag={""} setSelectedTag={null} />);
  const linkElement = screen.getByText(/Filter by Tag/i);
  expect(linkElement).toBeInTheDocument();
});
