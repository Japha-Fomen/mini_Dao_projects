# Academic Management System

A desktop application for managing students, instructors, courses, and enrollments — built with **Java 17**, **JavaFX 17**, and **MySQL**.

---

## Features

### Student Portal
- View enrolled courses with grade and status
- Access course materials (file paths / URLs)
- Drop a course (only allowed while status is `on_going`)

### Instructor Portal
- View assigned courses with enrollment and material counts
- Browse students enrolled in each course
- Assign or update grades
- Change student status (`on_going` / `passed` / `fail`)
- Upload course materials

### Admin Dashboard
- Full CRUD management of students, instructors, subjects, and offered courses
- Enroll and unenroll students from courses
- View all enrollments across the system

---

## Tech Stack

| Layer | Technology |
|-------|------------|
| Language | Java 17 |
| UI Framework | JavaFX 17 (FXML) |
| Database | MySQL 8 |
| JDBC Driver | MySQL Connector/J 8.3.0 |
| Build Tool | Maven |

**Architecture:** DAO → Service → Controller → FXML View

---

## Prerequisites

- Java 17+
- Maven 3.8+
- MySQL 8+ running on your machine

---

## Setup

### 1. Clone the repository

```bash
git clone https://github.com/your-username/your-repo-name.git
cd your-repo-name
```

### 2. Create the database

Run the SQL script to create the schema and tables:

```bash
mysql -u root -p < schema.sql
```

### 3. Configure your database credentials

Copy the example config file and fill in your credentials:

```bash
cp src/main/resources/db.properties.example src/main/resources/db.properties
```

Edit `src/main/resources/db.properties`:

```properties
db.url=jdbc:mysql://127.0.0.1:3306/your_database_name
db.user=your_mysql_user
db.password=your_mysql_password
```

> ⚠️ `db.properties` is listed in `.gitignore` and will never be committed to the repository.

### 4. Run the application

```bash
mvn javafx:run
```

---

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   ├── DataAccessObject/        # DAOs — direct database access
│   │   ├── Objects/                 # Domain entities (Student, Enseignant, etc.)
│   │   ├── DtoObjects/              # DTOs — combined views for the UI
│   │   ├── ObjectServiceImplementation/  # Business logic layer
│   │   └── org/example/
│   │       ├── MainApp.java         # JavaFX entry point
│   │       ├── ViewManager.java     # Navigation and session management
│   │       ├── AppContext.java      # Service singletons
│   │       ├── LoginController.java
│   │       ├── StudentController.java
│   │       ├── EnseignantController.java
│   │       └── AdminController.java
│   └── resources/org/example/
│       ├── login.fxml
│       ├── student_view.fxml
│       ├── enseignant_view.fxml
│       ├── admin_view.fxml
│       ├── styles.css
│       └── db.properties.example   # Credentials template (safe to commit)
```

---

## Login

| Role | Credentials required |
|------|----------------------|
| Student | ID + Name (verified against the database) |
| Instructor | ID + Name (verified against the database) |
| Administrator | No credentials — direct access |

---

## Author

**Japha Fomen**
