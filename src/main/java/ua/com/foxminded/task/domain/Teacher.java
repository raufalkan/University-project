package ua.com.foxminded.task.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "teachers")
@PrimaryKeyJoinColumn(name = "person_id")
public class Teacher extends Person {

    @OneToMany
    @JoinTable(
            name = "teachers_subjects", 
            joinColumns = @JoinColumn(name = "teacher_id"), 
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private List<Subject> subjects = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    public Teacher() {
        super();
    }

    public void addSubject(Subject subject) {
        subjects.add(subject);
    }

    public void removeSubject(Subject subject) {
        subjects.remove(subject);
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((department == null) ? 0 : department.hashCode());
        result = prime * result + ((subjects == null) ? 0 : subjects.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Teacher other = (Teacher) obj;
        if (department == null) {
            if (other.department != null)
                return false;
        } else if (!department.equals(other.department))
            return false;
        if (subjects == null) {
            if (other.subjects != null)
                return false;
        } else if (!subjects.equals(other.subjects))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Teacher [subjects=" + subjects 
             + ", department=" + department 
             + " " + super.toString() 
             + "]";
    }

}
