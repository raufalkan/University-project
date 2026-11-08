package ua.com.foxminded.task.dao.exception;

public class EntityNotValidException extends RuntimeException {

    public EntityNotValidException(String message, Throwable cause) {
        super(message, cause);
    }

}
