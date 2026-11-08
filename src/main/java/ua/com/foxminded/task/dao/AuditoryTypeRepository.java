package ua.com.foxminded.task.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.task.domain.AuditoryType;

@Repository
@Transactional
public interface AuditoryTypeRepository extends JpaRepository<AuditoryType, Integer> {

    public boolean existsById(Integer id);

    public AuditoryType findByType(String type);
}
