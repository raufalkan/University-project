package ua.com.foxminded.task.homework.umut;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ua.com.foxminded.task.domain.Auditory;
import ua.com.foxminded.task.domain.Group;
import ua.com.foxminded.task.domain.Lecture;
import ua.com.foxminded.task.domain.Subject;
import ua.com.foxminded.task.domain.Teacher;
import ua.com.foxminded.task.domain.TimetableItem;
import ua.com.foxminded.task.domain.dto.TimetableItemDto;
import ua.com.foxminded.task.service.converter.ConverterToDtoService;

public class TimetableConverterBasisPathTest {

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

    @Test
    @DisplayName("P1 [F,F,F,F]: no subject, auditory, lecture, teacher -> only id, groups, date copied")
    void path1_allFieldsNull_returnsDtoWithoutOptionalFields() {
        TimetableItem item = baseItem();

        TimetableItemDto dto = ConverterToDtoService.convert(item);

        assertAll("path 1: 1-2-4-6-7-9-10-12",
            () -> assertCommonFieldsCopied(dto),
            () -> assertSubjectSkipped(dto),
            () -> assertAuditorySkipped(dto),
            () -> assertLectureSkipped(dto),
            () -> assertTeacherSkipped(dto));
    }

    @Test
    @DisplayName("P2 [T,F,F,F]: only subject present -> subject title and id copied")
    void path2_subjectOnlyPresent_subjectCopied() {
        TimetableItem item = baseItem();
        item.setSubject(subject());

        TimetableItemDto dto = ConverterToDtoService.convert(item);

        assertAll("path 2: 1-2-3-4-6-7-9-10-12",
            () -> assertCommonFieldsCopied(dto),
            () -> assertSubjectCopied(dto),
            () -> assertAuditorySkipped(dto),
            () -> assertLectureSkipped(dto),
            () -> assertTeacherSkipped(dto));
    }

    @Test
    @DisplayName("P3 [F,T,F,F]: only auditory present -> auditory number and id copied")
    void path3_auditoryOnlyPresent_auditoryCopied() {
        TimetableItem item = baseItem();
        item.setAuditory(auditory());

        TimetableItemDto dto = ConverterToDtoService.convert(item);

        assertAll("path 3: 1-2-4-5-6-7-9-10-12",
            () -> assertCommonFieldsCopied(dto),
            () -> assertSubjectSkipped(dto),
            () -> assertAuditoryCopied(dto),
            () -> assertLectureSkipped(dto),
            () -> assertTeacherSkipped(dto));
    }

    @Test
    @DisplayName("P4 [F,F,T,F]: only lecture present -> lecture number and id copied")
    void path4_lectureOnlyPresent_lectureCopied() {
        TimetableItem item = baseItem();
        item.setLecture(lecture());

        TimetableItemDto dto = ConverterToDtoService.convert(item);

        assertAll("path 4: 1-2-4-6-7-8-9-10-12",
            () -> assertCommonFieldsCopied(dto),
            () -> assertSubjectSkipped(dto),
            () -> assertAuditorySkipped(dto),
            () -> assertLectureCopied(dto),
            () -> assertTeacherSkipped(dto));
    }

    @Test
    @DisplayName("P5 [F,F,F,T]: only teacher present -> teacher first name and id copied")
    void path5_teacherOnlyPresent_teacherCopied() {
        TimetableItem item = baseItem();
        item.setTeacher(teacher());

        TimetableItemDto dto = ConverterToDtoService.convert(item);

        assertAll("path 5: 1-2-4-6-7-9-10-11-12",
            () -> assertCommonFieldsCopied(dto),
            () -> assertSubjectSkipped(dto),
            () -> assertAuditorySkipped(dto),
            () -> assertLectureSkipped(dto),
            () -> assertTeacherCopied(dto));
    }

    private static TimetableItem baseItem() {
        Group group = new Group();
        group.setId(10);
        group.setTitle("KB-21");
        group.setYearEntry(2021);

        TimetableItem item = new TimetableItem();
        item.setId(ITEM_ID);
        item.setDate(LESSON_DAY);
        item.addGroup(group);
        return item;
    }

    private static Subject subject() {
        Subject subject = new Subject();
        subject.setId(SUBJECT_ID);
        subject.setTitle(SUBJECT_TITLE);
        return subject;
    }

    private static Auditory auditory() {
        Auditory auditory = new Auditory();
        auditory.setId(AUDITORY_ID);
        auditory.setAuditoryNumber(AUDITORY_NUMBER);
        return auditory;
    }

    private static Lecture lecture() {
        Lecture lecture = new Lecture();
        lecture.setId(LECTURE_ID);
        lecture.setNumber(LECTURE_NUMBER);
        return lecture;
    }

    private static Teacher teacher() {
        Teacher teacher = new Teacher();
        teacher.setId(TEACHER_ID);
        teacher.setFirstName(TEACHER_FIRST_NAME);
        teacher.setLastName("Kaya");
        return teacher;
    }

    private static void assertCommonFieldsCopied(TimetableItemDto dto) {
        assertEquals(ITEM_ID, dto.getId());
        assertEquals(LESSON_DAY, dto.getDate());
        assertEquals(1, dto.getGroups().size());
        assertEquals("KB-21", dto.getGroups().get(0).getTitle());
    }

    private static void assertSubjectCopied(TimetableItemDto dto) {
        assertEquals(SUBJECT_TITLE, dto.getSubjectTitle());
        assertEquals(SUBJECT_ID, dto.getSubjectId());
    }

    private static void assertSubjectSkipped(TimetableItemDto dto) {
        assertNull(dto.getSubjectTitle());
        assertEquals(0, dto.getSubjectId());
    }

    private static void assertAuditoryCopied(TimetableItemDto dto) {
        assertEquals(AUDITORY_NUMBER, dto.getAuditoryTitle());
        assertEquals(AUDITORY_ID, dto.getAuditoryId());
    }

    private static void assertAuditorySkipped(TimetableItemDto dto) {
        assertNull(dto.getAuditoryTitle());
        assertEquals(0, dto.getAuditoryId());
    }

    private static void assertLectureCopied(TimetableItemDto dto) {
        assertEquals(LECTURE_NUMBER, dto.getLectureTitle());
        assertEquals(LECTURE_ID, dto.getLectureId());
    }

    private static void assertLectureSkipped(TimetableItemDto dto) {
        assertNull(dto.getLectureTitle());
        assertEquals(0, dto.getLectureId());
    }

    private static void assertTeacherCopied(TimetableItemDto dto) {
        assertEquals(TEACHER_FIRST_NAME, dto.getTeacherTitle());
        assertEquals(TEACHER_ID, dto.getTeacherId());
    }

    private static void assertTeacherSkipped(TimetableItemDto dto) {
        assertNull(dto.getTeacherTitle());
        assertEquals(0, dto.getTeacherId());
    }
}
