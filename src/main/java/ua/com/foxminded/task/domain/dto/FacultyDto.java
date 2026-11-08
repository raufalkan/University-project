package ua.com.foxminded.task.domain.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import ua.com.foxminded.task.validation.annotation.PropertyValueUnique;

@PropertyValueUnique(message = "Faculty title is already exists!",
    nameProperty = "title",
    fieldError = "title")
@Schema(description = "Object with a faculty information")
public class FacultyDto {

    @Min(value = 0, message = "Id must be more than zero!")
    @Schema(description = "The database generated faculty id")
    private int id;

    @NotBlank(message = "Title can`t be blank!")
    @Size(max = 20, message = "Maximum length is 20!")
    @Schema(description = "The unique name of faculty. Max length is 20 character")
    private String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((title == null) ? 0 : title.hashCode());
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
        FacultyDto other = (FacultyDto) obj;
        if (title == null) {
            if (other.title != null) {
                return false;
            }
        } else if (!title.equals(other.title)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FacultyDto [id=" + id
            + ", title=" + title
            + "]";
    }
}
