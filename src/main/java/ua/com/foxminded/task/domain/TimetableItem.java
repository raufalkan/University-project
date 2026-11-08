package ua.com.foxminded.task.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "timetable_items")
public class TimetableItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;
    
    @ManyToOne
    @JoinColumn(name = "auditory_id")
    private Auditory auditory;
    
    @OneToMany
    @JoinTable(
            name = "groups_timetable_items", 
            joinColumns = @JoinColumn(name = "timetable_item_id"), 
            inverseJoinColumns = @JoinColumn(name = "group_id"))
    private List<Group> groups = new ArrayList<>();
    
    @ManyToOne
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;
    
    @Column(name = "date")
    private LocalDate date;
    
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    public void addGroup(Group group) {
        groups.add(group);
    }

    public void removeGroup(Group group) {
        groups.remove(group);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Auditory getAuditory() {
        return auditory;
    }

    public void setAuditory(Auditory auditory) {
        this.auditory = auditory;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public Lecture getLecture() {
        return lecture;
    }

    public void setLecture(Lecture lecture) {
        this.lecture = lecture;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((auditory == null) ? 0 : auditory.hashCode());
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((groups == null) ? 0 : groups.hashCode());
        result = prime * result + ((lecture == null) ? 0 : lecture.hashCode());
        result = prime * result + ((subject == null) ? 0 : subject.hashCode());
        result = prime * result + ((teacher == null) ? 0 : teacher.hashCode());
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
        TimetableItem other = (TimetableItem) obj;
        if (auditory == null) {
            if (other.auditory != null)
                return false;
        } else if (!auditory.equals(other.auditory))
            return false;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        if (groups == null) {
            if (other.groups != null)
                return false;
        } else if (!groups.equals(other.groups))
            return false;
        if (lecture == null) {
            if (other.lecture != null)
                return false;
        } else if (!lecture.equals(other.lecture))
            return false;
        if (subject == null) {
            if (other.subject != null)
                return false;
        } else if (!subject.equals(other.subject))
            return false;
        if (teacher == null) {
            if (other.teacher != null)
                return false;
        } else if (!teacher.equals(other.teacher))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "TimetableItem [id=" + id 
             + ", subject=" + subject 
             + ", auditory=" + auditory 
             + ", groups=" + groups 
             + ", lecture=" + lecture 
             + ", date=" + date 
             + ", teacher=" + teacher 
             + "]";
    }
   
}
