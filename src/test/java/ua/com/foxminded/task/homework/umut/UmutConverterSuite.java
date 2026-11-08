package ua.com.foxminded.task.homework.umut;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Umut converter suite")
@SelectClasses({
        PersonConverterTest.class,
        TimetableConverterTest.class
})
public class UmutConverterSuite {
}
