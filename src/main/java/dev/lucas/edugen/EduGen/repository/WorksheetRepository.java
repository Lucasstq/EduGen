package dev.lucas.edugen.EduGen.repository;

import dev.lucas.edugen.EduGen.domain.Worksheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorksheetRepository extends JpaRepository<Worksheet, Long> {
}
