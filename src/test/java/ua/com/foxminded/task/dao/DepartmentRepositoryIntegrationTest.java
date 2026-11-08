package ua.com.foxminded.task.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import ua.com.foxminded.task.domain.Department;
import ua.com.foxminded.task.domain.repository.DepartmentModelRepository;

@DBRider
@SpringBootTest
public class DepartmentRepositoryIntegrationTest {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    @DataSet(cleanBefore = true, skipCleaningFor = "flyway_schema_history")
    public void whenRepositoryHasNotRecords_thenReturnEmptyList() {
        List<Department> departments = departmentRepository.findAll();
        assertThat(departments).isNotNull().isEmpty();
    }

    @Test
    @DataSet(value = "department/departments.yml",
        cleanBefore = true,
        skipCleaningFor = "flyway_schema_history")
    @ExpectedDataSet(value = "department/expected-departments.yml")
    public void whenRepositoryHasRecords_thenReturnNonEmptyList() {
        List<Department> departments = departmentRepository.findAll();
        assertThat(departments).isNotNull().isNotEmpty().hasSize(3);
    }

    @Test
    @DataSet(value = "department/departments.yml",
        cleanBefore = true,
        skipCleaningFor = "flyway_schema_history")
    public void whenPutAtTableDbObjects_thenGetThisObjectsFindById() {
        Department expectedDepartment = DepartmentModelRepository.getModel3();
        expectedDepartment.setFaculty(null);
        int id = 30;
        Department actuallyDepartment = departmentRepository.findById(id).get();
        assertThat(expectedDepartment).isEqualTo(actuallyDepartment);
    }

    @Test
    @DataSet(value = "department/departments.yml",
        cleanBefore = true,
        skipCleaningFor = "flyway_schema_history")
    public void whenFindByTitle_thenDepartmentReturned() {
        String title = "department2";
        Department departmentActually = departmentRepository.findByTitle(title);
        assertThat(departmentActually.getTitle()).isNotNull().isEqualTo(title);
    }

    @Test
    @DataSet(cleanBefore = true, skipCleaningFor = "flyway_schema_history")
    @ExpectedDataSet(value = "department/expected-department.yml")
    public void whenSaveObject_thenExpectRecord() {
        Department department = DepartmentModelRepository.getModel2();
        department.setFaculty(null);
        Department actuallyDepartment = departmentRepository.saveAndFlush(department);
        assertThat(actuallyDepartment).isNotNull();
    }

    @Test
    @DataSet(value = "department/department.yml",
        cleanBefore = true,
        skipCleaningFor = "flyway_schema_history")
    @ExpectedDataSet(value = "department/expected-department.yml")
    public void whenUpdateObject_thenExpectUpdatedRecord() {
        Department department = DepartmentModelRepository.getModel2();
        department.setId(1);
        department.setFaculty(null);
        Department actuallyDepartment = departmentRepository.saveAndFlush(department);
        assertThat(actuallyDepartment).isNotNull();
    }
}
