# File Upload Service

A Spring Boot-based REST API for uploading and retrieving files, including images and PDFs. This service allows users to upload files with metadata and fetch them by UUID, returning the files as downloadable resources.

## Features

- **File Upload**: Upload files (image or PDF) with metadata, such as `entityName` and `entityId`.
- **File Retrieval**: Retrieve files by `UUID` and download them with the appropriate content type.
- **Validation**: Validates file type and size (up to 5 MB) before uploading.
- **Error Handling**: Provides clear error responses for invalid input, file not found, and server errors.

## Project Structure

- **Controller Layer**: Manages incoming HTTP requests.
- **Service Layer**: Handles business logic for file upload and retrieval.
- **Repository Layer**: Communicates with the database to store file metadata.

## Endpoints

### 1. Upload a File

- **Endpoint**: `POST /api/upload`
- **Parameters**:
  - `file` (Multipart): The file to upload.
  - `entityName` (String): The name associated with the file.
- **Response**: 
  - `201 Created` on successful upload with a message containing the file name.
  - `400 Bad Request` if there is an issue with the file input (e.g., invalid type, missing parameters).
- **Example**:

    ```http
    POST /api/upload
    Content-Type: multipart/form-data

    file: [binary file data]
    entityName: "exampleEntity"
    ```

### 2. Retrieve a File by UUID

- **Endpoint**: `GET /api/files/{id}`
- **Parameters**:
  - `id` (UUID): The unique identifier for the file.
- **Response**:
  - `200 OK` with the file in `byte[]` format and correct content type.
  - `404 Not Found` if the file is not found or cannot be read.
  - `500 Internal Server Error` for server-side issues while reading the file.
- **Example**:

    ```http
    GET /api/files/123e4567-e89b-12d3-a456-426614174000
    ```

## Prerequisites

- **Java 17**
- **Maven**
- **MySQL Database** (or another compatible database)

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/your-username/File_Upload_Service.git
cd File_Upload_Service


# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/your_db_name
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password

# JPA Settings
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

mvn spring-boot:run


This `README.md` file provides a comprehensive overview, installation instructions, endpoint documentation, error handling info, and general project information. Adjust the configuration or any project-specific details as needed for your setup.
