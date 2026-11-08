package ua.com.foxminded.task.domain.repository.dto;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import ua.com.foxminded.task.domain.dto.LectureDto;

public class LectureDtoModelRepository {

    private LectureDtoModelRepository() {
    }

    public static LectureDto getModel1() {
        LectureDto lecture = new LectureDto();
        lecture.setNumber("1");
        lecture.setStartTime(LocalTime.of(7, 45));
        lecture.setEndTime(LocalTime.of(9, 20));
        return lecture;
    }

    public static LectureDto getModel2() {
        LectureDto lecture = new LectureDto();
        lecture.setNumber("2");
        lecture.setStartTime(LocalTime.of(9, 30));
        lecture.setEndTime(LocalTime.of(11, 05));
        return lecture;
    }

    public static LectureDto getModel3() {
        LectureDto lecture = new LectureDto();
        lecture.setNumber("3");
        lecture.setStartTime(LocalTime.of(11, 15));
        lecture.setEndTime(LocalTime.of(12, 50));
        return lecture;
    }

    public static LectureDto getModel4() {
        LectureDto lecture = new LectureDto();
        lecture.setNumber("4");
        lecture.setStartTime(LocalTime.of(13, 10));
        lecture.setEndTime(LocalTime.of(14, 45));
        return lecture;
    }

    public static LectureDto getModel5() {
        LectureDto lecture = new LectureDto();
        lecture.setNumber("5");
        lecture.setStartTime(LocalTime.of(14, 55));
        lecture.setEndTime(LocalTime.of(16, 30));
        return lecture;
    }

    public static LectureDto getModel6() {
        LectureDto lecture = new LectureDto();
        lecture.setNumber("6");
        lecture.setStartTime(LocalTime.of(16, 40));
        lecture.setEndTime(LocalTime.of(18, 15));
        return lecture;
    }

    public static List<LectureDto> getModels() {
        return Arrays.asList(getModel1(), getModel2(), getModel3(), getModel4());
    }

}
