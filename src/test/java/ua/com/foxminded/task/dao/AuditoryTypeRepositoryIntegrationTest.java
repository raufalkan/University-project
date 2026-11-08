package ua.com.foxminded.task.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import ua.com.foxminded.task.domain.AuditoryType;
import ua.com.foxminded.task.domain.repository.AuditoryTypeModelRepository;

@DBRider
@SpringBootTest
public class AuditoryTypeRepositoryIntegrationTest {
    @Autowired
    private AuditoryTypeRepository auditoryTypeRepository;

    @Test
    @DataSet(cleanBefore = true,
        skipCleaningFor = "flyway_schema_history")
    public void whenRepositoryHasNotRecords_thenReturnEmptyList() {
        List<AuditoryType> auditoryTypes = auditoryTypeRepository.findAll();
        assertThat(auditoryTypes).isNotNull().isEmpty();
    }

    @Test
    @DataSet(value = "auditory-type/auditorytypes.yml",
        cleanBefore = true,
        skipCleaningFor = "flyway_schema_history")
    @ExpectedDataSet(value = "auditory-type/expected-auditorytypes.yml")
    public void whenRepositoryHasRecords_thenReturnNonEmptyList() {
        List<AuditoryType> auditoryTypes = auditoryTypeRepository.findAll();
        assertThat(auditoryTypes).isNotNull().isNotEmpty().hasSize(6);
    }

    @Test
    @DataSet(value = "auditory-type/auditorytypes.yml",
        cleanBefore = true,
        skipCleaningFor = "flyway_schema_history")
    public void whenPutAtTableDbObjects_thenGetThisObjectsFindById() {
        AuditoryType expectedAuditoryType = AuditoryTypeModelRepository.getModel3();
        int id = 3;
        AuditoryType actuallyAuditoryType = auditoryTypeRepository.findById(id).get();
        assertThat(expectedAuditoryType).isEqualTo(actuallyAuditoryType);
    }

    @Test
    @DataSet(value = "auditory-type/auditorytypes.yml",
        cleanBefore = true,
        skipCleaningFor = "flyway_schema_history")
    public void whenFindByType_thenAuditoryTypeReturned() {
        String type = "Laboratory";
        AuditoryType auditoryTypeActually = auditoryTypeRepository.findByType(type);
        assertThat(auditoryTypeActually.getType()).isNotNull().isEqualTo(type);
    }

    @Test
    @DataSet(cleanBefore = true, skipCleaningFor = "flyway_schema_history")
    @ExpectedDataSet(value = "auditory-type/expected-auditorytype.yml")
    public void whenSaveObject_thenExpectRecord() {
        AuditoryType auditoryType = AuditoryTypeModelRepository.getModel2();
        AuditoryType actuallyAuditoryType = auditoryTypeRepository.saveAndFlush(auditoryType);
        assertThat(actuallyAuditoryType).isNotNull();
    }

    @Test
    @DataSet(value = "auditory-type/auditorytype.yml",
        cleanBefore = true,
        skipCleaningFor = "flyway_schema_history")
    @ExpectedDataSet(value = "auditory-type/expected-auditorytype.yml")
    public void whenUpdateObject_thenExpectUpdatedRecord() {
        AuditoryType auditoryType = AuditoryTypeModelRepository.getModel2();
        auditoryType.setId(1);
        AuditoryType actuallyAuditoryType = auditoryTypeRepository.saveAndFlush(auditoryType);
        assertThat(actuallyAuditoryType).isNotNull();
    }
}
