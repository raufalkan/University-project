package ua.com.foxminded.task.homework.umut;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ua.com.foxminded.task.domain.Subject;
import ua.com.foxminded.task.domain.Teacher;

class TeacherModelTest {

    @Test
    @DisplayName("a new teacher has a subject list ready to use, not null")
    void newTeacherHasSubjectList() {
        Teacher teacher = new Teacher();

        assertNotNull(teacher.getSubjects());
    }

    @Test
    @DisplayName("subjects are kept in the order they were added")
    void subjectsKeepInsertionOrder() {
        Teacher teacher = new Teacher();
        Subject math = subject("Math");
        Subject physics = subject("Physics");

        teacher.addSubject(math);
        teacher.addSubject(physics);

        assertIterableEquals(Arrays.asList(math, physics), teacher.getSubjects());
    }

    @Test
    @DisplayName("the same subject can be added twice because there is no duplicate check")
    void sameSubjectCanBeAddedTwice() {
        Teacher teacher = new Teacher();
        Subject math = subject("Math");

        teacher.addSubject(math);
        teacher.addSubject(math);

        assertEquals(2, teacher.getSubjects().size());
    }

    @Test
    @DisplayName("removeSubject removes only one copy of a duplicated subject")
    void removeSubjectRemovesOnlyOneCopy() {
        Teacher teacher = new Teacher();
        Subject math = subject("Math");
        teacher.addSubject(math);
        teacher.addSubject(math);

        teacher.removeSubject(math);

        assertEquals(1, teacher.getSubjects().size());
    }

    @Test
    @DisplayName("removing a subject the teacher does not have causes no error")
    void removingUnknownSubjectCausesNoError() {
        Teacher teacher = new Teacher();
        teacher.addSubject(subject("Math"));

        assertDoesNotThrow(() -> teacher.removeSubject(subject("Chemistry")));
    }

    @Test
    @DisplayName("setSubjects stores the given list object itself, not a copy")
    void setSubjectsKeepsSameListObject() {
        Teacher teacher = new Teacher();
        List<Subject> subjects = new ArrayList<>();

        teacher.setSubjects(subjects);

        assertSame(subjects, teacher.getSubjects());
    }

    @Test
    @DisplayName("teachers with the same name but different subjects are not equal")
    void differentSubjectsMeansDifferentTeacher() {
        Teacher first = teacher("Ayse", "Kaya", 2001);
        first.addSubject(subject("Math"));
        Teacher second = teacher("Ayse", "Kaya", 2001);
        second.addSubject(subject("Physics"));

        assertNotEquals(first, second);
    }

    private Teacher teacher(String firstName, String lastName, int idFees) {
        Teacher teacher = new Teacher();
        teacher.setFirstName(firstName);
        teacher.setLastName(lastName);
        teacher.setIdFees(idFees);
        return teacher;
    }

    private Subject subject(String title) {
        Subject subject = new Subject();
        subject.setTitle(title);
        return subject;
    }
}
