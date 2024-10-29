package topg.File_Upload_Service.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record FileDto(
        MultipartFile file,
        String entityName,
        UUID entityId // Added to match the File model
) {
}
