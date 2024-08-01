package knou.course.service.department;

import knou.course.domain.department.Department;
import knou.course.domain.department.DepartmentRepository;
import knou.course.dto.department.request.DepartmentCreateRequest;
import knou.course.dto.department.response.DepartmentResponse;
import knou.course.exception.AppException;
import knou.course.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static knou.course.exception.ErrorCode.ALREADY_EXIST_DEPARTMENT_NAME;

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
}
