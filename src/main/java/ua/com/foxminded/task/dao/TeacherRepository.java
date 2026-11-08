package ua.com.foxminded.task.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.task.domain.Teacher;

@Repository
@Transactional
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    public boolean existsById(Integer id);

    public Teacher findByIdFees(Integer idFees);
}
