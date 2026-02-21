package dev.lucas.edugen.EduGen.eduGenException.businessExeception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException() {
        super("Email já cadastrado. Por favor, escolha outro email.");
    }
}
