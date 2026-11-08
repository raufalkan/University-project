package ua.com.foxminded.task.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import ua.com.foxminded.task.domain.Lecture;
import ua.com.foxminded.task.domain.repository.LectureModelRepository;

@DBRider
@SpringBootTest
public class LectureRepositoryIntegrationTest {

    @Autowired
    private LectureRepository lectureRepository;

    @Test
    @DataSet(cleanBefore = true, skipCleaningFor = "flyway_schema_history")
    public void whenRepositoryHasNotRecords_thenReturnEmptyList() {
        List<Lecture> lectures = lectureRepository.findAll();
        assertThat(lectures).isNotNull().isEmpty();
    }

    @Test
    @DataSet(value = "lecture/lectures.yml",
        cleanBefore = true,
        skipCleaningFor = "flyway_schema_history")
    @ExpectedDataSet(value = "lecture/expected-lectures.yml")
    public void whenRepositoryHasRecords_thenReturnNonEmptyList() {
        List<Lecture> lectures = lectureRepository.findAll();
        assertThat(lectures).isNotNull().isNotEmpty().hasSize(6);
    }

    @Test
    @DataSet(value = "lecture/lectures.yml",
        cleanBefore = true,
        skipCleaningFor = "flyway_schema_history")
    public void whenPutAtTableDbObjects_thenGetThisObjectsFindById() {
        Lecture expectedLecture = LectureModelRepository.getModel3();
        int id = 3;
        Lecture actuallyLecture = lectureRepository.findById(id).get();
        assertThat(expectedLecture).isEqualTo(actuallyLecture);
    }

    @Test
    @DataSet(value = "lecture/lectures.yml",
        cleanBefore = true,
        skipCleaningFor = "flyway_schema_history")
    public void whenFindByNumber_thenLectureReturned() {
        String number = "3";
        Lecture lectureActually = lectureRepository.findByNumber(number);
        assertThat(lectureActually.getNumber()).isNotNull().isEqualTo(number);
    }

    @Test
    @DataSet(cleanBefore = true,
        skipCleaningFor = "flyway_schema_history")
    @ExpectedDataSet(value = "lecture/expected-lecture.yml")
    public void whenSaveObject_thenExpectRecord() {
        Lecture lecture = LectureModelRepository.getModel2();
        Lecture actuallyLecture = lectureRepository.saveAndFlush(lecture);
        assertThat(actuallyLecture).isNotNull();
    }

    @Test
    @DataSet(value = "lecture/lecture.yml",
        cleanBefore = true,
        skipCleaningFor = "flyway_schema_history")
    @ExpectedDataSet(value = "lecture/expected-lecture.yml")
    public void whenUpdateObject_thenExpectUpdatedRecord() {
        Lecture lecture = LectureModelRepository.getModel2();
        lecture.setId(1);
        Lecture actuallyLecture = lectureRepository.saveAndFlush(lecture);
        assertThat(actuallyLecture).isNotNull();
    }
}
