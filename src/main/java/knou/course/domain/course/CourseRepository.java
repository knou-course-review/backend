package knou.course.domain.course;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query(value = "SELECT c " +
            "FROM Course c " +
            "WHERE c.professorId IN ( " +
            "SELECT p.id " +
            "FROM Professor p " +
            "WHERE p.professorName LIKE :name)")
    Page<Course> customFindByProfessorId(@Param("name") String name, Pageable pageable);

    @Query(value = "SELECT c " +
            "FROM Course c " +
            "WHERE c.departmentId IN ( " +
            "SELECT d.id " +
            "FROM Department d " +
            "WHERE d.departmentName LIKE :name)")
    Page<Course> customFindByDepartmentId(@Param("name") String name, Pageable pageable);

    @Query(value = "SELECT c " +
            "FROM Course c " +
            "WHERE c.id IN ( " +
            "SELECT c.id " +
            "FROM Course c " +
            "WHERE c.courseName LIKE :name)")
    Page<Course> customFindByCourseId(@Param("name") String name, Pageable pageable);

}
