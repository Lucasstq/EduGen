package dev.lucas.edugen.EduGen.eduGenException.businessExeception;

public class EmailTokenInvalidException extends RuntimeException {
    public EmailTokenInvalidException(String message) {
        super(message);
    }
}
