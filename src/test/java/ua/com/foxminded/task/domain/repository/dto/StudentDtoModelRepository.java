package ua.com.foxminded.task.domain.repository.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ua.com.foxminded.task.domain.Group;
import ua.com.foxminded.task.domain.dto.StudentDto;
import ua.com.foxminded.task.domain.repository.GroupModelRepository;

public class StudentDtoModelRepository {

    private StudentDtoModelRepository() {
    }

    public static List<StudentDto> getModels() {
        List<StudentDto> students = Arrays.asList(getModel1(), getModel2(), getModel3(), getModel4(), getModel5(), getModel6());
        return new ArrayList<>(students);
    }

    public static List<StudentDto> getModels1() {
        List<StudentDto> students = Arrays.asList(getModel1(), getModel2());
        return new ArrayList<>(students);
    }

    public static List<StudentDto> getModels2() {
        List<StudentDto> students = Arrays.asList(getModel3(), getModel4());
        return new ArrayList<>(students);
    }

    public static List<StudentDto> getModels3() {
        List<StudentDto> students = Arrays.asList(getModel5(), getModel6());
        return new ArrayList<>(students);
    }

    public static StudentDto getModel1() {
        StudentDto student = new StudentDto();
        Group group = GroupModelRepository.getModel11();
        group.setId(1);
        student.setFirstName("firstName1");
        student.setMiddleName("middleName1");
        student.setLastName("lastName1");
        student.setBirthday(LocalDate.parse("1999-06-25"));
        student.setIdFees(111111111);
        student.setGroupTitle(group.getTitle());
        student.setGroupId(group.getId());
        return student;
    }

    public static StudentDto getModel2() {
        StudentDto student = new StudentDto();
        Group group = GroupModelRepository.getModel11();
        group.setId(1);
        student.setFirstName("firstName2");
        student.setMiddleName("middleName2");
        student.setLastName("lastName2");
        student.setBirthday(LocalDate.parse("1998-06-25"));
        student.setIdFees(222211111);
        student.setGroupTitle(group.getTitle());
        student.setGroupId(group.getId());
        return student;
    }

    public static StudentDto getModel3() {
        StudentDto student = new StudentDto();
        Group group = GroupModelRepository.getModel12();
        group.setId(2);
        student.setFirstName("firstName3");
        student.setMiddleName("middleName3");
        student.setLastName("lastName3");
        student.setBirthday(LocalDate.parse("2001-07-25"));
        student.setIdFees(111133331);
        student.setGroupTitle(group.getTitle());
        student.setGroupId(group.getId());
        return student;
    }

    public static StudentDto getModel4() {
        StudentDto student = new StudentDto();
        Group group = GroupModelRepository.getModel12();
        group.setId(2);
        student.setFirstName("firstName4");
        student.setMiddleName("middleName4");
        student.setLastName("lastName4");
        student.setBirthday(LocalDate.parse("1999-06-25"));
        student.setIdFees(411111141);
        student.setGroupTitle(group.getTitle());
        student.setGroupId(group.getId());;
        return student;
    }

    public static StudentDto getModel5() {
        StudentDto student = new StudentDto();
        Group group = GroupModelRepository.getModel12();
        group.setId(2);
        student.setFirstName("firstName5");
        student.setMiddleName("middleName5");
        student.setLastName("lastName5");
        student.setBirthday(LocalDate.parse("1998-01-20"));
        student.setIdFees(111111551);
        student.setGroupTitle(group.getTitle());
        student.setGroupId(group.getId());
        return student;
    }

    public static StudentDto getModel6() {
        StudentDto student = new StudentDto();
        Group group = GroupModelRepository.getModel13();
        group.setId(3);
        student.setFirstName("firstName6");
        student.setMiddleName("middleName6");
        student.setLastName("lastName6");
        student.setBirthday(LocalDate.parse("1997-02-15"));
        student.setIdFees(111111661);
        student.setGroupTitle(group.getTitle());
        student.setGroupId(group.getId());
        return student;
    }

    public static StudentDto getModel7() {
        StudentDto student = new StudentDto();
        Group group = GroupModelRepository.getModel13();
        group.setId(3);
        student.setFirstName("firstName7");
        student.setMiddleName("middleName7");
        student.setLastName("lastName7");
        student.setBirthday(LocalDate.parse("1997-02-15"));
        student.setIdFees(111111667);
        student.setGroupTitle(group.getTitle());
        student.setGroupId(group.getId());
        return student;
    }
}
