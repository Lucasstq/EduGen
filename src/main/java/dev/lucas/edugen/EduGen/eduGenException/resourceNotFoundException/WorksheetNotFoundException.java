package dev.lucas.edugen.EduGen.eduGenException.resourceNotFoundException;

public class WorksheetNotFoundException extends RuntimeException {
    public WorksheetNotFoundException(String message) {
        super(message);
    }
}
