package ua.com.foxminded.task.domain.repository.dto;

import static java.util.Arrays.asList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ua.com.foxminded.task.domain.dto.GroupDto;
import ua.com.foxminded.task.domain.dto.TimetableItemDto;
import ua.com.foxminded.task.domain.repository.AuditoryModelRepository;
import ua.com.foxminded.task.domain.repository.LectureModelRepository;
import ua.com.foxminded.task.domain.repository.SubjectModelRepository;
import ua.com.foxminded.task.domain.repository.TeacherModelRepository;

public class TimetableItemDtoModelRepository {

    private TimetableItemDtoModelRepository() {
    }

    public static List<TimetableItemDto> getModels() {
        List<TimetableItemDto> timetableItems = asList(getModel1(), 
                                                    getModel2(), 
                                                    getModel3(), 
                                                    getModel4(), 
                                                    getModel5(),
                                                    getModel5(),
                                                    getModel6(),
                                                    getModel7(),
                                                    getModel8(),
                                                    getModel9(),
                                                    getModel10());
        return new ArrayList<>(timetableItems);
    }
    
    public static TimetableItemDto getModel1() {
        TimetableItemDto timetableItemDto = new TimetableItemDto();
        timetableItemDto.setSubjectTitle(SubjectModelRepository.getModel1().getTitle());
        timetableItemDto.setSubjectId(SubjectModelRepository.getModel1().getId());
        timetableItemDto.setAuditoryTitle(AuditoryModelRepository.getModel1().getAuditoryNumber());
        timetableItemDto.setAuditoryId(AuditoryModelRepository.getModel1().getId());
        List<GroupDto> groupDtos = asList(GroupDtoModelRepository.getModel1(), 
                                          GroupDtoModelRepository.getModel2());
        timetableItemDto.setGroups(groupDtos);
        timetableItemDto.setLectureTitle(LectureModelRepository.getModel1().getNumber());
        timetableItemDto.setLectureId(LectureModelRepository.getModel1().getId());
        timetableItemDto.setDate(LocalDate.of(2020, 06, 25));
        timetableItemDto.setTeacherTitle(TeacherModelRepository.getModel1().getFirstName());
        timetableItemDto.setTeacherId(TeacherModelRepository.getModel1().getId());
        return timetableItemDto;
    }
    
    public static TimetableItemDto getModel2() {
        TimetableItemDto timetableItemDto = new TimetableItemDto();
        timetableItemDto.setSubjectTitle(SubjectModelRepository.getModel2().getTitle());
        timetableItemDto.setSubjectId(SubjectModelRepository.getModel2().getId());
        timetableItemDto.setAuditoryTitle(AuditoryModelRepository.getModel2().getAuditoryNumber());
        timetableItemDto.setAuditoryId(AuditoryModelRepository.getModel2().getId());
        List<GroupDto> groupDtos = asList(GroupDtoModelRepository.getModel3(), 
                                          GroupDtoModelRepository.getModel4());
        timetableItemDto.setGroups(groupDtos);
        timetableItemDto.setLectureTitle(LectureModelRepository.getModel1().getNumber());
        timetableItemDto.setLectureId(LectureModelRepository.getModel1().getId());
        timetableItemDto.setDate(LocalDate.of(2020, 06, 25));
        timetableItemDto.setTeacherTitle(TeacherModelRepository.getModel2().getFirstName());
        timetableItemDto.setTeacherId(TeacherModelRepository.getModel2().getId());
        return timetableItemDto;
    }

    public static TimetableItemDto getModel3() {
        TimetableItemDto timetableItemDto = new TimetableItemDto();
        timetableItemDto.setSubjectTitle(SubjectModelRepository.getModel2().getTitle());
        timetableItemDto.setSubjectId(SubjectModelRepository.getModel2().getId());
        timetableItemDto.setAuditoryTitle(AuditoryModelRepository.getModel2().getAuditoryNumber());
        timetableItemDto.setAuditoryId(AuditoryModelRepository.getModel2().getId());
        List<GroupDto> groupDtos = asList(GroupDtoModelRepository.getModel2());
        timetableItemDto.setGroups(groupDtos);
        timetableItemDto.setLectureTitle(LectureModelRepository.getModel2().getNumber());
        timetableItemDto.setLectureId(LectureModelRepository.getModel2().getId());
        timetableItemDto.setDate(LocalDate.of(2020, 06, 25));
        timetableItemDto.setTeacherTitle(TeacherModelRepository.getModel2().getFirstName());
        timetableItemDto.setTeacherId(TeacherModelRepository.getModel2().getId());
        return timetableItemDto;
    }

