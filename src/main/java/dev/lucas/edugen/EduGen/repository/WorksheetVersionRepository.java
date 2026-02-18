package dev.lucas.edugen.EduGen.repository;

import dev.lucas.edugen.EduGen.domain.WorksheetVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorksheetVersionRepository extends JpaRepository<WorksheetVersion, Long> {

    /*
        * Método para buscar uma versão específica de uma worksheet, garantindo que ela pertence ao usuário correto.
     */
    Optional<WorksheetVersion> findByIdAndWorksheetOwnerId(Long id, UUID ownerId);

}
