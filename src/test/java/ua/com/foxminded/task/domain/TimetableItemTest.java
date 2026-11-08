package ua.com.foxminded.task.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ua.com.foxminded.task.domain.repository.GroupModelRepository;
import ua.com.foxminded.task.domain.repository.TimetableItemModelRepository;

public class TimetableItemTest {

    @Test
    public void whenAddGroupToTimetableItem_thenTimetableItemContainsGroup() {
        TimetableItem timetableItem = TimetableItemModelRepository.getEmptyModel();
        Group group = GroupModelRepository.getModel3();
        timetableItem.addGroup(group);
        assertTrue(timetableItem.getGroups().contains(group));
    }

    @Test
    public void whenRemoveGroupFromTimetableItem_thenTimetableItemNonContainsGroup() {
        TimetableItem timetableItem = TimetableItemModelRepository.getModel1();
        Group group = GroupModelRepository.getModel1();
        timetableItem.removeGroup(group);
        assertFalse(timetableItem.getGroups().contains(group));
    }
}
