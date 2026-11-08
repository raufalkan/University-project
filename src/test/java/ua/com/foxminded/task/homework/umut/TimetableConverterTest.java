package ua.com.foxminded.task.homework.umut;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ua.com.foxminded.task.domain.Auditory;
import ua.com.foxminded.task.domain.Group;
import ua.com.foxminded.task.domain.Lecture;
import ua.com.foxminded.task.domain.Subject;
import ua.com.foxminded.task.domain.Teacher;
import ua.com.foxminded.task.domain.TimetableItem;
import ua.com.foxminded.task.domain.dto.AuditoryDto;
import ua.com.foxminded.task.domain.dto.TimetableItemDto;
import ua.com.foxminded.task.service.converter.ConverterToDtoService;

class TimetableConverterTest {

    private static final LocalDate LESSON_DAY = LocalDate.of(2026, 10, 19);

    @Test
    @DisplayName("subject, auditory, lecture and date are copied into the dto")
    void fullItemIsFlattened() {
        TimetableItemDto dto = ConverterToDtoService.convert(fullItem());

        assertAll("timetable item dto",
            () -> assertEquals("Math", dto.getSubjectTitle()),
            () -> assertEquals("B-204", dto.getAuditoryTitle()),
            () -> assertEquals("1", dto.getLectureTitle()),
            () -> assertEquals(LESSON_DAY, dto.getDate()));
    }

    @Test
    @DisplayName("the teacher becomes a teacher id and the teacher's first name only")
    void teacherIsFlattenedToIdAndFirstName() {
        TimetableItemDto dto = ConverterToDtoService.convert(fullItem());

        assertEquals(4, dto.getTeacherId());
        assertEquals("Ayse", dto.getTeacherTitle());
    }

    @Test
    @DisplayName("every group of the lesson becomes a group dto in the same order")
    void groupsBecomeGroupDtos() {
        TimetableItemDto dto = ConverterToDtoService.convert(fullItem());

        assertEquals(2, dto.getGroups().size());
        assertEquals("KB-22", dto.getGroups().get(1).getTitle());
    }

    @Test
    @DisplayName("an empty timetable item converts without errors")
    void emptyItemConvertsSafely() {
        TimetableItemDto dto = assertDoesNotThrow(() -> ConverterToDtoService.convert(new TimetableItem()));

        assertNull(dto.getSubjectTitle());
    }

    // Parameterized test: 0 = boundary (closed room), 1 = smallest real room, 500 = big lecture hall.
    @ParameterizedTest(name = "capacity {0} is copied")
    @ValueSource(ints = {0, 1, 500})
    @DisplayName("auditory max capacity is copied for boundary and normal values")
    void auditoryCapacityIsCopied(int capacity) {
        Auditory auditory = new Auditory();
        auditory.setAuditoryNumber("B-204");
        auditory.setMaxCapacity(capacity);

        AuditoryDto dto = ConverterToDtoService.convert(auditory);

        assertEquals(Integer.valueOf(capacity), dto.getMaxCapacity());
    }

    @Test
    @DisplayName("an auditory without a type gets empty type fields")
    void auditoryWithoutTypeHasEmptyTypeFields() {
        Auditory auditory = new Auditory();
        auditory.setAuditoryNumber("B-204");

        AuditoryDto dto = ConverterToDtoService.convert(auditory);

        assertNull(dto.getAuditoryTypeTitle());
        assertEquals(0, dto.getAuditoryTypeId());
    }

    private TimetableItem fullItem() {
        Subject subject = new Subject();
        subject.setId(1);
        subject.setTitle("Math");

        Auditory auditory = new Auditory();
        auditory.setId(2);
        auditory.setAuditoryNumber("B-204");

        Lecture lecture = new Lecture();
        lecture.setId(3);
        lecture.setNumber("1");

        Teacher teacher = new Teacher();
        teacher.setId(4);
        teacher.setFirstName("Ayse");
        teacher.setLastName("Kaya");

        TimetableItem item = new TimetableItem();
        item.setSubject(subject);
        item.setAuditory(auditory);
        item.setLecture(lecture);
        item.setTeacher(teacher);
        item.setDate(LESSON_DAY);
        item.addGroup(group(10, "KB-21"));
        item.addGroup(group(11, "KB-22"));
        return item;
    }

    private Group group(int id, String title) {
        Group group = new Group();
        group.setId(id);
        group.setTitle(title);
        group.setYearEntry(2021);
        return group;
    }
}
