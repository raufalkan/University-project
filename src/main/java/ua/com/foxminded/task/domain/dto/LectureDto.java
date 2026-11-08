package ua.com.foxminded.task.domain.dto;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import java.time.LocalTime;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import ua.com.foxminded.task.validation.annotation.PropertyValueUnique;

@PropertyValueUnique(message = "Lecture number is already exists!",
    nameProperty = "number",
    fieldError = "number")
@Schema(description = "Object with a lecture time information")
public class LectureDto {

    @Min(value = 0, message = "Id must be more than zero!")
    @Schema(description = "The database generated lecture id")
    private int id;

    @NotBlank(message = "Number name can`t be blank!")
    @Size(max = 5, message = "Maximum length is 5!")
    @Schema(description = "The unique number name of lecture. Max length is 5 character")
    private String number;

    @DateTimeFormat(iso = ISO.TIME)
    @Schema(description = "The time of lecture beginning. Format must be 09:20:00")
    private LocalTime startTime;

    @DateTimeFormat(iso = ISO.TIME)
    @Schema(description = "The time of lecture be finished. Format must be 09:20:00")
    private LocalTime endTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
        result = prime * result + ((number == null) ? 0 : number.hashCode());
        result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
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
        LectureDto other = (LectureDto) obj;
        if (endTime == null) {
            if (other.endTime != null) {
                return false;
            }
        } else if (!endTime.equals(other.endTime)) {
            return false;
        }
        if (number == null) {
            if (other.number != null) {
                return false;
            }
        } else if (!number.equals(other.number)) {
            return false;
        }
        if (startTime == null) {
            if (other.startTime != null) {
                return false;
            }
        } else if (!startTime.equals(other.startTime)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "LectureDto [id=" + id
            + ", number=" + number
            + ", startTime=" + startTime
            + ", endTime=" + endTime
            + "]";
    }
}
