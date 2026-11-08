package ua.com.foxminded.task.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.task.domain.Lecture;

@Repository
@Transactional
public interface LectureRepository extends JpaRepository<Lecture, Integer> {

    public boolean existsById(Integer id);

    public Lecture findByNumber(String number);
}
