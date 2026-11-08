package ua.com.foxminded.task.homework.umut;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ua.com.foxminded.task.domain.Group;
import ua.com.foxminded.task.domain.Subject;
import ua.com.foxminded.task.domain.TimetableItem;

class TimetableItemModelTest {

    private static final LocalDate MONDAY = LocalDate.of(2026, 10, 19);

    @Test
    @DisplayName("a new timetable item has no groups, no subject and no date")
    void newItemIsEmpty() {
        TimetableItem item = new TimetableItem();

        assertAll("new timetable item",
            () -> assertTrue(item.getGroups().isEmpty()),
            () -> assertNull(item.getSubject()),
            () -> assertNull(item.getDate()));
    }

    @Test
    @DisplayName("removeGroup also works with a different but equal group object")
    void removeGroupUsesEquals() {
        TimetableItem item = new TimetableItem();
        item.addGroup(group("KB-21"));

        item.removeGroup(group("KB-21"));

        assertTrue(item.getGroups().isEmpty());
    }

    @Test
    @DisplayName("two items with the same lesson data are equal even if their ids differ")
    void sameLessonDataMeansEqual() {
        TimetableItem first = lesson(MONDAY);
        first.setId(1);
        TimetableItem second = lesson(MONDAY);
        second.setId(2);

        assertEquals(first, second);
    }

    @Test
    @DisplayName("the same lesson on another day is a different item")
    void differentDateMeansDifferentItem() {
        assertNotEquals(lesson(MONDAY), lesson(MONDAY.plusDays(1)));
    }

    @Test
    @DisplayName("hashCode still works when the group list is null")
    void hashCodeIsNullSafe() {
        TimetableItem item = lesson(MONDAY);
        item.setGroups(null);

        assertDoesNotThrow(() -> item.hashCode());
    }

    @Test
    @DisplayName("addGroup fails when the group list was replaced with null")
    void addGroupFailsOnNullList() {
        TimetableItem item = new TimetableItem();
        item.setGroups(null);

        assertThrows(NullPointerException.class, () -> item.addGroup(group("KB-21")));
    }

    private TimetableItem lesson(LocalDate date) {
        Subject math = new Subject();
        math.setTitle("Math");
        TimetableItem item = new TimetableItem();
        item.setSubject(math);
        item.setDate(date);
        item.addGroup(group("KB-21"));
        return item;
    }

    private Group group(String title) {
        Group group = new Group();
        group.setTitle(title);
        group.setYearEntry(2021);
        return group;
    }
}
