package ua.com.foxminded.task.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import ua.com.foxminded.task.domain.TimetableItem;
import ua.com.foxminded.task.domain.repository.TimetableItemModelRepository;

@DBRider
@SpringBootTest
public class TimetableItemRepositoryIntegrationTest {

    @Autowired
    private TimetableItemRepository timetableItemRepository;

    @Test
    @DataSet(cleanBefore = true, skipCleaningFor = "flyway_schema_history")
    public void whenRepositoryHasNotRecords_thenReturnEmptyList() {
        List<TimetableItem> timetableItems = timetableItemRepository.findAll();
        assertThat(timetableItems).isNotNull().isEmpty();
    }

    @Test
    @Transactional
    @DataSet(value = "timetableItem/timetableItems.yml",
        cleanBefore = true,
        skipCleaningFor = "flyway_schema_history")
    @ExpectedDataSet(value = "timetableItem/expected-timetableItems.yml")
    public void whenRepositoryHasRecords_thenReturnNonEmptyList() {
        List<TimetableItem> timetableItems = timetableItemRepository.findAll();
        assertThat(timetableItems).isNotNull().isNotEmpty().hasSize(10);
    }

    @Test
    @Transactional
    @DataSet(value = "timetableItem/timetableItems.yml",
        cleanBefore = true,
        skipCleaningFor = "flyway_schema_history")
    public void whenPutAtTableDbObjects_thenGetThisObjectsFindById() {
        int id = 30;
        TimetableItem expectedTimetableItem = TimetableItemModelRepository.getModel3();
        expectedTimetableItem.getTeacher().setSubjects(new ArrayList<>());
        expectedTimetableItem.setId(id);
        TimetableItem actuallyTimetableItem = timetableItemRepository.getOne(id);
        assertThat(expectedTimetableItem).isEqualToComparingFieldByField(actuallyTimetableItem);
    }

    @Test
    @DataSet(cleanBefore = true, skipCleaningFor = "flyway_schema_history")
    @ExpectedDataSet(value = "timetableItem/expected-timetableItem.yml")
    public void whenSaveObject_thenExpectRecord() {
        TimetableItem timetableItem = TimetableItemModelRepository.getModel2();
        timetableItem.setAuditory(null);
        timetableItem.setLecture(null);
        timetableItem.setSubject(null);
        timetableItem.setTeacher(null);
        timetableItem.setGroups(new ArrayList<>());
        TimetableItem actuallyTimetableItem = timetableItemRepository.saveAndFlush(timetableItem);
        assertThat(actuallyTimetableItem).isNotNull();
    }

    @Test
    @DataSet(value = "timetableItem/timetableItem.yml",
        cleanBefore = true,
        skipCleaningFor = "flyway_schema_history")
    @ExpectedDataSet(value = "timetableItem/expected-timetableItem.yml")
    public void whenUpdateObject_thenExpectUpdatedRecord() {
        TimetableItem timetableItem = TimetableItemModelRepository.getModel2();
        timetableItem.setId(1);
        timetableItem.setAuditory(null);
        timetableItem.setLecture(null);
        timetableItem.setSubject(null);
        timetableItem.setTeacher(null);
        timetableItem.setGroups(new ArrayList<>());
        TimetableItem actuallyTimetableItem = timetableItemRepository.saveAndFlush(timetableItem);
        assertThat(actuallyTimetableItem).isNotNull();
    }

    @Test
    @DataSet(value = "timetableItem/timetableItems.yml",
        cleanBefore = true,
        skipCleaningFor = "flyway_schema_history")
    public void whenRepositoryHasDuplicateAuditoryLectureAndDate_thenReturnBoolean() {
        LocalDate date = LocalDate.of(2020, 06, 25);
        TimetableItem exist = timetableItemRepository.findByAuditoryIdAndLectureIdAndDate(1, 1, date);
        assertNotNull(exist);
        TimetableItem noExist = timetableItemRepository.findByAuditoryIdAndLectureIdAndDate(2, 3, date);
        assertNull(noExist);
    }

    @Test
    @DataSet(value = "timetableItem/timetableItems.yml",
        cleanBefore = true,
        cleanAfter = true,
        skipCleaningFor = "flyway_schema_history")
    public void whenRepositoryHasDuplicateTeacherLectureAndDate_thenReturnBoolean() {
        LocalDate date = LocalDate.of(2020, 06, 25);
        TimetableItem exist = timetableItemRepository.findByTeacherIdAndLectureIdAndDate(1, 1, date);
        assertNotNull(exist);
        TimetableItem noExist = timetableItemRepository.findByTeacherIdAndLectureIdAndDate(1, 4, date);
        assertNull(noExist);
    }
}
