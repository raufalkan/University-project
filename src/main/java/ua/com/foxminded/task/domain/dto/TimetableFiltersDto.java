package ua.com.foxminded.task.domain.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.Min;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Object for finding period timetableItems")
public class TimetableFiltersDto implements Serializable {

    private static final long serialVersionUID = 5672207011636676194L;

    @Min(value = 0, message = "Id must be more than zero!")
    @Schema(description = "The teacher id of entity")
    private int selectedTeacher;

    @Min(value = 0, message = "Id must be more than zero!")
    @Schema(description = "The student id of entity")
    private int selectedStudent;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "The date of search period starting.")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "The date of search period be finished.")
    private LocalDate endDate;

    @Schema(description = "The all of teachers for selector")
    private List<TeacherDto> availableTeachers;

    @Schema(description = "The all of students for selector")
    private List<StudentDto> availableStudents;


    public int getSelectedTeacher() {
        return selectedTeacher;
    }

    public void setSelectedTeacher(int teacherId) {
        this.selectedTeacher = teacherId;
    }

    public int getSelectedStudent() {
        return selectedStudent;
    }

    public void setSelectedStudent(int studentId) {
        this.selectedStudent = studentId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public List<TeacherDto> getAvailableTeachers() {
        return availableTeachers;
    }

    public void setAvailableTeachers(List<TeacherDto> availableTeachers) {
        this.availableTeachers = availableTeachers;
    }

    public List<StudentDto> getAvailableStudents() {
        return availableStudents;
    }

    public void setAvailableStudents(List<StudentDto> availableStudents) {
        this.availableStudents = availableStudents;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
        result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
        result = prime * result + selectedStudent;
        result = prime * result + selectedTeacher;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        TimetableFiltersDto other = (TimetableFiltersDto) obj;
        if (endDate == null) {
            if (other.endDate != null) {
                return false;
            }
        } else if (!endDate.equals(other.endDate)) {
            return false;
        }
        if (startDate == null) {
            if (other.startDate != null) {
                return false;
            }
        } else if (!startDate.equals(other.startDate)) {
            return false;
        }
        if (selectedStudent != other.selectedStudent) {
            return false;
        }
        if (selectedTeacher != other.selectedTeacher) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FiltersDto [teacherId=" + selectedTeacher
            + ", studentId=" + selectedStudent
            + ", startDate=" + startDate
            + ", endDate=" + endDate
            + "]";
    }
}
