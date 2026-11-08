package ua.com.foxminded.task.domain.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ua.com.foxminded.task.domain.Group;

public class GroupModelRepository {

    private GroupModelRepository() {
    }

    public static List<Group> getModels() {
        List<Group> groups = Arrays.asList(getModel1(), getModel2(), getModel3(), getModel4());
        return new ArrayList<>(groups);
    }

    public static List<Group> getModels1() {
        List<Group> groups = Arrays.asList(getModel1(), getModel2());
        return new ArrayList<>(groups);
    }

    public static List<Group> getModels2() {
        List<Group> groups = Arrays.asList(getModel3(), getModel4());
        return new ArrayList<>(groups);
    }

    public static List<Group> getModels3() {
        List<Group> groups = Arrays.asList(getModel5());
        return new ArrayList<>(groups);
    }

    public static List<Group> getModels4() {
        List<Group> groups = Arrays.asList(getModel6(), getModel7());
        return new ArrayList<>(groups);
    }

    public static List<Group> getModels5() {
        List<Group> groups = Arrays.asList(getModel8());
        return new ArrayList<>(groups);
    }

    public static Group getEmptyModel() {
        Group group = new Group();
        group.setTitle("group empty");
        group.setYearEntry(2016);
        return group;
    }

    public static Group getModelWithId() {
        Group group = new Group();
        group.setId(1);
        group.setTitle("group1");
        group.setYearEntry(2016);
        return group;
    }

    public static Group getModel1() {
        Group group = new Group();
        group.setTitle("group1");
        group.setYearEntry(2016);
        return group;
    }

    public static Group getModel2() {
        Group group = new Group();
        group.setTitle("group2");
        group.setYearEntry(2018);
        return group;
    }

    public static Group getModel3() {
        Group group = new Group();
        group.setTitle("group3");
        group.setYearEntry(2017);
        return group;
    }

    public static Group getModel4() {
        Group group = new Group();
        group.setTitle("group4");
        group.setYearEntry(2016);
        return group;
    }

    public static Group getModel5() {
        Group group = new Group();
        group.setTitle("group5");
        group.setYearEntry(2010);
        return group;
    }

    private static Group getModel6() {
        Group group = new Group();
        group.setTitle("group6");
        group.setYearEntry(2019);
        return group;
    }

    private static Group getModel7() {
        Group group = new Group();
        group.setTitle("group7");
        group.setYearEntry(2017);
        return group;
    }

    private static Group getModel8() {
        Group group = new Group();
        group.setTitle("group8");
        group.setYearEntry(2013);
        return group;
    }

    public static Group getModel11() {
        Group group = new Group();
        group.setTitle("group11");
        group.setYearEntry(2016);
        return group;
    }

    public static Group getModel12() {
        Group group = new Group();
        group.setTitle("group12");
        group.setYearEntry(2018);
        return group;
    }

    public static Group getModel13() {
        Group group = new Group();
        group.setTitle("group13");
        group.setYearEntry(2017);
        return group;
    }
}
