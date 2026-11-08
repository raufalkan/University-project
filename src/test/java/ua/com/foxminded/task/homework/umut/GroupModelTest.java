package ua.com.foxminded.task.homework.umut;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import ua.com.foxminded.task.domain.Department;
import ua.com.foxminded.task.domain.Group;
import ua.com.foxminded.task.service.converter.ConverterToDtoService;

class GroupModelTest {

    // Parameterized test: one rule (equality = same title AND same year) checked with 4 different situations.
    @ParameterizedTest(name = "[{index}] {0}/{1} vs {2}/{3} -> equal = {4}")
    @CsvSource({
        "KB-21, 2021, KB-21, 2021, true",
        "KB-21, 2021, KB-22, 2021, false",
        "KB-21, 2021, KB-21, 2022, false",
        "kb-21, 2021, KB-21, 2021, false"
    })
    @DisplayName("two groups are equal only when title and year of entry both match")
    void equalityDependsOnTitleAndYear(String title1, int year1, String title2, int year2, boolean expected) {
        Group first = group(title1, year1);
        Group second = group(title2, year2);

        assertEquals(expected, first.equals(second));
    }

    @Test
    @DisplayName("equal groups have the same hash code")
    void equalGroupsHaveSameHashCode() {
        Group first = group("KB-21", 2021);
        Group second = group("KB-21", 2021);

        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    @DisplayName("id and department are not part of group equality")
    void idAndDepartmentAreIgnored() {
        Group first = group("KB-21", 2021);
        first.setId(1);
        first.setDepartment(department("Software Engineering"));
        Group second = group("KB-21", 2021);
        second.setId(99);
        second.setDepartment(department("Physics"));

        assertEquals(first, second);
    }

    @Test
    @DisplayName("a group is never equal to null")
    void groupIsNotEqualToNull() {
        Group group = group("KB-21", 2021);

        assertFalse(group.equals(null));
    }

    @Test
    @DisplayName("a group entity is not equal to its own dto")
    void groupIsNotEqualToItsDto() {
        Group group = group("KB-21", 2021);

        assertFalse(group.equals(ConverterToDtoService.convert(group)));
    }

    @Test
    @DisplayName("groups without a title are compared safely")
    void groupsWithoutTitleAreComparedSafely() {
        assertEquals(group(null, 2020), group(null, 2020));
    }

    @Test
    @DisplayName("toString shows the group title and year of entry")
    void toStringShowsTitleAndYear() {
        String text = group("KB-21", 2021).toString();

        assertTrue(text.contains("title=KB-21") && text.contains("yearEntry=2021"));
    }

    @Test
    @DisplayName("a HashSet keeps only one copy of equal groups")
    void hashSetKeepsOneCopyOfEqualGroups() {
        Set<Group> groups = new HashSet<>();
        groups.add(group("KB-21", 2021));
        groups.add(group("KB-21", 2021));

        assertEquals(1, groups.size());
    }

    private Group group(String title, int yearEntry) {
        Group group = new Group();
        group.setTitle(title);
        group.setYearEntry(yearEntry);
        return group;
    }

    private Department department(String title) {
        Department department = new Department();
        department.setTitle(title);
        return department;
    }
}
