package ua.com.foxminded.task.homework.umut;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Umut domain model suite")
@SelectClasses({
        GroupModelTest.class,
        StudentModelTest.class,
        TeacherModelTest.class,
        TimetableItemModelTest.class
})
public class UmutDomainSuite {
}
