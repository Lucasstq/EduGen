package dev.lucas.edugen.EduGen.repository;

import dev.lucas.edugen.EduGen.domain.TeachingExample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeachingExampleRepository extends JpaRepository<TeachingExample, Long> {
}
