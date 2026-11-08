package ua.com.foxminded.task.service;

import ua.com.foxminded.task.domain.Student;
import ua.com.foxminded.task.domain.dto.StudentDto;

public interface StudentService extends ModelService<StudentDto> {

    public Student findByIdFees(Integer idFees);

    public long countByGroupId(Integer id);
    
    public boolean existsStudentByIdAndGroupId(Integer studentId, Integer groupId);

}
