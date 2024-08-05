package knou.course.controller.professor;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import knou.course.dto.ApiResponse;
import knou.course.dto.professor.request.ProfessorCreateRequest;
import knou.course.dto.professor.request.ProfessorUpdateRequest;
import knou.course.dto.professor.response.ProfessorResponse;
import knou.course.service.professor.ProfessorService;
import knou.course.swagger.ApiErrorCodeExamples;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static knou.course.exception.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ProfessorController {

    private final ProfessorService professorService;

    @Operation(summary = "교수 등록", description = "교수를 등록합니다. ex) 홍길동, ...")
    @ApiErrorCodeExamples({NOT_ADMIN, INVALID_INPUT_VALUE, NOT_FOUND_DEPARTMENT})
    @PostMapping("/api/v1/professor")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ProfessorResponse> createProfessor(@Valid @RequestBody ProfessorCreateRequest request) {
        return ApiResponse.ok(professorService.createProfessor(request));
    }

    @Operation(summary = "교수 전체 조회", description = "교수를 조회합니다.")
    @GetMapping("/api/v1/professors")
    public ApiResponse<List<ProfessorResponse>> getAllProfessors() {
        return ApiResponse.ok(professorService.getAllProfessors());
    }

    @Operation(summary = "교수 수정", description = "교수를 수정합니다.")
    @ApiErrorCodeExamples({NOT_ADMIN, INVALID_INPUT_VALUE, NOT_FOUND_DEPARTMENT, NOT_FOUND_PROFESSOR})
    @PutMapping("/api/v1/professor/{professorId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ProfessorResponse> updateProfessor(@PathVariable Long professorId,
                                                          @Valid @RequestBody ProfessorUpdateRequest request) {
        return ApiResponse.ok(professorService.updateProfessor(professorId, request));
    }

    @Operation(summary = "교수 삭제", description = "교수를 삭제합니다.")
    @ApiErrorCodeExamples({NOT_ADMIN, INVALID_INPUT_VALUE, NOT_FOUND_PROFESSOR})
    @DeleteMapping("/api/v1/professor/{professorId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteProfessor(@PathVariable Long professorId) {
        professorService.deleteProfessor(professorId);
    }
}
