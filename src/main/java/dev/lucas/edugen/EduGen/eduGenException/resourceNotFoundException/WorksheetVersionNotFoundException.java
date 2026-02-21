package dev.lucas.edugen.EduGen.eduGenException.resourceNotFoundException;

public class WorksheetVersionNotFoundException extends RuntimeException {
    public WorksheetVersionNotFoundException(String message) {
        super(message);
    }
}
