package ua.com.foxminded.task.domain.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import ua.com.foxminded.task.validation.annotation.PropertyValueUnique;

@PropertyValueUnique(message = "Title is already exists!",
    nameProperty = "title",
    fieldError = "title")
@Schema(description = "Object with a group information")
public class GroupDto {

    @Min(value = 0, message = "Id must be more than zero!")
    @Schema(description = "The database generated group id")
    private int id;

    @NotBlank(message = "Title can`t be blank!")
    @Size(max = 20, message = "Maximum length is 20!")
    @Schema(description = "The unique name of group. Max length is 20 character")
    private String title;

    @NotNull(message = "Year of entry can`t be blank or not numeric!")
    @Max(value = 2100, message = "Year of entry is not correct!")
    @Min(value = 2000, message = "Year of entry is not correct!")
    @Schema(description = "The year of entry students in the group. Value between 2000-2100")
    private Integer yearEntry;

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYearEntry(Integer yearEntry) {
        this.yearEntry = yearEntry;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Integer getYearEntry() {
        return yearEntry;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
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
        GroupDto other = (GroupDto) obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GroupDto [id=" + id
            + ", title=" + title
            + ", yearEntry=" + yearEntry
            + "]";
    }
}
