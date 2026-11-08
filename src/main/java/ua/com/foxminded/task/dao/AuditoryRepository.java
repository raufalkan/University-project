package ua.com.foxminded.task.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.task.domain.Auditory;

@Repository
@Transactional
public interface AuditoryRepository extends JpaRepository<Auditory, Integer> {

    public boolean existsById(Integer id);

    public Auditory findByAuditoryNumber(String auditoryNumber);
}
