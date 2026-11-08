package ua.com.foxminded.task.homework.rauf;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.task.dao.GroupRepository;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import ua.com.foxminded.task.dao.GroupRepository;
import ua.com.foxminded.task.domain.Group;
import ua.com.foxminded.task.domain.dto.GroupDto;
import ua.com.foxminded.task.service.impl.GroupServiceImpl;

@ExtendWith(MockitoExtension.class)
public class GroupServiceTest {

    @Mock
    private Logger logger;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private GroupServiceImpl  service;

    @BeforeEach
    void setUp(){
        service= new GroupServiceImpl(logger, groupRepository);
    }

    @Test
    @DisplayName("create returns dto with generated id, title and year")
    void createReturnsDtoWithAllFields() {
        GroupDto input = new GroupDto();
        input.setTitle("KB-21");
        input.setYearEntry(2021);
        when(groupRepository.saveAndFlush(any(Group.class)))
                .thenReturn(group(7, "KB-21", 2021));

        GroupDto result = service.create(input);

        assertAll(
                () -> assertEquals(7, result.getId()),
                () -> assertEquals("KB-21", result.getTitle()),
                () -> assertEquals(Integer.valueOf(2021), result.getYearEntry()));
    }

    @Test
    @DisplayName("findByIdDto returns the year of entry")
    void findByIdDtoReturnsYearEntry() {
        when(groupRepository.getOne(7)).thenReturn(group(7, "KB-21", 2021));

        GroupDto result = service.findByIdDto(7);

        assertEquals(Integer.valueOf(2021), result.getYearEntry());
    }

    @Test
    @DisplayName("findAllDto returns one dto per group")
    void findAllDtoReturnsOneDtoPerGroup() {
        when(groupRepository.findAll()).thenReturn(Arrays.asList(
                group(1, "A-1", 2020), group(2, "A-2", 2021), group(3, "A-3", 2022)));

        List<GroupDto> result = service.findAllDto();

        assertEquals(3, result.size());
        assertEquals("A-1", result.get(0).getTitle());
    }

    @Test
    @DisplayName("update changes the year of entry")
    void updateChangesYearEntry() {
        GroupDto input = new GroupDto();
        input.setId(3);
        input.setTitle("KB-21");
        input.setYearEntry(2022);
        Group existing = group(3, "KB-21", 2021);
        when(groupRepository.existsById(3)).thenReturn(true);
        when(groupRepository.getOne(3)).thenReturn(existing);
        when(groupRepository.saveAndFlush(existing)).thenReturn(existing);

        GroupDto result = service.update(input);

        assertEquals(Integer.valueOf(2022), result.getYearEntry());
        assertNotEquals(Integer.valueOf(2021), result.getYearEntry());
    }

    @Test
    @DisplayName("findByTitle returns the group with its year")
    void findByTitleReturnsGroup() {
        when(groupRepository.findByTitle("KB-21")).thenReturn(group(7, "KB-21", 2021));

        Group result = service.findByTitle("KB-21");

        assertNotNull(result);
        assertEquals(2021, result.getYearEntry());
    }


    private Group group(int id, String title, int yearEntry) {
        Group group = new Group();
        group.setId(id);
        group.setTitle(title);
        group.setYearEntry(yearEntry);
        return group;
    }






















}
