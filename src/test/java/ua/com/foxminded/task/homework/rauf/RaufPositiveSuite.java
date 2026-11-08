package ua.com.foxminded.task.homework.rauf;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Rauf positive suite")
@SelectClasses({
        FacultyServiceReadTest.class,
        FacultyServiceWriteTest.class,
        GroupServiceTest.class,
        LectureServiceTest.class,
        AuditoryTypeServiceTest.class
})
public class RaufPositiveSuite {
}