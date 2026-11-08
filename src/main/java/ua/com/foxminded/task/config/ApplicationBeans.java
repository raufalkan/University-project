package ua.com.foxminded.task.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ua.com.foxminded.task.domain.dto.AuditoryDto;
import ua.com.foxminded.task.domain.dto.AuditoryTypeDto;
import ua.com.foxminded.task.domain.dto.DepartmentDto;
import ua.com.foxminded.task.domain.dto.FacultyDto;
import ua.com.foxminded.task.domain.dto.GroupDto;
import ua.com.foxminded.task.domain.dto.LectureDto;
import ua.com.foxminded.task.domain.dto.StudentDto;
import ua.com.foxminded.task.domain.dto.SubjectDto;
import ua.com.foxminded.task.domain.dto.TeacherDto;
import ua.com.foxminded.task.service.AuditoryService;
import ua.com.foxminded.task.service.AuditoryTypeService;
import ua.com.foxminded.task.service.DepartmentService;
import ua.com.foxminded.task.service.FacultyService;
import ua.com.foxminded.task.service.GroupService;
import ua.com.foxminded.task.service.LectureService;
import ua.com.foxminded.task.service.StudentService;
import ua.com.foxminded.task.service.SubjectService;
import ua.com.foxminded.task.service.TeacherService;
import ua.com.foxminded.task.validation.validator.property.unique.Command;
import ua.com.foxminded.task.validation.validator.property.unique.impl.AuditoryCommand;
import ua.com.foxminded.task.validation.validator.property.unique.impl.AuditoryTypeCommand;
import ua.com.foxminded.task.validation.validator.property.unique.impl.DepartmentCommand;
import ua.com.foxminded.task.validation.validator.property.unique.impl.FacultyCommand;
import ua.com.foxminded.task.validation.validator.property.unique.impl.GroupCommand;
import ua.com.foxminded.task.validation.validator.property.unique.impl.LectureCommand;
import ua.com.foxminded.task.validation.validator.property.unique.impl.StudentCommand;
import ua.com.foxminded.task.validation.validator.property.unique.impl.SubjectCommand;
import ua.com.foxminded.task.validation.validator.property.unique.impl.TeacherCommand;

@Configuration
public class ApplicationBeans {

    @Autowired
    private AuditoryService auditoryService;
    @Autowired
    private AuditoryTypeService auditoryTypeService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private FacultyService facultyService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private LectureService lectureService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private TeacherService teacherService;

    @Bean
    @Qualifier("uniqueValidationCommandMap")
    public Map<String, Command> getUniqueValidationCommandMap() {
        Map<String, Command> uniqueValidationCommandMap = new HashMap<>();
        uniqueValidationCommandMap.put(AuditoryDto.class.getName(), new AuditoryCommand(auditoryService));
        uniqueValidationCommandMap.put(AuditoryTypeDto.class.getName(), new AuditoryTypeCommand(auditoryTypeService));
        uniqueValidationCommandMap.put(DepartmentDto.class.getName(), new DepartmentCommand(departmentService));
        uniqueValidationCommandMap.put(FacultyDto.class.getName(), new FacultyCommand(facultyService));
        uniqueValidationCommandMap.put(GroupDto.class.getName(), new GroupCommand(groupService));
        uniqueValidationCommandMap.put(LectureDto.class.getName(), new LectureCommand(lectureService));
        uniqueValidationCommandMap.put(StudentDto.class.getName(), new StudentCommand(studentService));
        uniqueValidationCommandMap.put(SubjectDto.class.getName(), new SubjectCommand(subjectService));
        uniqueValidationCommandMap.put(TeacherDto.class.getName(), new TeacherCommand(teacherService));
        return uniqueValidationCommandMap;
    }

}
