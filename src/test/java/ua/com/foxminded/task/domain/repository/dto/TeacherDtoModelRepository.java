package ua.com.foxminded.task.domain.repository.dto;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import ua.com.foxminded.task.domain.Department;
import ua.com.foxminded.task.domain.dto.TeacherDto;
import ua.com.foxminded.task.domain.repository.DepartmentModelRepository;

public class TeacherDtoModelRepository {

    private TeacherDtoModelRepository() {
    }

    public static TeacherDto getEmptyModel() {
        TeacherDto teacher = new TeacherDto();
        teacher.setFirstName("firstName6");
        teacher.setMiddleName("middleName6");
        teacher.setLastName("lastName6");
        teacher.setBirthday(LocalDate.of(1980, 06, 25));
        teacher.setIdFees(111111166);
        return teacher;
    }

    public static List<TeacherDto> getModels1() {
        return Arrays.asList(getModel2(), getModel3(), getModel4());
    }

    public static List<TeacherDto> getModels2() {
        return Arrays.asList(getModel1(), getModel5());
    }

    public static List<TeacherDto> getModels3() {
        return Arrays.asList(getModel6());
    }

    public static List<TeacherDto> getModels4() {
        return Arrays.asList(getModel7());
    }

    public static List<TeacherDto> getModels5() {
        return Arrays.asList(getModel8(), getModel9());
    }

    public static TeacherDto getModel1() {
        TeacherDto teacher = new TeacherDto();
        Department department = DepartmentModelRepository.getModel1();
        department.setId(1);
        teacher.setDepartmentTitle(department.getTitle());
        teacher.setDepartmentId(department.getId());
        teacher.setFirstName("firstNameTe1");
        teacher.setMiddleName("middleNameTe1");
        teacher.setLastName("lastNameTe1");
        teacher.setBirthday(LocalDate.of(1980, 06, 25));
        teacher.setIdFees(111111166);
        teacher.setSubjects(SubjectDtoModelRepository.getModels1());
        return teacher;
    }

    public static TeacherDto getModel2() {
        TeacherDto teacher = new TeacherDto();
        teacher.setFirstName("firstNameTe2");
        teacher.setMiddleName("middleNameTe2");
        teacher.setLastName("lastNameTe2");
        teacher.setBirthday(LocalDate.of(1966, 06, 25));
        teacher.setIdFees(211111111);
        teacher.setSubjects(SubjectDtoModelRepository.getModels1());
        teacher.setDepartmentTitle(DepartmentModelRepository.getModel2().getTitle());
        return teacher;
    }

    public static TeacherDto getModel3() {
        TeacherDto teacher = new TeacherDto();
        teacher.setFirstName("firstNameTe3");
        teacher.setMiddleName("middleNameTe3");
        teacher.setLastName("lastNameTe3");
        teacher.setBirthday(LocalDate.of(1950, 06, 25));
        teacher.setIdFees(333111111);
        teacher.setSubjects(SubjectDtoModelRepository.getModels1());
        teacher.setDepartmentTitle(DepartmentModelRepository.getModel1().getTitle());
        return teacher;
    }

    private static TeacherDto getModel4() {
        TeacherDto teacher = new TeacherDto();
        teacher.setFirstName("firstNameTe4");
        teacher.setMiddleName("middleNameTe4");
        teacher.setLastName("lastNameTe4");
        teacher.setBirthday(LocalDate.of(1971, 06, 25));
        teacher.setIdFees(411111333);
        teacher.setSubjects(SubjectDtoModelRepository.getModels1());
        teacher.setDepartmentTitle(DepartmentModelRepository.getModel2().getTitle());
        return teacher;
    }

    private static TeacherDto getModel5() {
        TeacherDto teacher = new TeacherDto();
        teacher.setFirstName("firstNameTe5");
        teacher.setMiddleName("middleNameTe5");
        teacher.setLastName("lastNameTe5");
        teacher.setBirthday(LocalDate.of(1975, 06, 25));
        teacher.setIdFees(511111333);
        teacher.setSubjects(SubjectDtoModelRepository.getModels1());
        teacher.setDepartmentTitle(DepartmentModelRepository.getModel1().getTitle());
        return teacher;
    }

    private static TeacherDto getModel6() {
        TeacherDto teacher = new TeacherDto();
        teacher.setFirstName("firstNameTe6");
        teacher.setMiddleName("middleNameTe6");
        teacher.setLastName("lastNameTe6");
        teacher.setBirthday(LocalDate.of(1976, 06, 25));
        teacher.setIdFees(611111333);
        teacher.setSubjects(SubjectDtoModelRepository.getModels2());
        teacher.setDepartmentTitle(DepartmentModelRepository.getModel3().getTitle());
        return teacher;
    }

    private static TeacherDto getModel7() {
        TeacherDto teacher = new TeacherDto();
        teacher.setFirstName("firstNameTe7");
        teacher.setMiddleName("middleNameTe7");
        teacher.setLastName("lastNameTe7");
        teacher.setBirthday(LocalDate.of(1977, 06, 25));
        teacher.setIdFees(711111333);
        teacher.setSubjects(SubjectDtoModelRepository.getModels2());
        teacher.setDepartmentTitle(DepartmentModelRepository.getModel3().getTitle());
        return teacher;
    }

    private static TeacherDto getModel8() {
        TeacherDto teacher = new TeacherDto();
        teacher.setFirstName("firstNameTe8");
        teacher.setMiddleName("middleNameTe8");
        teacher.setLastName("lastNameTe8");
        teacher.setBirthday(LocalDate.of(1978, 06, 25));
        teacher.setIdFees(811111333);
        teacher.setSubjects(SubjectDtoModelRepository.getModels2());
        teacher.setDepartmentTitle(DepartmentModelRepository.getModel1().getTitle());
        return teacher;
    }

    private static TeacherDto getModel9() {
        TeacherDto teacher = new TeacherDto();
        teacher.setFirstName("firstNameTe9");
        teacher.setMiddleName("middleNameTe9");
        teacher.setLastName("lastNameTe9");
        teacher.setBirthday(LocalDate.of(1979, 06, 25));
        teacher.setIdFees(911111333);
        teacher.setSubjects(SubjectDtoModelRepository.getModels2());
        teacher.setDepartmentTitle(DepartmentModelRepository.getModel2().getTitle());
        return teacher;
    }
}
