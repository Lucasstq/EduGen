package dev.lucas.edugen.EduGen.domain;

import dev.lucas.edugen.EduGen.domain.enums.OptionLabel;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_question_option")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class QuestionOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OptionLabel label;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    private String text;
}
