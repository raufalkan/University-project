package ua.com.foxminded.task.domain;

import java.time.LocalDate;

public class TimetableFilters {
    
    private int selectedTeacher;

    private int selectedStudent;

    private LocalDate startDate;

    private LocalDate endDate;

    public int getSelectedTeacher() {
        return selectedTeacher;
    }

    public void setSelectedTeacher(int selectedTeacher) {
        this.selectedTeacher = selectedTeacher;
    }

    public int getSelectedStudent() {
        return selectedStudent;
    }

    public void setSelectedStudent(int selectedStudent) {
        this.selectedStudent = selectedStudent;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
        result = prime * result + selectedStudent;
        result = prime * result + selectedTeacher;
        result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TimetableFilters other = (TimetableFilters) obj;
        if (endDate == null) {
            if (other.endDate != null)
                return false;
        } else if (!endDate.equals(other.endDate))
            return false;
        if (selectedStudent != other.selectedStudent)
            return false;
        if (selectedTeacher != other.selectedTeacher)
            return false;
        if (startDate == null) {
            if (other.startDate != null)
                return false;
        } else if (!startDate.equals(other.startDate))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "TimetableFilters [selectedTeacher=" + selectedTeacher 
             + ", selectedStudent=" + selectedStudent 
             + ", startDate=" + startDate 
             + ", endDate=" + endDate 
             + "]";
    }
    
}
