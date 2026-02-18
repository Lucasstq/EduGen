package dev.lucas.edugen.EduGen.repository;

import dev.lucas.edugen.EduGen.domain.QuestionOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionOptionRepository extends JpaRepository<QuestionOption, Long> {
}
