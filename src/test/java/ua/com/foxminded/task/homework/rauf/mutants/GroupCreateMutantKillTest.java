package ua.com.foxminded.task.homework.rauf.mutants;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import ua.com.foxminded.task.dao.GroupRepository;
import ua.com.foxminded.task.dao.exception.EntityNotValidException;
import ua.com.foxminded.task.domain.Group;
import ua.com.foxminded.task.domain.dto.GroupDto;

@ExtendWith(MockitoExtension.class)
public class GroupCreateMutantKillTest {

    @Mock
    private GroupRepository groupRepository;

    private GroupDto input;

    private void applyHw1Oracle(GroupDto result) {
        assertAll(
                () -> assertEquals(7, result.getId()),
                () -> assertEquals("KB-21", result.getTitle()),
                () -> assertEquals(Integer.valueOf(2021), result.getYearEntry()));
    }

    @BeforeEach
    void setUp() {
        input = new GroupDto();
        input.setTitle("KB-21");
        input.setYearEntry(2021);

        lenient().when(groupRepository.saveAndFlush(any(Group.class)))
                 .thenReturn(persistedGroup());
    }

    private Group persistedGroup() {
        Group g = new Group();
        g.setId(7);
        g.setTitle("KB-21");
        g.setYearEntry(2021);
        return g;
    }

    @Test
    @DisplayName("baseline: original() passes HW1 oracle (sanity check)")
    void baseline_original_passes() {
        GroupDto result = GroupCreateMutants.original(groupRepository, input);
        assertDoesNotThrow(() -> applyHw1Oracle(result));
    }

    @Test
    @DisplayName("M1 killed: inverted guard throws on valid input")
    void kill_m1_invertedGuard() {
        assertThrows(RuntimeException.class,
                () -> GroupCreateMutants.mutant1(groupRepository, input));
    }

    @Test
    @DisplayName("M2 killed: yearEntry negated -> captured entity has -2021")
    void kill_m2_negatedYearEntry() {
        org.mockito.ArgumentCaptor<Group> captor =
                org.mockito.ArgumentCaptor.forClass(Group.class);
        when(groupRepository.saveAndFlush(captor.capture()))
                .thenReturn(persistedGroup());

        GroupCreateMutants.mutant2(groupRepository, input);
        Group persisted = captor.getValue();

        assertThrows(AssertionError.class,
                () -> assertEquals(Integer.valueOf(2021), persisted.getYearEntry()));
    }

    @Test
    @DisplayName("M3 killed: return null causes NPE in oracle")
    void kill_m3_returnNull() {
        GroupDto result = GroupCreateMutants.mutant3(groupRepository, input);
        assertThrows(Throwable.class, () -> applyHw1Oracle(result));
    }

    @Test
    @DisplayName("M4 killed: wrong exception type on DataIntegrityViolation")
    void kill_m4_wrongExceptionType() {
        when(groupRepository.saveAndFlush(any(Group.class)))
                .thenThrow(new DataIntegrityViolationException("boom"));

        Throwable mutantException = assertThrows(RuntimeException.class,
                () -> GroupCreateMutants.mutant4(groupRepository, input));
        if (mutantException instanceof EntityNotValidException) {
            throw new AssertionError("M4 survived: same exception type as original");
        }
    }

    @Test
    @DisplayName("M5 killed: title setter deleted -> captured entity has null title")
    void kill_m5_deletedTitleSetter() {
        org.mockito.ArgumentCaptor<Group> captor =
                org.mockito.ArgumentCaptor.forClass(Group.class);
        when(groupRepository.saveAndFlush(captor.capture()))
                .thenReturn(persistedGroup());

        GroupCreateMutants.mutant5(groupRepository, input);
        Group persisted = captor.getValue();

        assertThrows(AssertionError.class,
                () -> assertEquals("KB-21", persisted.getTitle()));
    }

    @Test
    @DisplayName("M6 killed: yearEntry setter deleted -> captured entity has null year")
    void kill_m6_deletedYearSetter() {
        org.mockito.ArgumentCaptor<Group> captor =
                org.mockito.ArgumentCaptor.forClass(Group.class);
        when(groupRepository.saveAndFlush(captor.capture()))
                .thenReturn(persistedGroup());

        GroupCreateMutants.mutant6(groupRepository, input);
        Group persisted = captor.getValue();

        assertThrows(AssertionError.class,
                () -> assertEquals(Integer.valueOf(2021), persisted.getYearEntry()));
    }

    @Test
    @DisplayName("M7 killed: saveAndFlush replaced with getOne -> no generated id")
    void kill_m7_methodSwap() {
        Throwable t = assertThrows(Throwable.class, () -> {
            GroupDto result = GroupCreateMutants.mutant7(groupRepository, input);
            applyHw1Oracle(result);
        });
        if (!(t instanceof AssertionError) && !(t instanceof NullPointerException)) {
            throw new AssertionError("Unexpected throwable type: " + t);
        }
    }

    @Test
    @DisplayName("M8 killed: returns input dto -> id stays 0, assertion fails")
    void kill_m8_returnsInputDto() {
        GroupDto result = GroupCreateMutants.mutant8(groupRepository, input);
        assertThrows(AssertionError.class, () -> applyHw1Oracle(result));
    }

    @Test
    @DisplayName("M9 EQUIVALENT: exception message changed -> HW1 oracle cannot detect")
    void equivalent_m9_messageConstant() {
        GroupDto result = GroupCreateMutants.mutant9(groupRepository, input);
        assertDoesNotThrow(() -> applyHw1Oracle(result));
    }

    @Test
    @DisplayName("M10 killed: save result discarded -> returned dto has id=0")
    void kill_m10_discardedReturn() {
        GroupDto result = GroupCreateMutants.mutant10(groupRepository, input);
        assertThrows(AssertionError.class, () -> applyHw1Oracle(result));
    }
}
