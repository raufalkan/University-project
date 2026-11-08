package ua.com.foxminded.task.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import ua.com.foxminded.task.dao.GroupRepository;
import ua.com.foxminded.task.dao.exception.EntityAlreadyExistsException;
import ua.com.foxminded.task.dao.exception.EntityNotValidException;
import ua.com.foxminded.task.domain.Group;
import ua.com.foxminded.task.domain.dto.GroupDto;
import ua.com.foxminded.task.domain.repository.GroupModelRepository;
import ua.com.foxminded.task.domain.repository.dto.GroupDtoModelRepository;

class GroupServiceImplTest {

    @Mock
    private Logger logger;
    @Mock
    private GroupRepository groupRepository;
    @InjectMocks
    private GroupServiceImpl groupService;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenFindById_thenFindGroup() {
        Group expectedGroup = GroupModelRepository.getModel1();
        doReturn(expectedGroup).when(groupRepository).getOne(1);

        Group actuallyGroup = groupService.findById(1);

        verify(groupRepository, times(1)).getOne(anyInt());
        assertEquals(expectedGroup, actuallyGroup);
    }

    @Test
    void whenFindById_thenFindGroupAndConvertItToDto() {
        Group group = GroupModelRepository.getModel1();
        GroupDto groupDtoExpected = GroupDtoModelRepository.getModel1();
        doReturn(group).when(groupRepository).getOne(1);

        GroupDto groupDtoActually = groupService.findByIdDto(1);

        verify(groupRepository, times(1)).getOne(anyInt());
        assertEquals(groupDtoExpected, groupDtoActually);
    }

    @Test
    void whenFindByAll_thenFindGroupsAndConvertItToDto() {
        List<Group> groups = GroupModelRepository.getModels1();
        List<GroupDto> groupDtosExpected = GroupDtoModelRepository.getModels1();
        doReturn(groups).when(groupRepository).findAll();

        List<GroupDto> groupDtosActually = groupService.findAllDto();

        verify(groupRepository, times(1)).findAll();
        assertEquals(groupDtosExpected, groupDtosActually);
    }

    @Test
    void whenCreate_thenInvocCreateDaoClass() {
        GroupDto groupDto = GroupDtoModelRepository.getModel1();

        Group groupInput = GroupModelRepository.getModel1();
        Group groupExpected = GroupModelRepository.getModelWithId();

        doReturn(groupExpected).when(groupRepository).saveAndFlush(groupInput);

        GroupDto groupDtoActually = groupService.create(groupDto);

        verify(groupRepository, times(1)).saveAndFlush(groupInput);
        groupDto.setId(1);
        assertThat(groupDto).isEqualToComparingFieldByField(groupDtoActually);
    }

    @Test
    void whenUpdate_thenInvocUpdateDaoClass() {
        GroupDto groupDto = GroupDtoModelRepository.getModelWithId();
        Group group = new Group();
        group.setId(groupDto.getId());
        group.setTitle(groupDto.getTitle());
        group.setYearEntry(groupDto.getYearEntry());
        doReturn(group).when(groupRepository).saveAndFlush(group);
        doReturn(true).when(groupRepository).existsById(groupDto.getId());
        doReturn(group).when(groupRepository).getOne(groupDto.getId());

        GroupDto groupDtoActually = groupService.update(groupDto);

        verify(groupRepository, times(1)).saveAndFlush(group);
        verify(groupRepository, times(1)).getOne(groupDto.getId());
        assertEquals(groupDto, groupDtoActually);
    }

    @Test
    void whenFindByTitle_thenInvokeMethod() {
        Group expectedGroup = GroupModelRepository.getModelWithId();
        String expectedTitle = expectedGroup.getTitle();
        doReturn(expectedGroup).when(groupRepository).findByTitle(expectedTitle);

        Group actuallyGroup = groupService.findByTitle(expectedTitle);

        verify(groupRepository, times(1)).findByTitle(expectedTitle);
        assertEquals(expectedTitle, actuallyGroup.getTitle());
    }

    @Test
    void whenCreateRecordEntityWithId_thenThrowException() {
        GroupDto group = new GroupDto();
        group.setId(1);

        assertThatThrownBy(() -> groupService.create(group))
            .isInstanceOf(EntityAlreadyExistsException.class)
            .hasMessage("create() groupDto: %s", group);
    }

    @Test
    void whenCreateRecordWithNotValidEntity_thenThrowException() {
        GroupDto groupDto = GroupDtoModelRepository.getModel1();
        Group group = GroupModelRepository.getModel1();
        doThrow(DataIntegrityViolationException.class).when(groupRepository).saveAndFlush(group);

        assertThatThrownBy(() -> groupService.create(groupDto))
            .isInstanceOf(EntityNotValidException.class)
            .hasMessageContaining("create() group: " + group);
    }

    @Test
    void whenUpdateRecordWithNotValidEntity_thenThrowException() {
        GroupDto groupDto = GroupDtoModelRepository.getModel1();
        Group group = GroupModelRepository.getModel1();
        doReturn(true).when(groupRepository).existsById(group.getId());
        doThrow(DataIntegrityViolationException.class).when(groupRepository).saveAndFlush(group);

        assertThatThrownBy(() -> groupService.update(groupDto))
            .isInstanceOf(EntityNotValidException.class)
            .hasMessageContaining("update() group: " + group);
    }

    @Test
    void whenUpdateRecordNotFound_thenThrowException() {
        GroupDto groupDto = GroupDtoModelRepository.getModel1();
        Group group = GroupModelRepository.getModel1();
        doReturn(false).when(groupRepository).existsById(group.getId());

        assertThatThrownBy(() -> groupService.update(groupDto))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessageContaining("Group not exist!");
    }
}
