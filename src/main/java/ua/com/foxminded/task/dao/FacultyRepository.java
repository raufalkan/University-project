package ua.com.foxminded.task.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.task.domain.Faculty;

@Repository
@Transactional
public interface FacultyRepository extends JpaRepository<Faculty, Integer> {

    public boolean existsById(Integer id);

    public Faculty findByTitle(String title);
}
