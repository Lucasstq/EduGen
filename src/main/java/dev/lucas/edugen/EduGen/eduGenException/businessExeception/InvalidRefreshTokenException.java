package dev.lucas.edugen.EduGen.eduGenException.businessExeception;

public class InvalidRefreshTokenException extends RuntimeException {
    public InvalidRefreshTokenException(String message) {
        super(message);
    }
}
