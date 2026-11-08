package ua.com.foxminded.task.domain.repository.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ua.com.foxminded.task.domain.dto.GroupDto;

public class GroupDtoModelRepository {

    private GroupDtoModelRepository() {
    }

    public static List<GroupDto> getModels() {
        List<GroupDto> groups = Arrays.asList(getModel1(), getModel2(), getModel3(), getModel4());
        return new ArrayList<>(groups);
    }
    
    public static List<GroupDto> getModelDtos() {
        List<GroupDto> groups = Arrays.asList(getModel11(), getModel12(), getModel13(), getModel14());
        return new ArrayList<>(groups);
    }
    
    public static List<GroupDto> getModelDtosWithId() {
        GroupDto groupDto1 = getModel11();
        GroupDto groupDto2 = getModel12();
        GroupDto groupDto3 = getModel13();
        GroupDto groupDto4 = getModel14();
        groupDto1.setId(1);
        groupDto2.setId(2);
        groupDto3.setId(3);
        groupDto4.setId(4);
        List<GroupDto> groups = Arrays.asList(groupDto1, groupDto2, groupDto3, groupDto4);
        return new ArrayList<>(groups);
    }

    public static List<GroupDto> getModels1() {
        List<GroupDto> groups = Arrays.asList(getModel1(), getModel2());
        return new ArrayList<>(groups);
    }

    public static List<GroupDto> getModels2() {
        List<GroupDto> groups = Arrays.asList(getModel3(), getModel4());
        return new ArrayList<>(groups);
    }

    public static List<GroupDto> getModels3() {
        List<GroupDto> groups = Arrays.asList(getModel5());
        return new ArrayList<>(groups);
    }

    public static List<GroupDto> getModels4() {
        List<GroupDto> groups = Arrays.asList(getModel6(), getModel7());
        return new ArrayList<>(groups);
    }

    public static List<GroupDto> getModels5() {
        List<GroupDto> groups = Arrays.asList(getModel8());
        return new ArrayList<>(groups);
    }

    public static GroupDto getModelWithId() {
        GroupDto group = new GroupDto();
        group.setId(1);
        group.setTitle("group21");
        group.setYearEntry(2016);
        return group;
    }

    public static GroupDto getModel1() {
        GroupDto group = new GroupDto();
        group.setTitle("group1");
        group.setYearEntry(2016);
        return group;
    }

    public static GroupDto getModel2() {
        GroupDto group = new GroupDto();
        group.setTitle("group2");
        group.setYearEntry(2018);
        return group;
    }

    public static GroupDto getModel3() {
        GroupDto group = new GroupDto();
        group.setTitle("group3");
        group.setYearEntry(2017);
        return group;
    }

    public static GroupDto getModel4() {
        GroupDto group = new GroupDto();
        group.setTitle("group4");
        group.setYearEntry(2016);
        return group;
    }

    public static GroupDto getModel5() {
        GroupDto group = new GroupDto();
        group.setTitle("group5");
        group.setYearEntry(2010);
        return group;
    }

    public static GroupDto getModel6() {
        GroupDto group = new GroupDto();
        group.setTitle("group6");
        group.setYearEntry(2019);
        return group;
    }

    private static GroupDto getModel7() {
        GroupDto group = new GroupDto();
        group.setTitle("group7");
        group.setYearEntry(2017);
        return group;
    }

    private static GroupDto getModel8() {
        GroupDto group = new GroupDto();
        group.setTitle("group8");
        group.setYearEntry(2013);
        return group;
    }

    public static GroupDto getModel11() {
        GroupDto group = new GroupDto();
        group.setTitle("group11");
        group.setYearEntry(2016);
        return group;
    }

    public static GroupDto getModel12() {
        GroupDto group = new GroupDto();
        group.setTitle("group12");
        group.setYearEntry(2018);
        return group;
    }

    public static GroupDto getModel13() {
        GroupDto group = new GroupDto();
        group.setTitle("group13");
        group.setYearEntry(2017);
        return group;
    }
    
    public static GroupDto getModel14() {
        GroupDto group = new GroupDto();
        group.setTitle("group14");
        group.setYearEntry(2016);
        return group;
    }
}
