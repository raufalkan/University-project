package ua.com.foxminded.task.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.task.domain.Department;

@Repository
@Transactional
public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    public boolean existsById(Integer id);

    public Department findByTitle(String title);
}
