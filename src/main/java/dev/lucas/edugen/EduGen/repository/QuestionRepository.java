package dev.lucas.edugen.EduGen.repository;

import dev.lucas.edugen.EduGen.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
}
