package ua.com.foxminded.task.homework.rauf;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import ua.com.foxminded.task.dao.FacultyRepository;
import ua.com.foxminded.task.domain.Faculty;
import ua.com.foxminded.task.domain.dto.FacultyDto;
import ua.com.foxminded.task.service.impl.FacultyServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FacultyServiceReadTest {

    @Mock
    private Logger logger;

    @Mock
    private FacultyRepository facultyRepository;

    private FacultyServiceImpl service;

    @BeforeEach
    void setUp(){
        service = new FacultyServiceImpl(logger, facultyRepository);
    }

    @Test
    @DisplayName("findByIdDto returns dto with same id and name")
    void findByIdDtoReturnsMatchingDto(){
        when(facultyRepository.getOne(1)).thenReturn(faculty(1,"Engineering"));

        FacultyDto dto = service.findByIdDto(1);

        assertAll(
                () -> assertEquals(1, dto.getId()),
                () -> assertEquals("Engineering", dto.getTitle())
        );
    }

    @Test
    @DisplayName("findAllDto retuns all faculties in repository in order")
    void findAllDtoKeepsOrder(){
        when(facultyRepository.findAll()).thenReturn(Arrays.asList(faculty(1,"Law"),faculty(2,"Medicine")));

        List<FacultyDto> result = service.findAllDto();

        assertEquals(2, result.size());
        assertIterableEquals(
                Arrays.asList("Law", "Medicine"),
                result.stream().map(FacultyDto :: getTitle).collect(Collectors.toList())
        );
    }

    @Test
    @DisplayName("findAllDto return empty list on empty repository")
    void findAllDtoEmptyRepositoryRetunEmptyList(){
        when(facultyRepository.findAll()).thenReturn(Collections.emptyList());

        List<FacultyDto> result = service.findAllDto();

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("findByTitle return same titile in repository")
    void findByTitleReturnsTitleEntitiy(){
        Faculty faculty = faculty(1,"Enginering");

        when(facultyRepository.findByTitle("Engineering")).thenReturn(faculty);

        Faculty result =  service.findByTitle("Engineering");

        assertSame(faculty, result);
    }

    @Test
    @DisplayName("findByTitle retuns null for unknown values")
    void findByTitleReturnsNullUnknownTitle(){
        when(service.findByTitle("Unknown")).thenReturn(null);
        Faculty result = service.findByTitle("Unknown");
        assertNull(result);
    }



    private Faculty faculty(int id, String title){
        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setTitle(title);
        return faculty;
    }

}
