package ua.com.foxminded.task.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.task.domain.Student;

@Repository
@Transactional
public interface StudentRepository extends JpaRepository<Student, Integer> {

    public boolean existsById(Integer id);
    
    public List<Student> findAllByGroupId(Integer id);

    public Student findByIdFees(Integer idFees);
    
    public long countByGroupId(Integer id);
    
    public boolean existsStudentByIdAndGroupId(Integer studentId, Integer groupId);

}
