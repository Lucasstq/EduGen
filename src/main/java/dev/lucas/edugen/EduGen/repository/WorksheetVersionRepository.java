package dev.lucas.edugen.EduGen.repository;

import dev.lucas.edugen.EduGen.domain.WorksheetVersion;
import dev.lucas.edugen.EduGen.domain.enums.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorksheetVersionRepository extends JpaRepository<WorksheetVersion, Long> {

    /*
        * Método para buscar uma versão específica de uma worksheet, garantindo que ela pertence ao usuário correto.
     */
    Optional<WorksheetVersion> findByIdAndWorksheetOwnerId(Long id, UUID ownerId);

    List<WorksheetVersion> findTop3ByWorksheetOwnerIdOrderByCreatedAtDesc(UUID ownerId);

    Page<WorksheetVersion> findByWorksheetOwnerId(UUID ownerId, Pageable pageable);

    Page<WorksheetVersion> findByWorksheetOwnerIdAndWorksheetSubject(UUID ownerId, Subject subject, Pageable pageable);
}
