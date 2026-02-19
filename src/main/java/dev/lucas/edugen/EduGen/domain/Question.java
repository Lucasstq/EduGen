package dev.lucas.edugen.EduGen.domain;

import dev.lucas.edugen.EduGen.domain.enums.QuestionType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_question")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "version_id", nullable = false)
    private WorksheetVersion version;

    @Column(name = "order_number")
    private int orderNumber;

    @Enumerated(EnumType.STRING)
    private QuestionType type;

    @Column(columnDefinition = "TEXT")
    private String statement;

    @Column(name = "correct_answer", columnDefinition = "TEXT")
    private String correctAnswer;

    @Column(columnDefinition = "TEXT")
    private String explanation;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("label ASC")
    private List<QuestionOption> options = new ArrayList<>();
}
