package ua.com.foxminded.task.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import ua.com.foxminded.task.dao.GroupRepository;
import ua.com.foxminded.task.dao.StudentRepository;
import ua.com.foxminded.task.dao.exception.EntityAlreadyExistsException;
import ua.com.foxminded.task.dao.exception.EntityNotValidException;
import ua.com.foxminded.task.domain.Group;
import ua.com.foxminded.task.domain.Student;
import ua.com.foxminded.task.domain.dto.StudentDto;
import ua.com.foxminded.task.service.StudentService;
import ua.com.foxminded.task.service.converter.ConverterToDtoService;

@Service
public class StudentServiceImpl implements StudentService {

    private StudentRepository studentRepository;
    private GroupRepository groupRepository;
    private Logger logger;

    @Autowired
    public StudentServiceImpl(Logger logger, StudentRepository studentRepository, GroupRepository groupRepository) {
        this.logger = logger;
        this.studentRepository = studentRepository;
        this.groupRepository = groupRepository;
    }

    @Override
    public StudentDto findByIdDto(int id) {
        logger.debug("findByIdDto() [id:{}]", id);
        StudentDto studentDto = null;
            Student student = studentRepository.getOne(id);
            studentDto = ConverterToDtoService.convert(student);
        return studentDto;
    }

    @Override
    public List<StudentDto> findAllDto() {
        logger.debug("findAllDto()");
        return studentRepository.findAll().stream().map(ConverterToDtoService::convert).collect(Collectors.toList());
    }

    @Override
    public StudentDto create(StudentDto studentDto) {
        logger.debug("create() [studentDto:{}]", studentDto);
        if (studentDto.getId() != 0) {
            logger.warn("create() [studentDto:{}]", studentDto);
            throw new EntityAlreadyExistsException("create() studentDto: " + studentDto);
        }
        Student student = retriveEntityFromDto(studentDto);
        try {
            student = studentRepository.saveAndFlush(student);
        } catch (DataIntegrityViolationException e) {
            logger.warn("create() [student:{}], exception:{}", student, e);
            throw new EntityNotValidException("create() student: " + student, e);
        }
        return ConverterToDtoService.convert(student);
    }

    @Override
    public StudentDto update(StudentDto studentDto) {
        logger.debug("update() [studentDto:{}]", studentDto);
        int studentId = studentDto.getId();
        if (!studentRepository.existsById(studentId)) {
            throw new EntityNotFoundException("Student not exist!");
        }
        Student student = retriveEntityFromDto(studentDto);
        try {
            student = studentRepository.saveAndFlush(student);
        } catch (DataIntegrityViolationException e) {
            logger.warn("update() [student:{}], exception:{}", student, e);
            throw new EntityNotValidException("update() student: " + student, e);
        }
        return ConverterToDtoService.convert(student);
    }

    @Override
    public Student findByIdFees(Integer idFees) {
        logger.debug("findByIdFees() [idFees:{}]", idFees);
        return studentRepository.findByIdFees(idFees);
    }

    @Override
    public long countByGroupId(Integer id) {
        logger.debug("countByGroupId() [id:{}]", id);
        return studentRepository.countByGroupId(id);
    }

    @Override
    public boolean existsStudentByIdAndGroupId(Integer studentId, Integer groupId) {
        logger.debug("existsStudentByIdAndGroupId() [studentId:{}, groupId:{}]", studentId, groupId);
        return studentRepository.existsStudentByIdAndGroupId(studentId, groupId);
    }

    private Student retriveEntityFromDto(StudentDto studentDto) {
        Student student = (studentDto.getId() != 0) ? studentRepository.getOne(studentDto.getId()) : new Student();

        Group group = studentDto.getGroupId() != 0 ? groupRepository.getOne(Integer.valueOf(studentDto.getGroupId())) : null;
        student.setGroup(group);
        student.setFirstName(studentDto.getFirstName());
        student.setMiddleName(studentDto.getMiddleName());
        student.setLastName(studentDto.getLastName());
        student.setBirthday(studentDto.getBirthday());
        student.setIdFees(studentDto.getIdFees());
        return student;
    }

}
