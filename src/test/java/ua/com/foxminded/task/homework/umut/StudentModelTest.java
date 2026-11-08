package ua.com.foxminded.task.homework.umut;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Modifier;
import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ua.com.foxminded.task.domain.Group;
import ua.com.foxminded.task.domain.Person;
import ua.com.foxminded.task.domain.Student;
import ua.com.foxminded.task.domain.Teacher;

class StudentModelTest {

    @Test
    @DisplayName("a new student is not in any group yet")
    void newStudentHasNoGroup() {
        Student student = new Student();

        assertNull(student.getGroup());
    }

    @Test
    @DisplayName("Student inherits from Person")
    void studentIsAPerson() {
        assertInstanceOf(Person.class, new Student());
    }

    @Test
    @DisplayName("Person is abstract, so only Student or Teacher objects can be created")
    void personIsAbstract() {
        assertTrue(Modifier.isAbstract(Person.class.getModifiers()));
    }

    @Test
    @DisplayName("middle name and birthday are not part of student equality")
    void middleNameAndBirthdayAreIgnored() {
        Student first = student("Ali", "Yilmaz", 1001);
        first.setMiddleName("Can");
        first.setBirthday(LocalDate.of(2003, 4, 15));
        Student second = student("Ali", "Yilmaz", 1001);
        second.setMiddleName("Emre");
        second.setBirthday(LocalDate.of(2004, 9, 1));

        assertEquals(first, second);
    }

    @Test
    @DisplayName("students with the same name but different fee ids are different people")
    void differentFeeIdMeansDifferentStudent() {
        assertNotEquals(student("Ali", "Yilmaz", 1001), student("Ali", "Yilmaz", 1002));
    }

    @Test
    @DisplayName("the same person in two different groups is not equal")
    void differentGroupMeansDifferentStudent() {
        Student first = student("Ali", "Yilmaz", 1001);
        first.setGroup(group("KB-21"));
        Student second = student("Ali", "Yilmaz", 1001);
        second.setGroup(group("KB-22"));

        assertNotEquals(first, second);
    }

    @Test
    @DisplayName("a student is never equal to a teacher with the same personal data")
    void studentIsNotEqualToTeacher() {
        Student student = student("Ali", "Yilmaz", 1001);
        Teacher teacher = new Teacher();
        teacher.setFirstName("Ali");
        teacher.setLastName("Yilmaz");
        teacher.setIdFees(1001);

        assertNotEquals(student, teacher);
    }

    private Student student(String firstName, String lastName, int idFees) {
        Student student = new Student();
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setIdFees(idFees);
        return student;
    }

    private Group group(String title) {
        Group group = new Group();
        group.setTitle(title);
        group.setYearEntry(2021);
        return group;
    }
}