    public static TimetableItemDto getModel4() {
        TimetableItemDto timetableItemDto = new TimetableItemDto();
        timetableItemDto.setSubjectTitle(SubjectModelRepository.getModel3().getTitle());
        timetableItemDto.setSubjectId(SubjectModelRepository.getModel3().getId());
        timetableItemDto.setAuditoryTitle(AuditoryModelRepository.getModel3().getAuditoryNumber());
        timetableItemDto.setAuditoryId(AuditoryModelRepository.getModel3().getId());
        List<GroupDto> groupDtos = asList(GroupDtoModelRepository.getModel1());
        timetableItemDto.setGroups(groupDtos);
        timetableItemDto.setLectureTitle(LectureModelRepository.getModel2().getNumber());
        timetableItemDto.setLectureId(LectureModelRepository.getModel2().getId());
        timetableItemDto.setDate(LocalDate.of(2020, 06, 25));
        timetableItemDto.setTeacherTitle(TeacherModelRepository.getModel3().getFirstName());
        timetableItemDto.setTeacherId(TeacherModelRepository.getModel3().getId());
        return timetableItemDto;
    }

    public static TimetableItemDto getModel5() {
        TimetableItemDto timetableItemDto = new TimetableItemDto();
        timetableItemDto.setSubjectTitle(SubjectModelRepository.getModel3().getTitle());
        timetableItemDto.setSubjectId(SubjectModelRepository.getModel3().getId());
        timetableItemDto.setAuditoryTitle(AuditoryModelRepository.getModel3().getAuditoryNumber());
        timetableItemDto.setAuditoryId(AuditoryModelRepository.getModel3().getId());
        List<GroupDto> groupDtos = asList(GroupDtoModelRepository.getModel2());
        timetableItemDto.setGroups(groupDtos);
        timetableItemDto.setLectureTitle(LectureModelRepository.getModel3().getNumber());
        timetableItemDto.setLectureId(LectureModelRepository.getModel3().getId());
        timetableItemDto.setDate(LocalDate.of(2020, 06, 25));
        timetableItemDto.setTeacherTitle(TeacherModelRepository.getModel3().getFirstName());
        timetableItemDto.setTeacherId(TeacherModelRepository.getModel3().getId());
        return timetableItemDto;
    }

    public static TimetableItemDto getModel6() {
        TimetableItemDto timetableItemDto = new TimetableItemDto();
        timetableItemDto.setSubjectTitle(SubjectModelRepository.getModel4().getTitle());
        timetableItemDto.setSubjectId(SubjectModelRepository.getModel4().getId());
        timetableItemDto.setAuditoryTitle(AuditoryModelRepository.getModel4().getAuditoryNumber());
        timetableItemDto.setAuditoryId(AuditoryModelRepository.getModel4().getId());
        List<GroupDto> groupDtos = asList(GroupDtoModelRepository.getModel2());
        timetableItemDto.setGroups(groupDtos);
        timetableItemDto.setLectureTitle(LectureModelRepository.getModel1().getNumber());
        timetableItemDto.setLectureId(LectureModelRepository.getModel1().getId());
        timetableItemDto.setDate(LocalDate.of(2020, 06, 25));
        timetableItemDto.setTeacherTitle(TeacherModelRepository.getModel3().getFirstName());
        timetableItemDto.setTeacherId(TeacherModelRepository.getModel3().getId());
        return timetableItemDto;
    }

