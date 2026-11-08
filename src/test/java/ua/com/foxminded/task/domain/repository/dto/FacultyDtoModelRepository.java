package ua.com.foxminded.task.domain.repository.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ua.com.foxminded.task.domain.dto.FacultyDto;

public class FacultyDtoModelRepository {

    private FacultyDtoModelRepository() {
    }

    public static List<FacultyDto> getModels() {
        List<FacultyDto> faculties = Arrays.asList(getModel1(), getModel2(), getModel3(), getModel4(), getModel5(), getModel6());
        return new ArrayList<>(faculties);
    }

    public static FacultyDto getEmptyModel1() {
        FacultyDto faculty = new FacultyDto();
        faculty.setTitle("faculty1");
        return faculty;
    }

    public static FacultyDto getModel1() {
        FacultyDto faculty = new FacultyDto();
        faculty.setTitle("faculty1");
        return faculty;
    }

    public static FacultyDto getModel2() {
        FacultyDto faculty = new FacultyDto();
        faculty.setTitle("faculty2");
        return faculty;
    }

    public static FacultyDto getModel3() {
        FacultyDto faculty = new FacultyDto();
        faculty.setTitle("faculty3");
        return faculty;
    }

    public static FacultyDto getModel4() {
        FacultyDto faculty = new FacultyDto();
        faculty.setTitle("faculty4");
        return faculty;
    }

    public static FacultyDto getModel5() {
        FacultyDto faculty = new FacultyDto();
        faculty.setTitle("faculty5");
        return faculty;
    }

    public static FacultyDto getModel6() {
        FacultyDto faculty = new FacultyDto();
        faculty.setTitle("faculty6");
        return faculty;
    }

}
