# Idea Refinery - Tasks API

This is a Spring Boot project that provides REST API for managing tasks. 
The project uses an in-memory store for data.

## Prerequisites

- Java 17 or higher

## Getting Started

1. Clone the repository:

    ```bash
    git clone git@github.com:kpilszak/idea-refinery.git
    ```

2. Navigate to the project directory:

    ```bash
    cd idea-refinery
    ```

3. Build the project using Maven Wrapper:

    ```bash
    ./mvnw clean install -DskipTests
    ```

4. Run the application:

    ```bash
    ./mvnw spring-boot:run
    ```

The application will start, and you can access the API endpoints as described below.

## API

### Get all tasks

- **Endpoint:** `GET /tasks`
- **Description:** Returns a list of all tasks.

### Get task by ID

- **Endpoint:** `GET /tasks/{id}`
- **Description:** Returns a specific task identified by its ID.

### Create a new task

- **Endpoint:** `POST /tasks`
- **Description:** Creates a new task.
- **Request Body:**
    ```json
    {
      "title": "Task Title",
      "description": "Task Description"
    }
    ```

### Update a task by ID

- **Endpoint:** `PUT /tasks/{id}`
- **Description:** Updates an existing task identified by its ID.
- **Request Body:**
    ```json
    {
      "title": "Updated Task Title",
      "description": "Updated Task Description",
      "completed": true,
      "createDate": "2023-01-01T12:00:00.000Z",
      "completedDate": "2023-01-02T12:00:00.000Z"
    }
    ```

## Running tests

To run tests for the project, execute the following command:

```bash
./mvnw test
```
