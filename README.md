# bookmark-it

## Background
In the modern digital age, people consume an overwhelming amount of content
online—articles, blogs, videos, tutorials, and more. While browser bookmarks exist, they
quickly become cluttered, unorganized, and hard to navigate. Furthermore, traditional
bookmarking features in browsers don't allow users to categorize or search effectively by
topics or interests, which makes retrieving important links a cumbersome process.
Many professionals, students, and casual users find themselves bookmarking content but
rarely revisiting it due to the lack of proper organization. As a result, valuable knowledge is
lost in an unmanageable list of bookmarks, often spread across multiple devices.
The solution? BookmarkIt
BookmarkIt is a lightweight, user-friendly web application designed to provide an intuitive
solution for managing bookmarks. Users can save links to their favorite articles, tutorials, or
tools and tag them with meaningful categories, such as “Tech,” “Design,” “Work,” or
“Learning.” This allows users to quickly filter and retrieve saved bookmarks when needed.
Whether a user is researching for a project, studying for an exam, or curating interesting
reads, BookmarkIt ensures that their saved content is easy to access and well-organized.

## Run it locally

### Pre-requisites
1. Clone the repository
2. Run `scripts/start-service.sh`
3. Run `scripts/create-database.sh`
4. Run `npm install` in src/main/js/bookmark-it

### Start the backend
1. Make sure the script `/start-service.sh` is running
2. Add a configuration to run the backend in your IDE 
   * Go to `Edit configurations` in your IDE 
   * Add a new configuration of type `Application`
   * Point at the main class `com.bookmarkit.BookmarkItApplication`
3. Run the configuration

### Start the frontend
1. Run `npm start` in src/main/js/bookmark-it

## Dependencies
* Java
* Node.js
* npm
* Postgres
* Docker/Podman
