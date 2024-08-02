package knou.course.controller.professor;

import jakarta.validation.Valid;
import knou.course.dto.ApiResponse;
import knou.course.dto.professor.request.ProfessorCreateRequest;
import knou.course.dto.professor.response.ProfessorResponse;
import knou.course.service.professor.ProfessorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ProfessorController {

    private final ProfessorService professorService;

    @PostMapping("/api/v1/professor")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ProfessorResponse> createProfessor(@Valid @RequestBody ProfessorCreateRequest request) {
        return ApiResponse.ok(professorService.createProfessor(request));
    }
}
