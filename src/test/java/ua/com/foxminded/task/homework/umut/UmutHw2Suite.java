package ua.com.foxminded.task.homework.umut;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Umut HW2 suite (basis path + decision table)")
@SelectClasses({
        TimetableConverterBasisPathTest.class,
        TimetableConverterDecisionTableTest.class
})
public class UmutHw2Suite {
}
