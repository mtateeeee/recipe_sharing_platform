# Recipe Sharing Platform

A simple social networking website backend for recipe sharing built with Spring Boot, JPA, and H2.

## Features
- User registration and login (simple credential check)
- Recipe CRUD (create, read, update, delete)
- Recipe comment feed
- Search recipes by title
- In-memory H2 database + data seeding

## Run

1. Build the project:

   mvn clean package

2. Run:

   mvn spring-boot:run

3. Open API:

   http://localhost:8080/api/recipes
   http://localhost:8080/api/auth

4. H2 console (development):

   http://localhost:8080/h2-console
   JDBC URL: jdbc:h2:mem:recipeshare

## Example requests

- Register:
  POST /api/auth/register
  {
    "username": "jane",
    "fullName": "Jane Doe",
    "email": "jane@example.com",
    "password": "password"
  }

- Login:
  POST /api/auth/login
  {
    "username": "jane",
    "password": "password"
  }

- Create recipe:
  POST /api/recipes?authorId=1
  {
    "title": "Grilled Veggie Salad",
    "description": "Healthy and fresh",
    "instructions": "..."
  }

- Add comment:
  POST /api/recipes/{id}/comments?authorId=2
  {
    "text": "Great recipe!"
  }
