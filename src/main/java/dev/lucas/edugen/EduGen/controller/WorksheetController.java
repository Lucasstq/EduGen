package dev.lucas.edugen.EduGen.controller;

import dev.lucas.edugen.EduGen.domain.enums.Audience;
import dev.lucas.edugen.EduGen.dtos.request.worksheet.CreateWorksheetRequest;
import dev.lucas.edugen.EduGen.dtos.request.worksheet.GenerateVersionRequest;
import dev.lucas.edugen.EduGen.dtos.response.worksheet.WorksheetResponse;
import dev.lucas.edugen.EduGen.dtos.response.worksheet.WorksheetVersionResponse;
import dev.lucas.edugen.EduGen.service.worksheet.WorksheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/worksheets")
@RequiredArgsConstructor
public class WorksheetController {

    private final WorksheetService worksheetService;

    @PostMapping("/{id}/versions")
    public ResponseEntity<WorksheetVersionResponse> generate(@PathVariable Long id,
                                                             @RequestBody GenerateVersionRequest req,
                                                             @AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getClaimAsString("userId"));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(worksheetService.generateWorksheetVersion(id, req, userId));
    }

    @PostMapping
    public ResponseEntity<WorksheetResponse> create(@RequestBody CreateWorksheetRequest req,
                                                    @AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getClaimAsString("userId"));
        return ResponseEntity.status(HttpStatus.CREATED).body(worksheetService.createWorksheet(req, userId));
    }

    @GetMapping("/versions/{versionId}/spec")
    public ResponseEntity<String> getSpec(@PathVariable Long versionId,
                                          @AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getClaimAsString("userId"));
        return ResponseEntity.ok(worksheetService.getSpecJson(versionId, userId));
    }

    @GetMapping("/versions/{versionId}/download")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long versionId,
                                              @RequestParam(defaultValue = "STUDENTS") Audience audience,
                                              @AuthenticationPrincipal Jwt jwt) {

        UUID userId = UUID.fromString(jwt.getClaimAsString("userId"));
        byte[] pdf = worksheetService.downloadPdf(versionId, audience, userId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"atividade_" + versionId + "_" + audience.name().toLowerCase() + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping
    public ResponseEntity<Page<WorksheetResponse>> listWorksheets(@RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "20") int size,
                                                                  @RequestParam(required = false) String subject,
                                                                  @AuthenticationPrincipal Jwt jwt) {

        UUID userId = UUID.fromString(jwt.getClaimAsString("userId"));
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(worksheetService.listWorksheets(userId, subject, pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorksheet(@PathVariable Long id,
                                                @AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getClaimAsString("userId"));
        worksheetService.deleteWorksheet(id, userId);
        return ResponseEntity.noContent().build();
    }
}
