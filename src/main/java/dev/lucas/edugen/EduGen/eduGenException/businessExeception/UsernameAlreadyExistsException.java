package dev.lucas.edugen.EduGen.eduGenException.businessExeception;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException() {
        super("Nome de usuário já existe. Por favor, escolha outro nome de usuário.");
    }
}
