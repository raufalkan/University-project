package ua.com.foxminded.task.domain.repository.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ua.com.foxminded.task.domain.Faculty;
import ua.com.foxminded.task.domain.dto.DepartmentDto;
import ua.com.foxminded.task.domain.repository.FacultyModelRepository;

public class DepartmentDtoModelRepository {

    private DepartmentDtoModelRepository() {
    }

    public static DepartmentDto getEmptyModel() {
        DepartmentDto department = new DepartmentDto();
        department.setTitle("test");
        return department;
    }

    public static List<DepartmentDto> getModels() {
        List<DepartmentDto> departments = Arrays.asList(getModel1(), getModel2(), getModel3());
        return new ArrayList<>(departments);
    }

    public static List<DepartmentDto> getModels1() {
        List<DepartmentDto> departments = Arrays.asList(getModel1(), getModel2());
        return new ArrayList<>(departments);
    }

    public static List<DepartmentDto> getModels2() {
        List<DepartmentDto> departments = Arrays.asList(getModel3(), getModel4());
        return new ArrayList<>(departments);
    }

    public static List<DepartmentDto> getModels3() {
        List<DepartmentDto> departments = Arrays.asList(getModel5());
        return new ArrayList<>(departments);
    }

    public static DepartmentDto getModel1() {
        DepartmentDto department = new DepartmentDto();
        Faculty faculty = FacultyModelRepository.getModel1();
        faculty.setId(1);
        department.setFacultyTitle(faculty.getTitle());
        department.setFacultyId(faculty.getId());
        department.setTitle("department1");
        department.setDescription("bla bla bla 1");
        return department;
    }

    public static DepartmentDto getModel2() {
        DepartmentDto department = new DepartmentDto();
        Faculty faculty = FacultyModelRepository.getModel1();
        faculty.setId(2);
        department.setFacultyTitle(faculty.getTitle());
        department.setFacultyId(faculty.getId());
        department.setTitle("department2");
        department.setDescription("bla bla bla 2");
        return department;
    }

    public static DepartmentDto getModel3() {
        DepartmentDto department = new DepartmentDto();
        department.setTitle("department3");
        department.setDescription("bla bla bla 3");
        department.setFacultyTitle(FacultyModelRepository.getModel1().getTitle());
        return department;
    }

    public static DepartmentDto getModel4() {
        DepartmentDto department = new DepartmentDto();
        department.setTitle("department4");
        department.setDescription("bla bla bla 4");
        department.setFacultyTitle(FacultyModelRepository.getModel2().getTitle());
        return department;
    }

    public static DepartmentDto getModel5() {
        DepartmentDto department = new DepartmentDto();
        department.setTitle("department5");
        department.setDescription("bla bla bla 5");
        department.setFacultyTitle(FacultyModelRepository.getModel2().getTitle());
        return department;
    }
}
