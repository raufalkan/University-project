package ua.com.foxminded.task.domain.repository;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import ua.com.foxminded.task.domain.Lecture;

public class LectureModelRepository {

    private LectureModelRepository() {
    }

    public static Lecture getModel1() {
        Lecture lecture = new Lecture();
        lecture.setNumber("1");
        lecture.setStartTime(LocalTime.of(7,45));
        lecture.setEndTime(LocalTime.of(9,20));
        return lecture;
    }

    public static Lecture getModel2() {
        Lecture lecture = new Lecture();
        lecture.setNumber("2");
        lecture.setStartTime(LocalTime.of(9,30));
        lecture.setEndTime(LocalTime.of(11,05));
        return lecture;
    }

    public static Lecture getModel3() {
        Lecture lecture = new Lecture();
        lecture.setNumber("3");
        lecture.setStartTime(LocalTime.of(11,15));
        lecture.setEndTime(LocalTime.of(12,50));
        return lecture;
    }

    public static Lecture getModel4() {
        Lecture lecture = new Lecture();
        lecture.setNumber("4");
        lecture.setStartTime(LocalTime.of(13,10));
        lecture.setEndTime(LocalTime.of(14,45));
        return lecture;
    }

    public static Lecture getModel5() {
        Lecture lecture = new Lecture();
        lecture.setNumber("5");
        lecture.setStartTime(LocalTime.of(14,55));
        lecture.setEndTime(LocalTime.of(16,30));
        return lecture;
    }

    public static Lecture getModel6() {
        Lecture lecture = new Lecture();
        lecture.setNumber("6");
        lecture.setStartTime(LocalTime.of(16,40));
        lecture.setEndTime(LocalTime.of(18,15));
        return lecture;
    }

    public static List<Lecture> getModels() {
        return Arrays.asList(getModel1(), getModel2(), getModel3(), getModel4());
    }

}
