package knou.course.domain.professor;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    List<Professor> findAllByIdIn(List<Long> ids);
}
