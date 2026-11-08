package ua.com.foxminded.task.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import ua.com.foxminded.task.domain.Teacher;
import ua.com.foxminded.task.domain.repository.TeacherModelRepository;

@DBRider
@SpringBootTest
public class TeacherRepositoryIntegrationTest {

    @Autowired
    private TeacherRepository teacherRepository;

    @Test
    @DataSet(cleanBefore = true, skipCleaningFor = "flyway_schema_history")
    public void whenRepositoryHasNotRecords_thenReturnEmptyList() {
        List<Teacher> teachers = teacherRepository.findAll();
        assertThat(teachers).isNotNull().isEmpty();
    }

    @Test
    @DataSet(value = "teacher/teachers.yml",
        cleanBefore = true,
        skipCleaningFor = "flyway_schema_history")
    @ExpectedDataSet(value = "teacher/expected-teachers.yml")
    public void whenRepositoryHasRecords_thenReturnNonEmptyList() {
        List<Teacher> teachers = teacherRepository.findAll();
        assertThat(teachers).isNotNull().isNotEmpty().hasSize(3);
    }

    @Test
    @Transactional
    @DataSet(value = "teacher/teachers.yml",
        cleanBefore = true,
        skipCleaningFor = "flyway_schema_history")
    public void whenPutAtTableDbObjects_thenGetThisObjectsFindById() {
        Teacher expectedTeacher = TeacherModelRepository.getModel3();
        expectedTeacher.setDepartment(null);
        expectedTeacher.setSubjects(new ArrayList<>());
        expectedTeacher.setId(3);
        int id = 3;
        Teacher actuallyTeacher = teacherRepository.getOne(id);
        assertThat(expectedTeacher).isEqualToComparingFieldByField(actuallyTeacher);
    }

    @Test
    @DataSet(value = "teacher/teachers.yml",
        cleanBefore = true,
        skipCleaningFor = "flyway_schema_history")
    public void whenFindByIdFees_thenTeacherReturned() {
        int idFees = 333111111;
        Teacher teacherActually = teacherRepository.findByIdFees(idFees);
        assertThat(teacherActually.getIdFees()).isEqualTo(idFees);
    }

    @Test
    @DataSet(cleanBefore = true, skipCleaningFor = "flyway_schema_history")
    @ExpectedDataSet(value = "teacher/expected-teacher.yml")
    public void whenSaveObject_thenExpectRecord() {
        Teacher teacher = TeacherModelRepository.getModel2();
        teacher.setSubjects(new ArrayList<>());
        teacher.setDepartment(null);
        Teacher actuallyTeacher = teacherRepository.saveAndFlush(teacher);
        assertThat(actuallyTeacher).isNotNull();
    }

    @Test
    @DataSet(value = "teacher/teacher.yml",
        cleanBefore = true,
        skipCleaningFor = "flyway_schema_history")
    @ExpectedDataSet(value = "teacher/expected-teacher.yml")
    public void whenUpdateObject_thenExpectUpdatedRecord() {
        Teacher teacher = TeacherModelRepository.getModel2();
        teacher.setSubjects(new ArrayList<>());
        teacher.setDepartment(null);
        teacher.setId(1);
        Teacher studentActually = teacherRepository.saveAndFlush(teacher);
        assertThat(studentActually).isNotNull();
    }
}
