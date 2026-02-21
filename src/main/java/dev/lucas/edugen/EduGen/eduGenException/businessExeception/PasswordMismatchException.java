package dev.lucas.edugen.EduGen.eduGenException.businessExeception;

public class PasswordMismatchException extends RuntimeException {
    public PasswordMismatchException() {
        super("As senhas não coincidem. Por favor, verifique e tente novamente.");
    }
}
