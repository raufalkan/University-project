package ua.com.foxminded.task.validation.validator.property.unique;

public interface Command {
    boolean check(String fieldId, String fieldUnique);
}
