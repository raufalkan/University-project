package ua.com.foxminded.task.service.converter;

import static java.util.Objects.nonNull;

import java.util.List;
import java.util.stream.Collectors;

import ua.com.foxminded.task.domain.Auditory;
import ua.com.foxminded.task.domain.AuditoryType;
import ua.com.foxminded.task.domain.Department;
import ua.com.foxminded.task.domain.Faculty;
import ua.com.foxminded.task.domain.Group;
import ua.com.foxminded.task.domain.Lecture;
import ua.com.foxminded.task.domain.Student;
import ua.com.foxminded.task.domain.Subject;
import ua.com.foxminded.task.domain.Teacher;
import ua.com.foxminded.task.domain.TimetableItem;
import ua.com.foxminded.task.domain.dto.AuditoryDto;
import ua.com.foxminded.task.domain.dto.AuditoryTypeDto;
import ua.com.foxminded.task.domain.dto.DepartmentDto;
import ua.com.foxminded.task.domain.dto.FacultyDto;
import ua.com.foxminded.task.domain.dto.GroupDto;
import ua.com.foxminded.task.domain.dto.LectureDto;
import ua.com.foxminded.task.domain.dto.StudentDto;
import ua.com.foxminded.task.domain.dto.SubjectDto;
import ua.com.foxminded.task.domain.dto.TeacherDto;
import ua.com.foxminded.task.domain.dto.TimetableItemDto;

public final class ConverterToDtoService {

    private ConverterToDtoService() {
    }

    public static AuditoryDto convert(Auditory auditory) {
        AuditoryDto auditoryDto = new AuditoryDto();
        auditoryDto.setId(auditory.getId());
        auditoryDto.setAuditoryNumber(auditory.getAuditoryNumber());
        if (nonNull(auditory.getAuditoryType())) {
            auditoryDto.setAuditoryTypeTitle(auditory.getAuditoryType().getType());
            auditoryDto.setAuditoryTypeId(auditory.getAuditoryType().getId());
        }
        auditoryDto.setMaxCapacity(auditory.getMaxCapacity());
        auditoryDto.setDescription(auditory.getDescription());
        return auditoryDto;
    }
    
    public static AuditoryTypeDto convert(AuditoryType auditoryType) {
        AuditoryTypeDto auditoryTypeDto = new AuditoryTypeDto();
        auditoryTypeDto.setId(auditoryType.getId());
        auditoryTypeDto.setType(auditoryType.getType());
        return auditoryTypeDto;
    }
    
    public static DepartmentDto convert(Department department) {
        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setId(department.getId());
        departmentDto.setTitle(department.getTitle());
        departmentDto.setDescription(department.getDescription());
        if (nonNull(department.getFaculty())) {
            departmentDto.setFacultyTitle(department.getFaculty().getTitle());
            departmentDto.setFacultyId(department.getFaculty().getId());
        }
        return departmentDto;
    }
    
    public static FacultyDto convert(Faculty faculty) {
        FacultyDto facultyDto = new FacultyDto();
        facultyDto.setId(faculty.getId());
        facultyDto.setTitle(faculty.getTitle());
        return facultyDto;
    }

    public static GroupDto convert(Group group) {
        GroupDto groupDto = new GroupDto();
        groupDto.setId(group.getId());
        groupDto.setTitle(group.getTitle());
        groupDto.setYearEntry(group.getYearEntry());
        return groupDto;
    }
    
    public static LectureDto convert(Lecture lecture) {
        LectureDto lectureDto = new LectureDto();
        lectureDto.setId(lecture.getId());
        lectureDto.setNumber(lecture.getNumber());
        lectureDto.setStartTime(lecture.getStartTime());
        lectureDto.setEndTime(lecture.getEndTime());
        return lectureDto;
    }
    
    public static StudentDto convert(Student student) {
        StudentDto studentDto = new StudentDto();
        if (nonNull(student.getId())) {
            studentDto.setId(student.getId());
        }
        studentDto.setFirstName(student.getFirstName());
        studentDto.setMiddleName(student.getMiddleName());
        studentDto.setLastName(student.getLastName());
        studentDto.setBirthday(student.getBirthday());
        studentDto.setIdFees(student.getIdFees());
        if (nonNull(student.getGroup())) {
            studentDto.setGroupId(student.getGroup().getId());
            studentDto.setGroupTitle(student.getGroup().getTitle());
        }
        return studentDto;
    }
    
    public static SubjectDto convert(Subject subject) {
        SubjectDto subjectDto = new SubjectDto();
        subjectDto.setId(subject.getId());
        subjectDto.setTitle(subject.getTitle());
        return subjectDto;
    }
    
    public static TeacherDto convert(Teacher teacher) {
        TeacherDto teacherDto = new TeacherDto();
        List<SubjectDto> subjectDtos = teacher.getSubjects().stream()
                                                            .map(ConverterToDtoService::convert)
                                                            .collect(Collectors.toList());
        teacherDto.setId(teacher.getId());
        teacherDto.setFirstName(teacher.getFirstName());
        teacherDto.setLastName(teacher.getLastName());
        teacherDto.setMiddleName(teacher.getMiddleName());
        teacherDto.setBirthday(teacher.getBirthday());
        teacherDto.setIdFees(teacher.getIdFees());
        if (nonNull(teacher.getDepartment())) {
            teacherDto.setDepartmentTitle(teacher.getDepartment().getTitle());
            teacherDto.setDepartmentId(teacher.getDepartment().getId());
        }
        teacherDto.setSubjects(subjectDtos);
        return teacherDto;
    }

    public static TimetableItemDto convert(TimetableItem timetableItem) {
        TimetableItemDto timetableItemDto = new TimetableItemDto();
        timetableItemDto.setId(timetableItem.getId());
        if (nonNull(timetableItem.getSubject())) {
            timetableItemDto.setSubjectTitle(timetableItem.getSubject().getTitle());
            timetableItemDto.setSubjectId(timetableItem.getSubject().getId());
        }
        if (nonNull(timetableItem.getAuditory())) {
            timetableItemDto.setAuditoryTitle(timetableItem.getAuditory().getAuditoryNumber());
            timetableItemDto.setAuditoryId(timetableItem.getAuditory().getId());
        }
        List<GroupDto> groupDtos = timetableItem.getGroups().stream()
                                                            .map(ConverterToDtoService::convert)
                                                            .collect(Collectors.toList());
        timetableItemDto.setGroups(groupDtos);
        if (nonNull(timetableItem.getLecture())) {
            timetableItemDto.setLectureTitle(timetableItem.getLecture().getNumber());
            timetableItemDto.setLectureId(timetableItem.getLecture().getId());
        }
        timetableItemDto.setDate(timetableItem.getDate());
        if (nonNull(timetableItem.getTeacher())) {
            timetableItemDto.setTeacherTitle(timetableItem.getTeacher().getFirstName());
            timetableItemDto.setTeacherId(timetableItem.getTeacher().getId());
        }
        return timetableItemDto;
    }

}
