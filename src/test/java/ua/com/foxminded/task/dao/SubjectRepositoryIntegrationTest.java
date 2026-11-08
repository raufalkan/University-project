package ua.com.foxminded.task.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import ua.com.foxminded.task.domain.Subject;
import ua.com.foxminded.task.domain.repository.SubjectModelRepository;

@DBRider
@SpringBootTest
public class SubjectRepositoryIntegrationTest {
    @Autowired
    private SubjectRepository subjectRepository;

    @Test
    @DataSet(cleanBefore = true, skipCleaningFor = "flyway_schema_history")
    public void whenRepositoryHasNotRecords_thenReturnEmptyList() {
        List<Subject> subjects = subjectRepository.findAll();
        assertThat(subjects).isNotNull().isEmpty();
    }

    @Test
    @DataSet(value = "subject/subjects.yml",
        cleanBefore = true,
        skipCleaningFor = "flyway_schema_history")
    @ExpectedDataSet(value = "subject/expected-subjects.yml")
    public void whenRepositoryHasRecords_thenReturnNonEmptyList() {
        List<Subject> subjects = subjectRepository.findAll();
        assertThat(subjects).isNotNull().isNotEmpty().hasSize(4);
    }

    @Test
    @DataSet(value = "subject/subjects.yml",
        cleanBefore = true,
        skipCleaningFor = "flyway_schema_history")
    public void whenPutAtTableDbObjects_thenGetThisObjectsFindById() {
        Subject expectedSubject = SubjectModelRepository.getModel3();
        int id = 3;
        Subject actuallySubject = subjectRepository.findById(id).get();
        assertThat(expectedSubject).isEqualTo(actuallySubject);
    }

    @Test
    @DataSet(value = "subject/subjects.yml",
        cleanBefore = true,
        skipCleaningFor = "flyway_schema_history")
    public void whenFindByTitle_thenSubjectReturned() {
        String title = "Mathmatics";
        Subject subjectActually = subjectRepository.findByTitle(title);
        assertThat(subjectActually.getTitle()).isNotNull().isEqualTo(title);
    }

    @Test
    @DataSet(cleanBefore = true, skipCleaningFor = "flyway_schema_history")
    @ExpectedDataSet(value = "subject/expected-subject.yml")
    public void whenSaveObject_thenExpectRecord() {
        Subject subject = SubjectModelRepository.getModel2();
        Subject actuallySubject = subjectRepository.saveAndFlush(subject);
        assertThat(actuallySubject).isNotNull();
    }

    @Test
    @DataSet(value = "subject/subject.yml",
        cleanBefore = true,
        skipCleaningFor = "flyway_schema_history")
    @ExpectedDataSet(value = "subject/expected-subject.yml")
    public void whenUpdateObject_thenExpectUpdatedRecord() {
        Subject subject = SubjectModelRepository.getModel2();
        subject.setId(1);
        Subject actuallySubject = subjectRepository.saveAndFlush(subject);
        assertThat(actuallySubject).isNotNull();
    }
}
