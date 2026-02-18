package dev.lucas.edugen.EduGen.repository;

import dev.lucas.edugen.EduGen.domain.WorksheetVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorksheetVersionRepository extends JpaRepository<WorksheetVersion, Long> {
}
