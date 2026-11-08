package ua.com.foxminded.task.homework.umut;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ua.com.foxminded.task.domain.Department;
import ua.com.foxminded.task.domain.Group;
import ua.com.foxminded.task.domain.Student;
import ua.com.foxminded.task.domain.Subject;
import ua.com.foxminded.task.domain.Teacher;
import ua.com.foxminded.task.domain.dto.StudentDto;
import ua.com.foxminded.task.domain.dto.SubjectDto;
import ua.com.foxminded.task.domain.dto.TeacherDto;
import ua.com.foxminded.task.service.converter.ConverterToDtoService;

class PersonConverterTest {

    @Test
    @DisplayName("student id and fee id are copied to the dto")
    void studentIdsAreCopied() {
        StudentDto dto = ConverterToDtoService.convert(fullStudent(7));

        assertEquals(7, dto.getId());
        assertEquals(1001, dto.getIdFees());
    }

    @Test
    @DisplayName("the student's group object becomes a group id and a group title")
    void studentGroupIsFlattened() {
        StudentDto dto = ConverterToDtoService.convert(fullStudent(7));

        assertEquals(3, dto.getGroupId());
        assertEquals("KB-21", dto.getGroupTitle());
    }

    @Test
    @DisplayName("a student without a group gets empty group fields")
    void studentWithoutGroupHasEmptyGroupFields() {
        Student student = new Student();
        student.setFirstName("Ali");

        StudentDto dto = ConverterToDtoService.convert(student);

        assertNull(dto.getGroupTitle());
        assertEquals(0, dto.getGroupId());
    }

    @Test
    @DisplayName("converting a null student throws NullPointerException")
    void convertingNullStudentThrows() {
        assertThrows(NullPointerException.class, () -> ConverterToDtoService.convert((Student) null));
    }

    @Test
    @DisplayName("every conversion creates a brand new dto object")
    void everyConversionCreatesNewDto() {
        Student student = fullStudent(7);

        StudentDto first = ConverterToDtoService.convert(student);
        StudentDto second = ConverterToDtoService.convert(student);

        assertNotSame(first, second);
    }

    @Test
    @DisplayName("student dtos with the same data are equal even when their ids differ")
    void studentDtoEqualityIgnoresId() {
        StudentDto first = ConverterToDtoService.convert(fullStudent(7));
        StudentDto second = ConverterToDtoService.convert(fullStudent(8));

        assertEquals(first, second);
    }

    @Test
    @DisplayName("teacher subjects become subject dtos in the same order")
    void teacherSubjectsAreConvertedInOrder() {
        Teacher teacher = new Teacher();
        teacher.addSubject(subject(1, "Math"));
        teacher.addSubject(subject(2, "Physics"));

        TeacherDto dto = ConverterToDtoService.convert(teacher);
        List<String> titles = dto.getSubjects().stream()
                .map(SubjectDto::getTitle)
                .collect(Collectors.toList());

        assertIterableEquals(Arrays.asList("Math", "Physics"), titles);
    }

    @Test
    @DisplayName("the teacher's department becomes a department id and a department title")
    void teacherDepartmentIsFlattened() {
        Department department = new Department();
        department.setId(5);
        department.setTitle("Software Eng.");
        Teacher teacher = new Teacher();
        teacher.setDepartment(department);

        TeacherDto dto = ConverterToDtoService.convert(teacher);

        assertEquals(5, dto.getDepartmentId());
        assertEquals("Software Eng.", dto.getDepartmentTitle());
    }

    private Student fullStudent(int id) {
        Group group = new Group();
        group.setId(3);
        group.setTitle("KB-21");
        group.setYearEntry(2021);
        Student student = new Student();
        student.setId(id);
        student.setFirstName("Ali");
        student.setMiddleName("Can");
        student.setLastName("Yilmaz");
        student.setBirthday(LocalDate.of(2003, 4, 15));
        student.setIdFees(1001);
        student.setGroup(group);
        return student;
    }

    private Subject subject(int id, String title) {
        Subject subject = new Subject();
        subject.setId(id);
        subject.setTitle(title);
        return subject;
    }
}
