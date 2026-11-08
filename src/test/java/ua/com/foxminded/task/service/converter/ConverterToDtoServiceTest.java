package ua.com.foxminded.task.service.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ua.com.foxminded.task.domain.Auditory;
import ua.com.foxminded.task.domain.AuditoryType;
import ua.com.foxminded.task.domain.Department;
import ua.com.foxminded.task.domain.Faculty;
import ua.com.foxminded.task.domain.Group;
import ua.com.foxminded.task.domain.Lecture;
import ua.com.foxminded.task.domain.Student;
import ua.com.foxminded.task.domain.Subject;
import ua.com.foxminded.task.domain.Teacher;
import ua.com.foxminded.task.domain.TimetableItem;
import ua.com.foxminded.task.domain.dto.AuditoryDto;
import ua.com.foxminded.task.domain.dto.AuditoryTypeDto;
import ua.com.foxminded.task.domain.dto.DepartmentDto;
import ua.com.foxminded.task.domain.dto.FacultyDto;
import ua.com.foxminded.task.domain.dto.GroupDto;
import ua.com.foxminded.task.domain.dto.LectureDto;
import ua.com.foxminded.task.domain.dto.StudentDto;
import ua.com.foxminded.task.domain.dto.SubjectDto;
import ua.com.foxminded.task.domain.dto.TeacherDto;
import ua.com.foxminded.task.domain.dto.TimetableItemDto;
import ua.com.foxminded.task.domain.repository.AuditoryModelRepository;
import ua.com.foxminded.task.domain.repository.AuditoryTypeModelRepository;
import ua.com.foxminded.task.domain.repository.DepartmentModelRepository;
import ua.com.foxminded.task.domain.repository.FacultyModelRepository;
import ua.com.foxminded.task.domain.repository.GroupModelRepository;
import ua.com.foxminded.task.domain.repository.LectureModelRepository;
import ua.com.foxminded.task.domain.repository.StudentModelRepository;
import ua.com.foxminded.task.domain.repository.SubjectModelRepository;
import ua.com.foxminded.task.domain.repository.TeacherModelRepository;
import ua.com.foxminded.task.domain.repository.TimetableItemModelRepository;
import ua.com.foxminded.task.domain.repository.dto.AuditoryDtoModelRepository;
import ua.com.foxminded.task.domain.repository.dto.AuditoryTypeDtoModelRepository;
import ua.com.foxminded.task.domain.repository.dto.DepartmentDtoModelRepository;
import ua.com.foxminded.task.domain.repository.dto.FacultyDtoModelRepository;
import ua.com.foxminded.task.domain.repository.dto.GroupDtoModelRepository;
import ua.com.foxminded.task.domain.repository.dto.LectureDtoModelRepository;
import ua.com.foxminded.task.domain.repository.dto.StudentDtoModelRepository;
import ua.com.foxminded.task.domain.repository.dto.SubjectDtoModelRepository;
import ua.com.foxminded.task.domain.repository.dto.TeacherDtoModelRepository;
import ua.com.foxminded.task.domain.repository.dto.TimetableItemDtoModelRepository;

public class ConverterToDtoServiceTest {

    @Test
    void whenConvertAuditory_thenRetriveDtoObject() {
        Auditory auditory = AuditoryModelRepository.getModel1();
        AuditoryDto auditoryExpected = AuditoryDtoModelRepository.getModel1();
        AuditoryDto auditoryActually = ConverterToDtoService.convert(auditory);
        assertEquals(auditoryExpected, auditoryActually);
    }

    @Test
    void whenConvertAuditoryType_thenRetriveDtoObject() {
        AuditoryType auditoryType = AuditoryTypeModelRepository.getModel1();
        AuditoryTypeDto auditoryTypeExpected = AuditoryTypeDtoModelRepository.getModel1();
        AuditoryTypeDto auditoryTypeActually = ConverterToDtoService.convert(auditoryType);
        assertEquals(auditoryTypeExpected, auditoryTypeActually);
    }

    @Test
    void whenConvertDepartment_thenRetriveDtoObject() {
        Department department = DepartmentModelRepository.getModel1();
        DepartmentDto departmentExpected = DepartmentDtoModelRepository.getModel1();
        DepartmentDto departmentActually = ConverterToDtoService.convert(department);
        assertEquals(departmentExpected, departmentActually);
    }

    @Test
    void whenConvertFaculty_thenRetriveDtoObject() {
        Faculty faculty = FacultyModelRepository.getModel1();
        FacultyDto facultyExpected = FacultyDtoModelRepository.getModel1();
        FacultyDto facultyActually = ConverterToDtoService.convert(faculty);
        assertEquals(facultyExpected, facultyActually);
    }

    @Test
    void whenConvertGroup_thenRetriveGroupDtoObject() {
        Group group = GroupModelRepository.getModel1();
        GroupDto groupExpected = GroupDtoModelRepository.getModel1();
        GroupDto groupActual = ConverterToDtoService.convert(group);
        assertEquals(groupExpected, groupActual);
    }

    @Test
    void whenConvertLecture_thenRetriveDtoObject() {
        Lecture lecture = LectureModelRepository.getModel1();
        LectureDto lectureExpected = LectureDtoModelRepository.getModel1();
        LectureDto lectureActually = ConverterToDtoService.convert(lecture);
        assertEquals(lectureExpected, lectureActually);
    }

    @Test
    void whenConvertStudent_thenRetriveDtoObject() {
        Student student = StudentModelRepository.getModel1();
        StudentDto studentExpected = StudentDtoModelRepository.getModel1();
        StudentDto studentActual = ConverterToDtoService.convert(student);
        assertEquals(studentExpected, studentActual);
    }

    @Test
    void whenConvertSubject_thenRetriveDtoObject() {
        Subject subject = SubjectModelRepository.getModel1();
        SubjectDto subjectExpected = SubjectDtoModelRepository.getModel1();
        SubjectDto subjectActually = ConverterToDtoService.convert(subject);
        assertEquals(subjectExpected, subjectActually);
    }

    @Test
    void whenConvertTeacher_thenRetriveDtoObject() {
        Teacher teacher = TeacherModelRepository.getModel1();
        TeacherDto teacherExpected = TeacherDtoModelRepository.getModel1();
        TeacherDto teacherActually = ConverterToDtoService.convert(teacher);
        assertEquals(teacherExpected, teacherActually);
    }
    
    @Test
    void whenConvertTimetableItem_thenRetriveDtoObject() {
        TimetableItem timetableItem = TimetableItemModelRepository.getModel1();
        TimetableItemDto timetableItemDtoExpected = TimetableItemDtoModelRepository.getModel1();
        TimetableItemDto timetableItemDtoActually = ConverterToDtoService.convert(timetableItem);
        assertEquals(timetableItemDtoExpected, timetableItemDtoActually);
    }
}
