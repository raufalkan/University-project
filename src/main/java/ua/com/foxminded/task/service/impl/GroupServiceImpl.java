package ua.com.foxminded.task.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import ua.com.foxminded.task.dao.GroupRepository;
import ua.com.foxminded.task.dao.exception.EntityAlreadyExistsException;
import ua.com.foxminded.task.dao.exception.EntityNotValidException;
import ua.com.foxminded.task.domain.Group;
import ua.com.foxminded.task.domain.dto.GroupDto;
import ua.com.foxminded.task.service.GroupService;
import ua.com.foxminded.task.service.converter.ConverterToDtoService;

@Service
public class GroupServiceImpl implements GroupService {

    private GroupRepository groupRepository;
    private Logger logger;

    @Autowired
    public GroupServiceImpl(Logger logger, GroupRepository groupRepository) {
        this.logger = logger;
        this.groupRepository = groupRepository;
    }

    @Override
    public Group findById(int id) {
        logger.debug("findById() [id:{}]", id);
        return groupRepository.getOne(id);
    }

    @Override
    public GroupDto findByIdDto(int id) {
        logger.debug("findById() [id:{}]", id);
        GroupDto groupDto = null;
            Group group = findById(id);
            groupDto = ConverterToDtoService.convert(group);
        return groupDto;
    }

    @Override
    public List<GroupDto> findAllDto() {
        logger.debug("findAllDto()");
        return groupRepository.findAll().stream().map(ConverterToDtoService::convert).collect(Collectors.toList());
    }

    @Override
    public GroupDto create(GroupDto groupDto) {
        logger.debug("create() [groupDto:{}]", groupDto);
        if (groupDto.getId()!=0) {
            logger.warn("create() [groupDto:{}]", groupDto);
            throw new EntityAlreadyExistsException("create() groupDto: " + groupDto);
        }
        Group group = retriveEntityFromDto(groupDto);
        try {
            group = groupRepository.saveAndFlush(group);
        } catch (DataIntegrityViolationException e) {
            logger.warn("create() [group:{}], exception:{}", group, e);
            throw new EntityNotValidException("create() group: " + group, e);
        }
        return ConverterToDtoService.convert(group);
    }

    @Override
    public GroupDto update(GroupDto groupDto) {
        logger.debug("update() [groupDto:{}]", groupDto);
        int groupId = groupDto.getId();
        if (!groupRepository.existsById(groupId)) {
            throw new EntityNotFoundException("Group not exist!");
        }
        Group group = retriveEntityFromDto(groupDto);
        try {
            group = groupRepository.saveAndFlush(group);
        } catch (DataIntegrityViolationException e) {
            logger.warn("update() [group:{}], exception:{}", group, e);
            throw new EntityNotValidException("update() group: " + group, e);
        }
        return ConverterToDtoService.convert(group);
    }

    @Override
    public Group findByTitle(String title) {
        logger.debug("findByTitle() [title:{}]", title);
        return groupRepository.findByTitle(title);
    }

    private Group retriveEntityFromDto(GroupDto groupDto) {
        Group group = (groupDto.getId() != 0) ? groupRepository.getOne(groupDto.getId()) : new Group();
        group.setTitle(groupDto.getTitle());
        group.setYearEntry(groupDto.getYearEntry());
        return group;
    }
}
