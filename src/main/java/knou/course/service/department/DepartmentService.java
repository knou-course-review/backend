package knou.course.service.department;

import knou.course.domain.department.Department;
import knou.course.domain.department.DepartmentRepository;
import knou.course.dto.department.request.DepartmentCreateRequest;
import knou.course.dto.department.request.DepartmentUpdateNameRequest;
import knou.course.dto.department.response.DepartmentResponse;
import knou.course.exception.AppException;
import knou.course.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static knou.course.exception.ErrorCode.ALREADY_EXIST_DEPARTMENT_NAME;
import static knou.course.exception.ErrorCode.NOT_FOUND_DEPARTMENT;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Transactional
    public DepartmentResponse createDepartment(DepartmentCreateRequest request) {
        if (departmentRepository.findByDepartmentName(request.getDepartmentName()).isPresent()) {
            throw new AppException(ErrorCode.ALREADY_EXIST_DEPARTMENT_NAME, ALREADY_EXIST_DEPARTMENT_NAME.getMessage());
        }

        Department savedDepartment = departmentRepository.save(request.toEntity());

        return DepartmentResponse.of(savedDepartment);
    }

    public List<DepartmentResponse> getAllDepartments() {
        List<Department> departmentList = departmentRepository.findAll();

        return departmentList.stream()
                .map(DepartmentResponse::of)
                .toList();
    }

    @Transactional
    public DepartmentResponse updateDepartmentName(final Long departmentId, final DepartmentUpdateNameRequest request) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new AppException(NOT_FOUND_DEPARTMENT, NOT_FOUND_DEPARTMENT.getMessage()));

        if (departmentRepository.existsByDepartmentName(request.getDepartmentName())) {
            throw new AppException(ALREADY_EXIST_DEPARTMENT_NAME, ALREADY_EXIST_DEPARTMENT_NAME.getMessage());
        }

        department.updateDepartmentName(request.getDepartmentName());
        return DepartmentResponse.of(department);
    }

    @Transactional
    public void deleteDepartment(final Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new AppException(NOT_FOUND_DEPARTMENT, NOT_FOUND_DEPARTMENT.getMessage()));

        departmentRepository.delete(department);
    }
}
