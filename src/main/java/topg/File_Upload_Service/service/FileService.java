package topg.File_Upload_Service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import topg.File_Upload_Service.dto.FileDto;
import topg.File_Upload_Service.model.File;
import topg.File_Upload_Service.repository.FileRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    private final String UPLOAD_DIR = "Downloads/uploads";
    private final FileRepository fileRepository;

    public String uploadFile(FileDto fileDto) {
        // Validate input
        if (fileDto.file() == null || fileDto.entityName() == null || fileDto.entityId() == null) {
            return "Invalid input: file, entity name, and entity ID must not be null.";
        } else if (!isValidFileType(fileDto.file().getContentType())) {
            return "Invalid file type: Only images and PDFs are allowed.";
        } else if (fileDto.file().getSize() > 5 * 1024 * 1024) { // 5 MB limit
            return "File size exceeds limit of 5 MB.";
        }

        // Create directory if it doesn't exist
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                return "Error creating upload directory: " + e.getMessage();
            }
        }

        // Generate a unique filename
        String fileName = UUID.randomUUID().toString() + "_" + fileDto.file().getOriginalFilename();

        // Define the path where the file will be saved
        Path filePath = uploadPath.resolve(fileName);

        // Save the file
        try {
            Files.copy(fileDto.file().getInputStream(), filePath);
        } catch (IOException e) {
            return "Error saving the file: " + e.getMessage();
        }

        // Save file metadata to your File model
        File newFile = File.builder()
                .filePath(filePath.toString())
                .entityName(fileDto.entityName())
                .entityId(fileDto.entityId())
                .originalFileName(fileDto.file().getOriginalFilename())
                .build();

        // Save the new file entity to the database
        fileRepository.save(newFile);

        // Return a success message
        return "File uploaded successfully: " + fileName;
    }

    // Helper method to validate file types
    private boolean isValidFileType(String contentType) {
        return contentType.equals("image/jpeg") ||
                contentType.equals("image/png") ||
                contentType.equals("application/pdf");
    }

    public String getFileById(UUID entityId) {
        Optional<File> fileOptional = fileRepository.findByEntityId(entityId);

        if (fileOptional.isPresent()) {
            return "File found: " + fileOptional.get().getOriginalFileName();
        } else {
            return "File not found.";
        }
    }
}
