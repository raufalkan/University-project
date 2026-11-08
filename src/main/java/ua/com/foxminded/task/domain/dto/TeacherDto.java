package ua.com.foxminded.task.domain.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import ua.com.foxminded.task.validation.annotation.PropertyValueUnique;

@PropertyValueUnique(message = "Teacher id fees is already exists!",
    nameProperty = "idFees",
    fieldError = "idFees")
@Schema(description = "Object with a teacher and department information")
public class TeacherDto {

    @Min(value = 0, message = "Id must be more than zero!")
    @Schema(description = "The database generated teacher id")
    private int id;

    @NotBlank(message = "First name can`t be blank!")
    @Size(max = 20, message = "Maximum length of first name is 20!")
    @Schema(description = "The teacher first name. Not blank. Max length is 20 character")
    private String firstName;

    @Size(max = 20, message = "Maximum length of middle name is 20!")
    @Schema(description = "The teacher middle name. Max length is 20 character")
    private String middleName;

    @Size(max = 20, message = "Maximum length of last name is 20!")
    @Schema(description = "The teacher last name. Max length is 20 character")
    private String lastName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "Birthday date must be in past!")
    @Schema(description = "The teacher birthday date. Date must be in past")
    private LocalDate birthday;

    @Min(value = 100000000, message = "Value is 9 number!")
    @Max(value = 999999999, message = "Value is 9 number!")
    @Schema(description = "The teacher identification number in an fees authority. Value is 9 number")
    private int idFees;

    @Size(max = 20, message = "Maximum length of title department is 20!")
    @Schema(description = "The teacher name of department. Max length is 20 character")
    private String departmentTitle;

    @Schema(description = "The unique number of teacher department")
    private int departmentId;

    @Schema(description = "The list of subjects by teacher")
    private List<SubjectDto> subjects = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public int getIdFees() {
        return idFees;
    }

    public void setIdFees(int idFees) {
        this.idFees = idFees;
    }

    public String getDepartmentTitle() {
        return departmentTitle;
    }

    public void setDepartmentTitle(String departmentTitle) {
        this.departmentTitle = departmentTitle;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public List<SubjectDto> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<SubjectDto> subjects) {
        this.subjects = subjects;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((birthday == null) ? 0 : birthday.hashCode());
        result = prime * result + ((departmentTitle == null) ? 0 : departmentTitle.hashCode());
        result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + departmentId;
        result = prime * result + idFees;
        result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
        result = prime * result + ((middleName == null) ? 0 : middleName.hashCode());
        result = prime * result + ((subjects == null) ? 0 : subjects.hashCode());
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
        TeacherDto other = (TeacherDto) obj;
        if (birthday == null) {
            if (other.birthday != null) {
                return false;
            }
        } else if (!birthday.equals(other.birthday)) {
            return false;
        }
        if (departmentTitle == null) {
            if (other.departmentTitle != null) {
                return false;
            }
        } else if (!departmentTitle.equals(other.departmentTitle)) {
            return false;
        }
        if (firstName == null) {
            if (other.firstName != null) {
                return false;
            }
        } else if (!firstName.equals(other.firstName)) {
            return false;
        }
        if (departmentId != other.departmentId) {
            return false;
        }
        if (idFees != other.idFees) {
            return false;
        }
        if (lastName == null) {
            if (other.lastName != null) {
                return false;
            }
        } else if (!lastName.equals(other.lastName)) {
            return false;
        }
        if (middleName == null) {
            if (other.middleName != null) {
                return false;
            }
        } else if (!middleName.equals(other.middleName)) {
            return false;
        }
        if (subjects == null) {
            if (other.subjects != null) {
                return false;
            }
        } else if (!subjects.equals(other.subjects)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TeacherDto [id=" + id
            + ", firstName=" + firstName
            + ", middleName=" + middleName
            + ", lastName=" + lastName
            + ", birthday=" + birthday
            + ", idFees=" + idFees
            + ", departmentTitle=" + departmentTitle
            + ", departmentId=" + departmentId
            + ", subjects=" + subjects
            + "]";
    }
}
