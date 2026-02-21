package dev.lucas.edugen.EduGen.domain;

import dev.lucas.edugen.EduGen.domain.enums.VersionStatus;
import dev.lucas.edugen.EduGen.domain.enums.VersionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_worksheet_version")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class WorksheetVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "worksheet_id", nullable = false)
    private Worksheet worksheet;

    @Enumerated(EnumType.STRING)
    @Column(name = "version_type", nullable = false)
    private VersionType versionType;

    private int seed;

    @Column(name = "include_answers", nullable = false)
    private boolean includeAnswers;

    @Column(name = "include_explanations", nullable = false)
    private boolean includeExplanations;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VersionStatus status;

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String specJson;

    @Column(name = "ai_description", columnDefinition = "TEXT")
    private String aiDescription;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Builder.Default
    @OneToMany(mappedBy = "version", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orderNumber ASC")
    private List<Question> questions = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "version", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt DESC")
    private List<WorksheetFile> files = new ArrayList<>();
}
