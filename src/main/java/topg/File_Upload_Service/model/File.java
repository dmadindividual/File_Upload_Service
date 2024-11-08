package topg.File_Upload_Service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class File {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID entityId;

    private String filePath;

    private String entityName;
    private String originalFileName; 


   
}
