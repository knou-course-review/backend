package knou.course.domain.professor;

import jakarta.persistence.*;
import knou.course.domain.BaseEntity;
import knou.course.domain.department.Department;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Professor extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String professorName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @Builder
    public Professor(final String professorName, final Department department) {
        this.professorName = professorName;
        this.department = department;
    }
}
