package ua.com.foxminded.task.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import ua.com.foxminded.task.dao.DepartmentRepository;
import ua.com.foxminded.task.dao.FacultyRepository;
import ua.com.foxminded.task.dao.exception.EntityAlreadyExistsException;
import ua.com.foxminded.task.dao.exception.EntityNotValidException;
import ua.com.foxminded.task.domain.Department;
import ua.com.foxminded.task.domain.Faculty;
import ua.com.foxminded.task.domain.dto.DepartmentDto;
import ua.com.foxminded.task.service.DepartmentService;
import ua.com.foxminded.task.service.converter.ConverterToDtoService;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private DepartmentRepository departmentRepository;
    private FacultyRepository facultyRepository;
    private Logger logger;

    @Autowired
    public DepartmentServiceImpl(Logger logger, 
                                 DepartmentRepository departmentRepository, 
                                 FacultyRepository facultyRepository) {
        this.logger = logger;
        this.departmentRepository = departmentRepository;
        this.facultyRepository = facultyRepository;
    }

    public Department findById(int id) {
        logger.debug("findById() [id:{}]", id);
        return departmentRepository.getOne(id);
    }

    @Override
    public DepartmentDto findByIdDto(int id) {
        logger.debug("findById() [id:{}]", id);
        Department department = findById(id);
        return ConverterToDtoService.convert(department);
    }

    @Override
    public List<DepartmentDto> findAllDto() {
        logger.debug("findAllDto()");
        return departmentRepository.findAll().stream().map(ConverterToDtoService::convert).collect(Collectors.toList());
    }

    @Override
    public DepartmentDto create(DepartmentDto departmentDto) {
        logger.debug("create() [departmentDto:{}]", departmentDto);
        if (departmentDto.getId() != 0) {
            logger.warn("create() [departmentDto:{}]", departmentDto);
            throw new EntityAlreadyExistsException("create() departmentDto: " + departmentDto);
        }
        Department department = retriveEntityFromDto(departmentDto);
        try {
            department = departmentRepository.saveAndFlush(department);
        } catch (DataIntegrityViolationException e) {
            logger.warn("create() [department:{}], exception:{}", department, e);
            throw new EntityNotValidException("create() department: " + department, e);
        }
        return ConverterToDtoService.convert(department);
    }

    @Override
    public DepartmentDto update(DepartmentDto departmentDto) {
        logger.debug("update() [departmentDto:{}]", departmentDto);
        int departmentId = departmentDto.getId();
        if (!departmentRepository.existsById(departmentId)) {
            throw new EntityNotFoundException("Department not exist!");
        }
        Department department = retriveEntityFromDto(departmentDto);
        try {
            department = departmentRepository.saveAndFlush(department);
        } catch (DataIntegrityViolationException e) {
            logger.warn("update() [department:{}], exception:{}", department, e);
            throw new EntityNotValidException("update() department: " + department, e);
        }
        return ConverterToDtoService.convert(department);
    }

    @Override
    public Department findByTitle(String title) {
        logger.debug("findByTitle() [title:{}]", title);
        return departmentRepository.findByTitle(title);
    }

    private Department retriveEntityFromDto(DepartmentDto departmentDto) {
        Department department = (departmentDto.getId() != 0) ? departmentRepository.getOne(departmentDto.getId()) : new Department();
        Faculty faculty = (departmentDto.getFacultyId() != 0) ? facultyRepository.getOne(departmentDto.getFacultyId()) : null;
        department.setTitle(departmentDto.getTitle());
        department.setDescription(departmentDto.getDescription());
        department.setFaculty(faculty);
        return department;
    }
}
