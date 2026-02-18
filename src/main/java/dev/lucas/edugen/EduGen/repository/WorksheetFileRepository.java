package dev.lucas.edugen.EduGen.repository;

import dev.lucas.edugen.EduGen.domain.WorksheetFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorksheetFileRepository extends JpaRepository<WorksheetFile, Long> {
}
