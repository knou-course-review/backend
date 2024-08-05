package knou.course.domain.course;

import jakarta.persistence.*;
import knou.course.domain.BaseEntity;
import knou.course.dto.course.request.CourseUpdateRequest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Course extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String courseName;

    private Long departmentId;

    private Long professorId;

    private int grade;

    private int credit;

    private String classType; // 수업유형(출석, 비출석)

    private String classification; // 교과구분(전공, 교양, 일반)

    private String semester;

    @Builder
    public Course(final String courseName, final Long departmentId, final Long professorId, final int grade, final int credit, final String classType, final String classification, final String semester) {
        this.courseName = courseName;
        this.departmentId = departmentId;
        this.professorId = professorId;
        this.grade = grade;
        this.credit = credit;
        this.classType = classType;
        this.classification = classification;
        this.semester = semester;
    }

    public void updateCourse(CourseUpdateRequest request) {
        this.courseName = request.getCourseName();
        this.departmentId = request.getDepartmentId();
        this.professorId = request.getProfessorId();
        this.grade = request.getGrade();
        this.credit = request.getCredit();
        this.classType = request.getClassType();
        this.classification = request.getClassification();
        this.semester = request.getSemester();
    }
}
