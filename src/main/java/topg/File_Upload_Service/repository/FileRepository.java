package topg.File_Upload_Service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import topg.File_Upload_Service.model.File;

import java.util.Optional;
import java.util.UUID;

public interface FileRepository extends JpaRepository<File, UUID> {


    Optional<File> findByEntityId(UUID entityId);
}
