package ua.com.foxminded.task.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import ua.com.foxminded.task.dao.DepartmentRepository;
import ua.com.foxminded.task.dao.SubjectRepository;
import ua.com.foxminded.task.dao.TeacherRepository;
import ua.com.foxminded.task.dao.exception.EntityAlreadyExistsException;
import ua.com.foxminded.task.dao.exception.EntityNotValidException;
import ua.com.foxminded.task.domain.Department;
import ua.com.foxminded.task.domain.Subject;
import ua.com.foxminded.task.domain.Teacher;
import ua.com.foxminded.task.domain.dto.SubjectDto;
import ua.com.foxminded.task.domain.dto.TeacherDto;
import ua.com.foxminded.task.service.TeacherService;
import ua.com.foxminded.task.service.converter.ConverterToDtoService;

@Service
public class TeacherServiceImpl implements TeacherService {

    private TeacherRepository teacherRepository;
    private DepartmentRepository departmentRepository;
    private SubjectRepository subjectRepository;
    private Logger logger;

    @Autowired
    public TeacherServiceImpl(Logger logger, 
                              TeacherRepository teacherRepository, 
                              DepartmentRepository departmentRepository, 
                              SubjectRepository subjectRepository) {
        this.logger = logger;
        this.teacherRepository = teacherRepository;
        this.departmentRepository = departmentRepository;
        this.subjectRepository = subjectRepository;
    }

    public Teacher findById(int id) {
        logger.debug("findById() [id:{}]", id);
        return teacherRepository.getOne(id);
    }

    @Override
    public TeacherDto findByIdDto(int id) {
        logger.debug("findById() [id:{}]", id);
        Teacher teacher = findById(id);
        return ConverterToDtoService.convert(teacher);
    }

    @Override
    public List<TeacherDto> findAllDto() {
        logger.debug("findAllDto()");
        return teacherRepository.findAll().stream().map(ConverterToDtoService::convert).collect(Collectors.toList());
    }

    @Override
    public TeacherDto create(TeacherDto teacherDto) {
        logger.debug("create() [teacherDto:{}]", teacherDto);
        if (teacherDto.getId() != 0) {
            logger.warn("create() [teacherDto:{}]", teacherDto);
            throw new EntityAlreadyExistsException("create() teacherDto: " + teacherDto);
        }
        Teacher teacher = retriveEntityFromDto(teacherDto);
        try {
            teacher = teacherRepository.saveAndFlush(teacher);
        } catch (DataIntegrityViolationException e) {
            logger.warn("create() [teacher:{}], exception:{}", teacher, e);
            throw new EntityNotValidException("create() teacher: " + teacher, e);
        }
        return ConverterToDtoService.convert(teacher);
    }

    @Override
    public TeacherDto update(TeacherDto teacherDto) {
        logger.debug("update() [teacherDto:{}]", teacherDto);
        int teacherId = teacherDto.getId();
        if (!teacherRepository.existsById(teacherId)) {
            throw new EntityNotFoundException("Teacher not exist!");
        }
        Teacher teacher = retriveEntityFromDto(teacherDto);
        Teacher teacherUpdated = null;
        try {
            teacherUpdated = teacherRepository.saveAndFlush(teacher);
        } catch (DataIntegrityViolationException e) {
            logger.warn("update() [teacher:{}], exception:{}", teacher, e);
            throw new EntityNotValidException("update() teacher: " + teacher, e);
        }
        return ConverterToDtoService.convert(teacherUpdated);
    }

    @Override
    public Teacher findByIdFees(Integer idFees) {
        logger.debug("findByIdFees() [idFees:{}]", idFees);
        return teacherRepository.findByIdFees(idFees);
    }

    private Teacher retriveEntityFromDto(TeacherDto teacherDto) {
        Teacher teacher = (teacherDto.getId() != 0) ? teacherRepository.getOne(teacherDto.getId()) : new Teacher();
        Department department = (teacherDto.getDepartmentId() != 0) ? departmentRepository.getOne(teacherDto.getDepartmentId()) : null;
        List<Subject> subjects = retriveSubjectsFromDtos(teacherDto.getSubjects());
        teacher.setFirstName(teacherDto.getFirstName());
        teacher.setMiddleName(teacherDto.getMiddleName());
        teacher.setLastName(teacherDto.getLastName());
        teacher.setBirthday(teacherDto.getBirthday());
        teacher.setIdFees(teacherDto.getIdFees());
        teacher.setDepartment(department);
        teacher.setSubjects(subjects);
        return teacher;
    }

    private List<Subject> retriveSubjectsFromDtos(List<SubjectDto> subjectDtos) {
        return subjectDtos.stream()
                          .map(s -> subjectRepository.getOne(s.getId()))
                          .collect(Collectors.toList());
    }
}
