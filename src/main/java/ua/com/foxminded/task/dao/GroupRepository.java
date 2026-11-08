package ua.com.foxminded.task.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.task.domain.Group;

@Repository
@Transactional
public interface GroupRepository extends JpaRepository<Group, Integer> {

    public boolean existsById(Integer id);
    
    public Group findByTitle(String title);

}
