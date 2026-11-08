package ua.com.foxminded.task.homework.rauf;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Rauf error suite")
@SelectClasses({
        CreateRejectsExistingId.class,
        UpdateNotFoundTest.class,
        DataIntegrityWrappingTest.class
})
public class RaufErrorSuite {
}