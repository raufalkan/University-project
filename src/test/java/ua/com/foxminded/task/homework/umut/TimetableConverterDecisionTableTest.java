package ua.com.foxminded.task.homework.umut;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import ua.com.foxminded.task.domain.Auditory;
import ua.com.foxminded.task.domain.Group;
import ua.com.foxminded.task.domain.Lecture;
import ua.com.foxminded.task.domain.Subject;
import ua.com.foxminded.task.domain.Teacher;
import ua.com.foxminded.task.domain.TimetableItem;
import ua.com.foxminded.task.domain.dto.TimetableItemDto;
import ua.com.foxminded.task.service.converter.ConverterToDtoService;

public class TimetableConverterDecisionTableTest {

    private static final String YES = "Y";
    private static final String DONE = "X";

    private static final int ITEM_ID = 100;
    private static final LocalDate LESSON_DAY = LocalDate.of(2026, 11, 10);

    private static final int SUBJECT_ID = 1;
    private static final String SUBJECT_TITLE = "Math";

    private static final int AUDITORY_ID = 2;
    private static final String AUDITORY_NUMBER = "B-204";

    private static final int LECTURE_ID = 3;
    private static final String LECTURE_NUMBER = "1";

    private static final int TEACHER_ID = 4;
    private static final String TEACHER_FIRST_NAME = "Ayse";

    @ParameterizedTest(name = "{0}: subject={1} auditory={2} lecture={3} teacher={4} -> A1={5} A2={6} A3={7} A4={8}")
    @DisplayName("Decision table for convert(TimetableItem)")
    @CsvSource({
        "R1,  N, N, N, N,  -, -, -, -",
        "R2,  N, N, N, Y,  -, -, -, X",
        "R3,  N, N, Y, N,  -, -, X, -",
        "R4,  N, N, Y, Y,  -, -, X, X",
        "R5,  N, Y, N, N,  -, X, -, -",
        "R6,  N, Y, N, Y,  -, X, -, X",
        "R7,  N, Y, Y, N,  -, X, X, -",
        "R8,  N, Y, Y, Y,  -, X, X, X",
        "R9,  Y, N, N, N,  X, -, -, -",
        "R10, Y, N, N, Y,  X, -, -, X",
        "R11, Y, N, Y, N,  X, -, X, -",
        "R12, Y, N, Y, Y,  X, -, X, X",
        "R13, Y, Y, N, N,  X, X, -, -",
        "R14, Y, Y, N, Y,  X, X, -, X",
        "R15, Y, Y, Y, N,  X, X, X, -",
        "R16, Y, Y, Y, Y,  X, X, X, X"
    })
    void ruleOfDecisionTable(String rule,
                             String hasSubject, String hasAuditory, String hasLecture, String hasTeacher,
                             String subjectAction, String auditoryAction, String lectureAction,
                             String teacherAction) {
        TimetableItem item = itemFor(hasSubject, hasAuditory, hasLecture, hasTeacher);

        TimetableItemDto dto = ConverterToDtoService.convert(item);

        assertAll(rule,
            () -> assertAlwaysCopied(dto),
            () -> assertSubject(dto, DONE.equals(subjectAction)),
            () -> assertAuditory(dto, DONE.equals(auditoryAction)),
            () -> assertLecture(dto, DONE.equals(lectureAction)),
            () -> assertTeacher(dto, DONE.equals(teacherAction)));
    }

    private static TimetableItem itemFor(String hasSubject, String hasAuditory,
                                         String hasLecture, String hasTeacher) {
        Group group = new Group();
        group.setId(10);
        group.setTitle("KB-21");
        group.setYearEntry(2021);

        TimetableItem item = new TimetableItem();
        item.setId(ITEM_ID);
        item.setDate(LESSON_DAY);
        item.addGroup(group);

        if (YES.equals(hasSubject)) {
            Subject subject = new Subject();
            subject.setId(SUBJECT_ID);
            subject.setTitle(SUBJECT_TITLE);
            item.setSubject(subject);
        }
        if (YES.equals(hasAuditory)) {
            Auditory auditory = new Auditory();
            auditory.setId(AUDITORY_ID);
            auditory.setAuditoryNumber(AUDITORY_NUMBER);
            item.setAuditory(auditory);
        }
        if (YES.equals(hasLecture)) {
            Lecture lecture = new Lecture();
            lecture.setId(LECTURE_ID);
            lecture.setNumber(LECTURE_NUMBER);
            item.setLecture(lecture);
        }
        if (YES.equals(hasTeacher)) {
            Teacher teacher = new Teacher();
            teacher.setId(TEACHER_ID);
            teacher.setFirstName(TEACHER_FIRST_NAME);
            teacher.setLastName("Kaya");
            item.setTeacher(teacher);
        }
        return item;
    }

    private static void assertAlwaysCopied(TimetableItemDto dto) {
        assertEquals(ITEM_ID, dto.getId());
        assertEquals(LESSON_DAY, dto.getDate());
        assertEquals(1, dto.getGroups().size());
        assertEquals("KB-21", dto.getGroups().get(0).getTitle());
    }

    private static void assertSubject(TimetableItemDto dto, boolean copied) {
        if (copied) {
            assertEquals(SUBJECT_TITLE, dto.getSubjectTitle());
            assertEquals(SUBJECT_ID, dto.getSubjectId());
        } else {
            assertNull(dto.getSubjectTitle());
            assertEquals(0, dto.getSubjectId());
        }
    }

    private static void assertAuditory(TimetableItemDto dto, boolean copied) {
        if (copied) {
            assertEquals(AUDITORY_NUMBER, dto.getAuditoryTitle());
            assertEquals(AUDITORY_ID, dto.getAuditoryId());
        } else {
            assertNull(dto.getAuditoryTitle());
            assertEquals(0, dto.getAuditoryId());
        }
    }

    private static void assertLecture(TimetableItemDto dto, boolean copied) {
        if (copied) {
            assertEquals(LECTURE_NUMBER, dto.getLectureTitle());
            assertEquals(LECTURE_ID, dto.getLectureId());
        } else {
            assertNull(dto.getLectureTitle());
            assertEquals(0, dto.getLectureId());
        }
    }

    private static void assertTeacher(TimetableItemDto dto, boolean copied) {
        if (copied) {
            assertEquals(TEACHER_FIRST_NAME, dto.getTeacherTitle());
            assertEquals(TEACHER_ID, dto.getTeacherId());
        } else {
            assertNull(dto.getTeacherTitle());
            assertEquals(0, dto.getTeacherId());
        }
    }
}
