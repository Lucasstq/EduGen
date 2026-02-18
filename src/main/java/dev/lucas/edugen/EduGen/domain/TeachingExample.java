package dev.lucas.edugen.EduGen.domain;

import dev.lucas.edugen.EduGen.domain.enums.Grade;
import dev.lucas.edugen.EduGen.domain.enums.Subject;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_teaching_example")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TeachingExample {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Subject subject;

    @Enumerated(EnumType.STRING)
    private Grade grade;

    private String topic;

    @Column(name = "content_text", columnDefinition = "TEXT")
    private String contentText;

    private String source;

    private String embedding;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
