package ua.com.foxminded.task.domain.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ua.com.foxminded.task.domain.Group;
import ua.com.foxminded.task.domain.Student;

public class StudentModelRepository {

    private StudentModelRepository() {
    }

    public static List<Student> getModels() {
        List<Student> students = Arrays.asList(getModel1(), getModel2(), getModel3(), getModel4(), getModel5(), getModel6());
        return new ArrayList<>(students);
    }

    public static List<Student> getModels1() {
        List<Student> students = Arrays.asList(getModel1(), getModel2());
        return new ArrayList<>(students);
    }

    public static List<Student> getModels2() {
        List<Student> students = Arrays.asList(getModel3(), getModel4());
        return new ArrayList<>(students);
    }

    public static List<Student> getModels3() {
        List<Student> students = Arrays.asList(getModel5(), getModel6());
        return new ArrayList<>(students);
    }
    
    public static Student getModel1() {
        Student student = new Student();
        Group group = GroupModelRepository.getModel11();
        group.setId(1);
        student.setFirstName("firstName1");
        student.setMiddleName("middleName1");
        student.setLastName("lastName1");
        student.setBirthday(LocalDate.of(1999, 06, 25));
        student.setIdFees(111111111);
        student.setGroup(group);
        return student;
    }

    public static Student getModel2() {
        Student student = new Student();
        Group group = GroupModelRepository.getModel11();
        group.setId(1);
        student.setFirstName("firstName2");
        student.setMiddleName("middleName2");
        student.setLastName("lastName2");
        student.setBirthday(LocalDate.of(1998, 06, 25));
        student.setIdFees(222211111);
        student.setGroup(group);
        return student;
    }

    public static Student getModel3() {
        Student student = new Student();
        Group group = GroupModelRepository.getModel12();
        group.setId(2);
        student.setFirstName("firstName3");
        student.setMiddleName("middleName3");
        student.setLastName("lastName3");
        student.setBirthday(LocalDate.of(2001, 07, 25));
        student.setIdFees(111133331);
        student.setGroup(group);
        return student;
    }

    public static Student getModel4() {
        Student student = new Student();
        Group group = GroupModelRepository.getModel12();
        group.setId(2);
        student.setFirstName("firstName4");
        student.setMiddleName("middleName4");
        student.setLastName("lastName4");
        student.setBirthday(LocalDate.of(1999, 06, 25));
        student.setIdFees(411111141);
        student.setGroup(group);
        return student;
    }

    public static Student getModel5() {
        Student student = new Student();
        Group group = GroupModelRepository.getModel12();
        group.setId(2);
        student.setFirstName("firstName5");
        student.setMiddleName("middleName5");
        student.setLastName("lastName5");
        student.setBirthday(LocalDate.of(1998, 01, 20));
        student.setIdFees(111111551);
        student.setGroup(group);
        return student;
    }

    public static Student getModel6() {
        Student student = new Student();
        Group group = GroupModelRepository.getModel13();
        group.setId(3);
        student.setFirstName("firstName6");
        student.setMiddleName("middleName6");
        student.setLastName("lastName6");
        student.setBirthday(LocalDate.of(1997, 02, 15));
        student.setIdFees(111111661);
        student.setGroup(group);
        return student;
    }

    public static Student getModel7() {
        Student student = new Student();
        Group group = GroupModelRepository.getModel1();
        group.setId(3);
        student.setFirstName("firstName7");
        student.setMiddleName("middleName7");
        student.setLastName("lastName7");
        student.setBirthday(LocalDate.parse("1997-02-15"));
        student.setIdFees(111111667);
        student.setGroup(group);
        return student;
    }
}
