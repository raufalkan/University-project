package ua.com.foxminded.task.domain.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import ua.com.foxminded.task.validation.annotation.AuditoryAndTimeUnique;
import ua.com.foxminded.task.validation.annotation.TeacherAndTimeUnique;

@TeacherAndTimeUnique(fieldError = "teacherId", message = "Teacher will be busy at the time!")
@AuditoryAndTimeUnique(fieldError = "auditoryId", message = "Auditory will be busy at the time!")
@Schema(description = "Object with a full timetableItem information")
public class TimetableItemDto {

    @Min(value = 0, message = "Id must be more than zero!")
    @Schema(description = "The database generated timetableItem id")
    private int id;

    @Size(max = 45, message = "Maximum length of title subject is 45!")
    @Schema(description = "The subject name. Max length is 45 character")
    private String subjectTitle;

    @Schema(description = "The unique number of subject")
    private int subjectId;

    @Size(max = 20, message = "Maximum length of title auditory is 20!")
    @Schema(description = "The auditory title. Max length is 20 character")
    private String auditoryTitle;

    @Schema(description = "The unique number of auditory")
    private int auditoryId;

    @Schema(description = "The list of groups")
    private List<GroupDto> groups = new ArrayList<>();

    @Size(max = 5, message = "Maximum length of title of lecture is 5!")
    @Schema(description = "The lecture title. Max length is 5 character")
    private String lectureTitle;

    @Schema(description = "The unique number of lecture")
    private int lectureId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "The date of datetable item.")
    private LocalDate date;

    @Size(max = 20, message = "Maximum length of teacher first name is 20!")
    @Schema(description = "The teacher first name. Max length is 20 character")
    private String teacherTitle;

    @Schema(description = "The unique number of teacher")
    private int teacherId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubjectTitle() {
        return subjectTitle;
    }

    public void setSubjectTitle(String subjectTitle) {
        this.subjectTitle = subjectTitle;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getAuditoryTitle() {
        return auditoryTitle;
    }

    public void setAuditoryTitle(String auditoryTitle) {
        this.auditoryTitle = auditoryTitle;
    }

    public int getAuditoryId() {
        return auditoryId;
    }

    public void setAuditoryId(int auditoryId) {
        this.auditoryId = auditoryId;
    }

    public List<GroupDto> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupDto> groups) {
        this.groups = groups;
    }

    public String getLectureTitle() {
        return lectureTitle;
    }

    public void setLectureTitle(String lectureTitle) {
        this.lectureTitle = lectureTitle;
    }

    public int getLectureId() {
        return lectureId;
    }

    public void setLectureId(int lectureId) {
        this.lectureId = lectureId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTeacherTitle() {
        return teacherTitle;
    }

    public void setTeacherTitle(String teacherTitle) {
        this.teacherTitle = teacherTitle;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + auditoryId;
        result = prime * result + ((auditoryTitle == null) ? 0 : auditoryTitle.hashCode());
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((groups == null) ? 0 : groups.hashCode());
        result = prime * result + lectureId;
        result = prime * result + ((lectureTitle == null) ? 0 : lectureTitle.hashCode());
        result = prime * result + subjectId;
        result = prime * result + ((subjectTitle == null) ? 0 : subjectTitle.hashCode());
        result = prime * result + teacherId;
        result = prime * result + ((teacherTitle == null) ? 0 : teacherTitle.hashCode());
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
        TimetableItemDto other = (TimetableItemDto) obj;
        if (auditoryId != other.auditoryId) {
            return false;
        }
        if (auditoryTitle == null) {
            if (other.auditoryTitle != null) {
                return false;
            }
        } else if (!auditoryTitle.equals(other.auditoryTitle)) {
            return false;
        }
        if (date == null) {
            if (other.date != null) {
                return false;
            }
        } else if (!date.equals(other.date)) {
            return false;
        }
        if (groups == null) {
            if (other.groups != null) {
                return false;
            }
        } else if (!groups.equals(other.groups)) {
            return false;
        }
        if (lectureId != other.lectureId) {
            return false;
        }
        if (lectureTitle == null) {
            if (other.lectureTitle != null) {
                return false;
            }
        } else if (!lectureTitle.equals(other.lectureTitle)) {
            return false;
        }
        if (subjectId != other.subjectId) {
            return false;
        }
        if (subjectTitle == null) {
            if (other.subjectTitle != null) {
                return false;
            }
        } else if (!subjectTitle.equals(other.subjectTitle)) {
            return false;
        }
        if (teacherId != other.teacherId) {
            return false;
        }
        if (teacherTitle == null) {
            if (other.teacherTitle != null) {
                return false;
            }
        } else if (!teacherTitle.equals(other.teacherTitle)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TimetableItemDto [id=" + id
            + ", subjectTitle=" + subjectTitle
            + ", subjectId=" + subjectId
            + ", auditoryTitle=" + auditoryTitle
            + ", auditoryId=" + auditoryId
            + ", groups=" + groups
            + ", lectureTitle=" + lectureTitle
            + ", lectureId=" + lectureId
            + ", date=" + date
            + ", teacherTitle=" + teacherTitle
            + ", teacherId=" + teacherId
            + "]";
    }
}
