package ua.com.foxminded.task.domain.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ua.com.foxminded.task.domain.Faculty;

public class FacultyModelRepository {

    private FacultyModelRepository() {
    }

    public static List<Faculty> getModels() {
        List<Faculty> faculties = Arrays.asList(getModel1(), getModel2(), getModel3(), getModel4(), getModel5(), getModel6());
        return new ArrayList<>(faculties);
    }

    public static Faculty getEmptyModel1() {
        Faculty faculty = new Faculty();
        faculty.setTitle("faculty1");
        return faculty;
    }

    public static Faculty getModel1() {
        Faculty faculty = new Faculty();
        faculty.setTitle("faculty1");
        return faculty;
    }

    public static Faculty getModel2() {
        Faculty faculty = new Faculty();
        faculty.setTitle("faculty2");
        return faculty;
    }

    public static Faculty getModel3() {
        Faculty faculty = new Faculty();
        faculty.setTitle("faculty3");
        return faculty;
    }

    public static Faculty getModel4() {
        Faculty faculty = new Faculty();
        faculty.setTitle("faculty4");
        return faculty;
    }

    public static Faculty getModel5() {
        Faculty faculty = new Faculty();
        faculty.setTitle("faculty5");
        return faculty;
    }

    public static Faculty getModel6() {
        Faculty faculty = new Faculty();
        faculty.setTitle("faculty6");
        return faculty;
    }

}
