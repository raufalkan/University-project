package ua.com.foxminded.task.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import ua.com.foxminded.task.dao.AuditoryRepository;
import ua.com.foxminded.task.dao.GroupRepository;
import ua.com.foxminded.task.dao.LectureRepository;
import ua.com.foxminded.task.dao.SubjectRepository;
import ua.com.foxminded.task.dao.TeacherRepository;
import ua.com.foxminded.task.dao.TimetableItemRepository;
import ua.com.foxminded.task.dao.exception.EntityAlreadyExistsException;
import ua.com.foxminded.task.dao.exception.EntityNotValidException;
import ua.com.foxminded.task.dao.filter.TimetableItemSpecification;
import ua.com.foxminded.task.domain.Auditory;
import ua.com.foxminded.task.domain.Group;
import ua.com.foxminded.task.domain.Lecture;
import ua.com.foxminded.task.domain.Subject;
import ua.com.foxminded.task.domain.Teacher;
import ua.com.foxminded.task.domain.TimetableFilters;
import ua.com.foxminded.task.domain.TimetableItem;
import ua.com.foxminded.task.domain.dto.GroupDto;
import ua.com.foxminded.task.domain.dto.TimetableItemDto;
import ua.com.foxminded.task.service.TimetableItemService;
import ua.com.foxminded.task.service.converter.ConverterToDtoService;

@Service
public class TimetableItemServiceImpl implements TimetableItemService {

    private TeacherRepository teacherRepository;
    private SubjectRepository subjectRepository;
    private AuditoryRepository auditoryRepository;
    private LectureRepository lectureRepository;
    private GroupRepository groupRepository;
    private TimetableItemRepository timetableItemRepository;
    private Logger logger;

    @Autowired
    public TimetableItemServiceImpl(TeacherRepository teacherRepository,
                                    SubjectRepository subjectRepository,
                                    AuditoryRepository auditoryRepository,
                                    LectureRepository lectureRepository,
                                    GroupRepository groupRepository,
                                    TimetableItemRepository timetableItemRepository,
                                    Logger logger) {
        this.teacherRepository = teacherRepository;
        this.subjectRepository = subjectRepository;
        this.auditoryRepository = auditoryRepository;
        this.lectureRepository = lectureRepository;
        this.groupRepository = groupRepository;
        this.timetableItemRepository = timetableItemRepository;
        this.logger = logger;
    }

    public TimetableItem findById(int id) {
        logger.debug("findById() [id:{}]", id);
        return timetableItemRepository.getOne(id);
    }

    @Override
    public TimetableItemDto findByIdDto(int id) {
        logger.debug("findById() [id:{}]", id);
        TimetableItem timetableItem = findById(id);
        return ConverterToDtoService.convert(timetableItem);
    }

    @Override
    public List<TimetableItemDto> findAllDto() {
        logger.debug("findAllDto()");
        return timetableItemRepository.findAll().stream().map(ConverterToDtoService::convert).collect(Collectors.toList());
    }

    @Override
    public TimetableItemDto create(TimetableItemDto timetableItemDto) {
        logger.debug("create() [timetableItemDto:{}]", timetableItemDto);
        if (timetableItemDto.getId() != 0) {
            logger.warn("create() [timetableItemDto:{}]", timetableItemDto);
            throw new EntityAlreadyExistsException("create() timetableItemDto: " + timetableItemDto);
        }
        TimetableItem timetableItem = retriveEntityFromDto(timetableItemDto);
        try {
            timetableItem = timetableItemRepository.saveAndFlush(timetableItem);
        } catch (DataIntegrityViolationException e) {
            logger.warn("create() [timetableItem:{}], exception:{}", timetableItem, e);
            throw new EntityNotValidException("create() timetableItem: " + timetableItem, e);
        }
        return ConverterToDtoService.convert(timetableItem);
    }

    @Override
    public TimetableItemDto update(TimetableItemDto timetableItemDto) {
        logger.debug("update() [timetableItemDto:{}]", timetableItemDto);
        int timetableItemId = timetableItemDto.getId();
        if (!timetableItemRepository.existsById(timetableItemId)) {
            throw new EntityNotFoundException("Timetable Item not exist!");
        }
        TimetableItem timetableItem = retriveEntityFromDto(timetableItemDto);
        try {
            timetableItem = timetableItemRepository.saveAndFlush(timetableItem);
        } catch (DataIntegrityViolationException e) {
            logger.warn("update() [timetableItem:{}], exception:{}", timetableItem, e);
            throw new EntityNotValidException("update() timetableItem: " + timetableItem, e);
        }
        return ConverterToDtoService.convert(timetableItem);
    }

    @Override
    public TimetableItem findByAuditoryIdAndLectureIdAndDate(Integer auditoryId, Integer lectureId, LocalDate date) {
        logger.debug("existsByAuditoryIdAndLectureIdAndDate() [auditoryId:{}, lectureId:{}, date:{}]", auditoryId, lectureId, date);
        return timetableItemRepository.findByAuditoryIdAndLectureIdAndDate(auditoryId, lectureId, date);
    }

    @Override
    public TimetableItem findByTeacherIdAndLectureIdAndDate(Integer teacherId, Integer lectureId, LocalDate date) {
        logger.debug("existsByTeacherIdAndLectureIdAndDate() [teacherId:{}, lectureId:{}, date:{}]", teacherId, lectureId, date);
        return timetableItemRepository.findByTeacherIdAndLectureIdAndDate(teacherId, lectureId, date);
    }

    @Override
    public List<TimetableItemDto> findAllByFilters(TimetableFilters filters) {
        logger.debug("findByTimetableItemSpecification() [filters:{}]", filters);
        TimetableItemSpecification itemSpecification = new TimetableItemSpecification(filters);
        List<TimetableItem> timetableItems =timetableItemRepository.findAll(itemSpecification);
        return timetableItems.stream().map(ConverterToDtoService::convert).collect(Collectors.toList());
    }

    private TimetableItem retriveEntityFromDto(TimetableItemDto timetableItemDto) {
        TimetableItem timetableItem = (timetableItemDto.getId() != 0) ? timetableItemRepository.getOne(timetableItemDto.getId()) : new TimetableItem();
        Subject subject = (timetableItemDto.getSubjectId() != 0) ? subjectRepository.getOne(timetableItemDto.getSubjectId()) : null;
        Auditory auditory = (timetableItemDto.getAuditoryId() != 0) ? auditoryRepository.getOne(timetableItemDto.getAuditoryId()) : null;
        Lecture lecture = (timetableItemDto.getLectureId() != 0) ? lectureRepository.getOne(timetableItemDto.getLectureId()) : null;
        Teacher teacher = (timetableItemDto.getLectureId() != 0) ? teacherRepository.getOne(timetableItemDto.getTeacherId()) : null;
        List<Group> groups = retriveGroupsFromDtos(timetableItemDto.getGroups());
        timetableItem.setSubject(subject);
        timetableItem.setAuditory(auditory);
        timetableItem.setGroups(groups);
        timetableItem.setLecture(lecture);
        timetableItem.setDate(timetableItemDto.getDate());
        timetableItem.setTeacher(teacher);
        return timetableItem;
    }

    private List<Group> retriveGroupsFromDtos(List<GroupDto> groupDtos) {
        return groupDtos.stream()
                        .map(s -> groupRepository.getOne(s.getId()))
                        .collect(Collectors.toList());
    }
}
