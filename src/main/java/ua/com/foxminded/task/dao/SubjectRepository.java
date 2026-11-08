package ua.com.foxminded.task.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.task.domain.Subject;

@Repository
@Transactional
public interface SubjectRepository extends JpaRepository<Subject, Integer> {

    public boolean existsById(Integer id);

    public Subject findByTitle(String title);
}
