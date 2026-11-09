package ua.com.foxminded.task.homework;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

import ua.com.foxminded.task.homework.rauf.AuditoryAndTimeUniqueBasisPathTest;
import ua.com.foxminded.task.homework.rauf.MaxStudentsInGroupLimitBasisPathTest;
import ua.com.foxminded.task.homework.rauf.MaxStudentsInGroupLimitDecisionTableTest;
import ua.com.foxminded.task.homework.rauf.mutants.GroupCreateMutantKillTest;
import ua.com.foxminded.task.homework.umut.TimetableConverterBasisPathTest;
import ua.com.foxminded.task.homework.umut.TimetableConverterDecisionTableTest;

@Suite
@SuiteDisplayName("HW2 suite (basis path + decision table + mutation)")
@SelectClasses({
        // Rauf
        MaxStudentsInGroupLimitBasisPathTest.class,
        MaxStudentsInGroupLimitDecisionTableTest.class,
        AuditoryAndTimeUniqueBasisPathTest.class,
        GroupCreateMutantKillTest.class,
        // Umut
        TimetableConverterBasisPathTest.class,
        TimetableConverterDecisionTableTest.class
})
public class Hw2Suite {
}
