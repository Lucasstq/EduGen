package dev.lucas.edugen.EduGen.domain;

import dev.lucas.edugen.EduGen.domain.enums.Difficulty;
import dev.lucas.edugen.EduGen.domain.enums.Grade;
import dev.lucas.edugen.EduGen.domain.enums.Subject;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_worksheet")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Worksheet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(name = "teacher_name")
    private String teacherName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Subject subject;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Grade grade;

    @Column(nullable = false)
    private String topic;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty;

    @Column(name = "question_count", nullable = false)
    private int questionCount;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder.Default
    @OneToMany(mappedBy = "worksheet", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    private List<WorksheetVersion> versions = new ArrayList<>();

}
