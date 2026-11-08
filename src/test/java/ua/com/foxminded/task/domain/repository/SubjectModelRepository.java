package ua.com.foxminded.task.domain.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ua.com.foxminded.task.domain.Subject;

public class SubjectModelRepository {

    private SubjectModelRepository() {
    }

    public static List<Subject> getModels1() {
        List<Subject> subjects = Arrays.asList(getModel2(), getModel3(), getModel4());
        return new ArrayList<>(subjects);
    }
    
    public static List<Subject> getModels2() {
        List<Subject> subjects = Arrays.asList(getModel1(), getModel2());
        return new ArrayList<>(subjects);
    }

    public static Subject getModel1() {
        Subject subject = new Subject();
        subject.setTitle("Programming");
        return subject;
    }

    public static Subject getModel2() {
        Subject subject = new Subject();
        subject.setTitle("Phisics");
        return subject;
    }

    public static Subject getModel3() {
        Subject subject = new Subject();
        subject.setTitle("Mathmatics");
        return subject;
    }

    public static Subject getModel4() {
        Subject subject = new Subject();
        subject.setTitle("Biologic");
        return subject;
    }

}