    public static TimetableItemDto getModel7() {
        TimetableItemDto timetableItemDto = new TimetableItemDto();
        timetableItemDto.setSubjectTitle(SubjectModelRepository.getModel4().getTitle());
        timetableItemDto.setSubjectId(SubjectModelRepository.getModel4().getId());
        timetableItemDto.setAuditoryTitle(AuditoryModelRepository.getModel4().getAuditoryNumber());
        timetableItemDto.setAuditoryId(AuditoryModelRepository.getModel4().getId());
        List<GroupDto> groupDtos = asList(GroupDtoModelRepository.getModel3());
        timetableItemDto.setGroups(groupDtos);
        timetableItemDto.setLectureTitle(LectureModelRepository.getModel2().getNumber());
        timetableItemDto.setLectureId(LectureModelRepository.getModel2().getId());
        timetableItemDto.setDate(LocalDate.of(2020, 06, 25));
        timetableItemDto.setTeacherTitle(TeacherModelRepository.getModel3().getFirstName());
        timetableItemDto.setTeacherId(TeacherModelRepository.getModel3().getId());
        return timetableItemDto;
    }

    public static TimetableItemDto getModel8() {
        TimetableItemDto timetableItemDto = new TimetableItemDto();
        timetableItemDto.setSubjectTitle(SubjectModelRepository.getModel1().getTitle());
        timetableItemDto.setSubjectId(SubjectModelRepository.getModel1().getId());
        timetableItemDto.setAuditoryTitle(AuditoryModelRepository.getModel5().getAuditoryNumber());
        timetableItemDto.setAuditoryId(AuditoryModelRepository.getModel5().getId());
        List<GroupDto> groupDtos = asList(GroupDtoModelRepository.getModel4());
        timetableItemDto.setGroups(groupDtos);
        timetableItemDto.setLectureTitle(LectureModelRepository.getModel5().getNumber());
        timetableItemDto.setLectureId(LectureModelRepository.getModel5().getId());
        timetableItemDto.setDate(LocalDate.of(2020, 06, 25));
        timetableItemDto.setTeacherTitle(TeacherModelRepository.getModel1().getFirstName());
        timetableItemDto.setTeacherId(TeacherModelRepository.getModel1().getId());
        return timetableItemDto;
    }

    public static TimetableItemDto getModel9() {
        TimetableItemDto timetableItemDto = new TimetableItemDto();
        timetableItemDto.setSubjectTitle(SubjectModelRepository.getModel2().getTitle());
        timetableItemDto.setSubjectId(SubjectModelRepository.getModel2().getId());
        timetableItemDto.setAuditoryTitle(AuditoryModelRepository.getModel4().getAuditoryNumber());
        timetableItemDto.setAuditoryId(AuditoryModelRepository.getModel4().getId());
        List<GroupDto> groupDtos = asList(GroupDtoModelRepository.getModel1());
        timetableItemDto.setGroups(groupDtos);
        timetableItemDto.setLectureTitle(LectureModelRepository.getModel5().getNumber());
        timetableItemDto.setLectureId(LectureModelRepository.getModel5().getId());
        timetableItemDto.setDate(LocalDate.of(2020, 06, 25));
        timetableItemDto.setTeacherTitle(TeacherModelRepository.getModel2().getFirstName());
        timetableItemDto.setTeacherId(TeacherModelRepository.getModel2().getId());
        return timetableItemDto;
    }

    public static TimetableItemDto getModel10() {
        TimetableItemDto timetableItemDto = new TimetableItemDto();
        timetableItemDto.setSubjectTitle(SubjectModelRepository.getModel3().getTitle());
        timetableItemDto.setSubjectId(SubjectModelRepository.getModel3().getId());
        timetableItemDto.setAuditoryTitle(AuditoryModelRepository.getModel3().getAuditoryNumber());
        timetableItemDto.setAuditoryId(AuditoryModelRepository.getModel3().getId());
        List<GroupDto> groupDtos = asList(GroupDtoModelRepository.getModel4());
        timetableItemDto.setGroups(groupDtos);
        timetableItemDto.setLectureTitle(LectureModelRepository.getModel6().getNumber());
        timetableItemDto.setLectureId(LectureModelRepository.getModel6().getId());
        timetableItemDto.setDate(LocalDate.of(2020, 06, 25));
        timetableItemDto.setTeacherTitle(TeacherModelRepository.getModel3().getFirstName());
        timetableItemDto.setTeacherId(TeacherModelRepository.getModel3().getId());
        return timetableItemDto;
    }

}
