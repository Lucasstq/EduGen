package dev.lucas.edugen.EduGen.domain;

import dev.lucas.edugen.EduGen.domain.enums.Audience;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_worksheet_file")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class WorksheetFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "version_id", nullable = false)
    private WorksheetVersion version;

    @Enumerated(EnumType.STRING)
    private Audience audience;

    @Column(name = "storage_key")
    private String storageKey;

    private String sha256;

    @Column(name = "size_bytes")
    private long sizeBytes;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
