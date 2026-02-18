package dev.lucas.edugen.EduGen.repository;

import dev.lucas.edugen.EduGen.domain.User;
import dev.lucas.edugen.EduGen.domain.Worksheet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorksheetRepository extends JpaRepository<Worksheet, Long> {

    @Query("SELECT COUNT(w) FROM Worksheet w WHERE w.owner = :owner")
    long countWorksheetByOwner(User owner);

    /*
        * Método para buscar as worksheets de um usuário específico, com paginação.
     */
    Page<Worksheet> findByOwner(User owner, Pageable pageable);

    Optional<Worksheet> findByIdAndOwnerId(Long id, UUID ownerId);


}
