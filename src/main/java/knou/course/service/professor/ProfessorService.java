package knou.course.service.professor;

import knou.course.domain.department.Department;
import knou.course.domain.department.DepartmentRepository;
import knou.course.domain.professor.Professor;
import knou.course.domain.professor.ProfessorRepository;
import knou.course.dto.professor.request.ProfessorCreateRequest;
import knou.course.dto.professor.request.ProfessorUpdateRequest;
import knou.course.dto.professor.response.ProfessorResponse;
import knou.course.exception.AppException;
import knou.course.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public List<ProfessorResponse> getAllProfessors() {
        List<Professor> professors = professorRepository.findAll();

        return professors.stream()
                .map(ProfessorResponse::of)
                .toList();
    }

    @Transactional
    public ProfessorResponse updateProfessor(final Long professorId, final ProfessorUpdateRequest request) {
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new AppException(NOT_FOUND_PROFESSOR, NOT_FOUND_PROFESSOR.getMessage()));

        Department department = departmentRepository.findByDepartmentName(request.getDepartmentName())
                .orElseThrow(() -> new AppException(NOT_FOUND_DEPARTMENT, NOT_FOUND_DEPARTMENT.getMessage()));

        professor.updateProfessor(request.getProfessorName(), department);

        return ProfessorResponse.of(professor, department);
    }

    @Transactional
    public void deleteProfessor(final Long professorId) {
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new AppException(NOT_FOUND_PROFESSOR, NOT_FOUND_PROFESSOR.getMessage()));

        professorRepository.delete(professor);
    }
}
