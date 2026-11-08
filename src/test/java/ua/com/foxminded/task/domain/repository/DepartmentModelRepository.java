package ua.com.foxminded.task.domain.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ua.com.foxminded.task.domain.Department;
import ua.com.foxminded.task.domain.Faculty;

public class DepartmentModelRepository {

    private DepartmentModelRepository() {
    }

    public static Department getEmptyModel() {
        Department department = new Department();
        department.setTitle("test");
        return department;
    }

    public static List<Department> getModels() {
        List<Department> departments = Arrays.asList(getModel1(), getModel2(), getModel3());
        return new ArrayList<>(departments);
    }

    public static List<Department> getModels1() {
        List<Department> departments = Arrays.asList(getModel1(), getModel2());
        return new ArrayList<>(departments);
    }

    public static List<Department> getModels2() {
        List<Department> departments = Arrays.asList(getModel3(), getModel4());
        return new ArrayList<>(departments);
    }

    public static List<Department> getModels3() {
        List<Department> departments = Arrays.asList(getModel5());
        return new ArrayList<>(departments);
    }

    public static Department getModel1() {
        Department department = new Department();
        Faculty faculty = FacultyModelRepository.getModel1();
        faculty.setId(1);
        department.setFaculty(faculty);
        department.setTitle("department1");
        department.setDescription("bla bla bla 1");
        return department;
    }

    public static Department getModel2() {
        Department department = new Department();
        Faculty faculty = FacultyModelRepository.getModel1();
        faculty.setId(2);
        department.setFaculty(faculty);
        department.setTitle("department2");
        department.setDescription("bla bla bla 2");
        return department;
    }

    public static Department getModel3() {
        Department department = new Department();
        department.setTitle("department3");
        department.setDescription("bla bla bla 3");
        department.setFaculty(FacultyModelRepository.getModel1());
        return department;
    }

    public static Department getModel4() {
        Department department = new Department();
        department.setTitle("department4");
        department.setDescription("bla bla bla 4");
        department.setFaculty(FacultyModelRepository.getModel2());
        return department;
    }

    public static Department getModel5() {
        Department department = new Department();
        department.setTitle("department5");
        department.setDescription("bla bla bla 5");
        department.setFaculty(FacultyModelRepository.getModel2());
        return department;
    }
}
