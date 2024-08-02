package knou.course.service.professor;

import knou.course.domain.department.Department;
import knou.course.domain.department.DepartmentRepository;
import knou.course.domain.professor.Professor;
import knou.course.domain.professor.ProfessorRepository;
import knou.course.dto.professor.request.ProfessorCreateRequest;
import knou.course.dto.professor.response.ProfessorResponse;
import knou.course.exception.AppException;
import knou.course.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static knou.course.exception.ErrorCode.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProfessorService {

    private final ProfessorRepository professorRepository;
    private final DepartmentRepository departmentRepository;

    @Transactional
    public ProfessorResponse createProfessor(final ProfessorCreateRequest request) {
        Department department = departmentRepository.findByDepartmentName(request.getDepartmentName())
                .orElseThrow(() -> new AppException(NOT_FOUND_DEPARTMENT, NOT_FOUND_DEPARTMENT.getMessage()));

        Professor savedProfessor = professorRepository.save(request.toEntity(department));

        return ProfessorResponse.of(savedProfessor, department);
    }
}
