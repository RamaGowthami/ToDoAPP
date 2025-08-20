# ToDo List REST API

A complete Spring Boot 3.x REST API for managing ToDo tasks with MySQL database and a modern web interface.

## Features

- ✅ Create, Read, Update, Delete (CRUD) operations for tasks
- ✅ Pagination support for task listing
- ✅ Mark tasks as completed
- ✅ Comprehensive error handling
- ✅ Input validation with detailed error messages
- ✅ CORS support for frontend integration
- ✅ Comprehensive logging
- ✅ Unit tests for service and controller layers
- ✅ Standardized API response format
- ✅ Modern web interface with Thymeleaf templates
- ✅ Responsive design with Bootstrap 5
- ✅ Real-time task management

## Technology Stack

- **Java 21**
- **Spring Boot 3.5.4**
- **Spring Data JPA**
- **MySQL**
- **Lombok**
- **Maven**
- **JUnit 5**
- **Thymeleaf** (for web templates)
- **Bootstrap 5** (for UI)
- **Font Awesome** (for icons)

## Project Structure

```
src/main/java/com/example/todoapp/
├── controller/
│   ├── TaskController.java          # REST API endpoints
│   └── WebController.java           # Web interface endpoints
├── service/
│   ├── TaskService.java
│   └── TaskServiceImpl.java
├── repository/
│   └── TaskRepository.java
├── entity/
│   └── Task.java
├── dto/
│   ├── TaskRequestDTO.java
│   ├── TaskResponseDTO.java
│   └── ApiResponse.java
├── exception/
│   ├── ResourceNotFoundException.java
│   ├── ValidationException.java
│   └── GlobalExceptionHandler.java
└── TodoApplication.java

src/main/resources/
├── templates/
│   ├── index.html                   # Home page with task list
│   ├── create-task.html             # Create new task form
│   ├── edit-task.html               # Edit task form
│   ├── task-detail.html             # Task detail view
│   └── layout.html                  # Base layout template
├── static/                          # Static resources
└── application.properties           # Configuration
```

## API Endpoints

### REST API (JSON)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/tasks` | Create a new task |
| GET | `/api/tasks?page=0&size=10` | Get all tasks with pagination |
| GET | `/api/tasks/{id}` | Get a specific task by ID |
| PUT | `/api/tasks/{id}` | Update a task |
| DELETE | `/api/tasks/{id}` | Delete a task |
| PATCH | `/api/tasks/{id}/complete` | Mark a task as completed |

### Web Interface (HTML)

| Page | URL | Description |
|------|-----|-------------|
| Home | `/` | Display all tasks with pagination |
| Create Task | `/tasks/new` | Form to create a new task |
| Task Detail | `/tasks/{id}` | View specific task details |
| Edit Task | `/tasks/{id}/edit` | Form to edit an existing task |

## Web Interface Features

### 🎨 **Modern UI/UX**
- **Responsive Design**: Works on desktop, tablet, and mobile
- **Bootstrap 5**: Modern, clean interface
- **Font Awesome Icons**: Beautiful, consistent icons
- **Card-based Layout**: Easy-to-scan task cards
- **Hover Effects**: Interactive elements with smooth transitions

### 📊 **Task Management**
- **Task Cards**: Visual representation of tasks with status badges
- **Pagination**: Navigate through large task lists
- **Search & Filter**: Find tasks quickly (future enhancement)
- **Bulk Actions**: Select multiple tasks (future enhancement)

### ✨ **Interactive Features**
- **Real-time Updates**: Tasks update without page refresh
- **Form Validation**: Client-side and server-side validation
- **Confirmation Dialogs**: Safe delete operations
- **Status Toggle**: Mark tasks as complete/incomplete
- **Quick Actions**: Edit, delete, and view tasks with one click

## Request/Response Format

### Create Task Request
```json
{
  "title": "Complete project documentation",
  "description": "Write comprehensive documentation for the Spring Boot project",
  "completed": false
}
```

### Task Response
```json
{
  "status": "success",
  "message": "Task created successfully",
  "data": {
    "id": 1,
    "title": "Complete project documentation",
    "description": "Write comprehensive documentation for the Spring Boot project",
    "completed": false,
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  }
}
```

### Validation Rules

#### TaskRequestDTO Validation
- **title**: Required, cannot be empty, must be between 1-255 characters
- **description**: Optional, maximum 1000 characters
- **completed**: Optional, boolean value

#### Validation Error Response Example
```json
{
  "status": "error",
  "message": "Validation failed. Please check the following fields:",
  "data": {
    "title": "Title is required and cannot be empty",
    "description": "Description cannot exceed 1000 characters"
  }
}
```

## Setup Instructions

### Prerequisites

1. Java 21 or higher
2. Maven 3.6+
3. MySQL 8.0+

### Database Setup

1. Create a MySQL database:
```sql
CREATE DATABASE todoapp;
```

2. Update `application.properties` with your database credentials:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/todoapp?useSSL=false&serverTimezone=UTC
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Run the application:
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Accessing the Application

- **Web Interface**: Open `http://localhost:8080` in your browser
- **REST API**: Use `http://localhost:8080/api/tasks` for API calls
- **API Documentation**: Available at `http://localhost:8080/api/tasks` (GET request)

### Running Tests

```bash
mvn test
```

## Configuration

### Application Properties

Key configuration options in `application.properties`:

- `spring.datasource.*` - Database connection settings
- `spring.jpa.hibernate.ddl-auto=update` - Auto-create/update database schema
- `server.port=8080` - Application port
- `logging.level.com.example.todoapp=DEBUG` - Debug logging for the application
- `spring.web.cors.allowed-origins=http://localhost:3000` - CORS configuration for frontend

## Development

### Adding New Features

1. Create entity in `entity/` package
2. Create DTOs in `dto/` package with proper validation annotations
3. Create repository in `repository/` package
4. Create service in `service/` package
5. Create controller in `controller/` package
6. Add tests in `test/` package
7. Create Thymeleaf templates in `templates/` package

### Code Style

- Follow Java naming conventions
- Use Lombok annotations for boilerplate code
- Add comprehensive logging with `@Slf4j`
- Include proper exception handling
- Write unit tests for all business logic
- Use `@Valid` annotation on all `@RequestBody` parameters
- Add validation annotations to DTOs
- Provide clear error messages
- Use semantic HTML in templates
- Follow Bootstrap 5 conventions for styling

## Screenshots

### Home Page
- Task cards with status badges
- Pagination controls
- Quick action buttons
- Responsive grid layout

### Create/Edit Forms
- Clean, intuitive forms
- Real-time validation
- Clear error messages
- Consistent styling

### Task Detail View
- Comprehensive task information
- Action buttons
- Status indicators
- Timestamps

## License

This project is licensed under the MIT License.
