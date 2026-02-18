package dev.lucas.edugen.EduGen.controller;

import dev.lucas.edugen.EduGen.dtos.request.worksheet.CreateWorksheetRequest;
import dev.lucas.edugen.EduGen.dtos.request.worksheet.GenerateVersionRequest;
import dev.lucas.edugen.EduGen.dtos.response.worksheet.WorksheetResponse;
import dev.lucas.edugen.EduGen.dtos.response.worksheet.WorksheetVersionResponse;
import dev.lucas.edugen.EduGen.service.worksheet.WorksheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

}
