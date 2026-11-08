package ua.com.foxminded.task.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ua.com.foxminded.task.domain.repository.SubjectModelRepository;
import ua.com.foxminded.task.domain.repository.TeacherModelRepository;

public class TeacherTest {

    @Test
    public void whenAddSubjectToTeacher_thenTeacherContainsSubject() {
        Teacher teacher = TeacherModelRepository.getEmptyModel();
        Subject subject = SubjectModelRepository.getModel1();
        teacher.addSubject(subject);
        assertTrue(teacher.getSubjects().contains(subject));
    }

    @Test
    public void whenRemoveSubjectFromTeacher_thenTeacherNonContainsSubject() {
        Teacher teacher = TeacherModelRepository.getModel1();
        Subject subject = SubjectModelRepository.getModel1();
        teacher.removeSubject(subject);
        assertFalse(teacher.getSubjects().contains(subject));
    }
}
