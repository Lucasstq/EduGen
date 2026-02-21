package dev.lucas.edugen.EduGen.eduGenException.resourceNotFoundException;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
