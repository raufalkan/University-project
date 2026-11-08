package ua.com.foxminded.task.dao;

import java.time.LocalDate;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.task.domain.TimetableItem;

@Repository
@Transactional
public interface TimetableItemRepository extends JpaRepository<TimetableItem, Integer>, JpaSpecificationExecutor<TimetableItem> {

    public boolean existsById(Integer id);

    public TimetableItem findByAuditoryIdAndLectureIdAndDate(Integer auditoryId, Integer lectureId, LocalDate date);

    public TimetableItem findByTeacherIdAndLectureIdAndDate(Integer teacherId, Integer lectureId, LocalDate date);
}
