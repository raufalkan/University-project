package ua.com.foxminded.task.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import ua.com.foxminded.task.domain.Faculty;
import ua.com.foxminded.task.domain.repository.FacultyModelRepository;

@DBRider
@SpringBootTest
public class FacultyRepositoryIntegrationTest {
    @Autowired
    private FacultyRepository facultyRepository;

    @Test
    @DataSet(cleanBefore = true, skipCleaningFor = "flyway_schema_history")
    public void whenRepositoryHasNotRecords_thenReturnEmptyList() {
        List<Faculty> faculties = facultyRepository.findAll();
        assertThat(faculties).isNotNull().isEmpty();
    }

    @Test
    @DataSet(value = "faculty/faculties.yml",
        cleanBefore = true,
        skipCleaningFor = "flyway_schema_history")
    @ExpectedDataSet(value = "faculty/expected-faculties.yml")
    public void whenRepositoryHasRecords_thenReturnNonEmptyList() {
        List<Faculty> faculties = facultyRepository.findAll();
        assertThat(faculties).isNotNull().isNotEmpty().hasSize(6);
    }

    @Test
    @DataSet(value = "faculty/faculties.yml",
        cleanBefore = true,
        skipCleaningFor = "flyway_schema_history")
    public void whenPutAtTableDbObjects_thenGetThisObjectsFindById() {
        Faculty expectedFaculty = FacultyModelRepository.getModel3();
        int id = 3;
        Faculty actuallyFaculty = facultyRepository.findById(id).get();
        assertThat(expectedFaculty).isEqualTo(actuallyFaculty);
    }

    @Test
    @DataSet(value = "faculty/faculties.yml",
        cleanBefore = true,
        skipCleaningFor = "flyway_schema_history")
    public void whenFindByTitle_thenFacultyReturned() {
        String title = "faculty3";
        Faculty facultyActually = facultyRepository.findByTitle(title);
        assertThat(facultyActually.getTitle()).isNotNull().isEqualTo(title);
    }

    @Test
    @DataSet(cleanBefore = true, skipCleaningFor = "flyway_schema_history")
    @ExpectedDataSet(value = "faculty/expected-faculty.yml")
    public void whenSaveObject_thenExpectRecord() {
        Faculty faculty = FacultyModelRepository.getModel2();
        Faculty actuallyFaculty = facultyRepository.saveAndFlush(faculty);
        assertThat(actuallyFaculty).isNotNull();
    }

    @Test
    @DataSet(value = "faculty/faculty.yml",
        cleanBefore = true,
        skipCleaningFor = "flyway_schema_history")
    @ExpectedDataSet(value = "faculty/expected-faculty.yml")
    public void whenUpdateObject_thenExpectUpdatedRecord() {
        Faculty faculty = FacultyModelRepository.getModel2();
        faculty.setId(1);
        Faculty actuallyFaculty = facultyRepository.saveAndFlush(faculty);
        assertThat(actuallyFaculty).isNotNull();
    }
}
