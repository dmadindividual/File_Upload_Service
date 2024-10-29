package topg.File_Upload_Service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import topg.File_Upload_Service.dto.FileDto;
import topg.File_Upload_Service.service.FileService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("entityName") String entityName) {
        UUID entityId = UUID.randomUUID();
        FileDto fileDto = new FileDto(file, entityName, entityId);
        String result = fileService.uploadFile(fileDto);

        if (result.startsWith("File uploaded successfully")) {
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<byte[]> getFileById(@PathVariable UUID id) {
        Optional<String> filePathOptional = fileService.getFileById(id).describeConstable();

        if (filePathOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(("File with ID " + id + " not found.").getBytes());
        }

        Path filePath = Paths.get(filePathOptional.get());
        try {
            Resource fileResource = new UrlResource(filePath.toUri());

            if (!fileResource.exists() || !fileResource.isReadable()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(("File with ID " + id + " cannot be read or does not exist.").getBytes());
            }

            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filePath.getFileName().toString() + "\"")
                    .body(Files.readAllBytes(filePath));

        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error reading the file with ID " + id + ": " + ex.getMessage()).getBytes());
        }
    }
}
