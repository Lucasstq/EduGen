package dev.lucas.edugen.EduGen.eduGenException.businessExeception;

public class PasswordMustDifferentException extends RuntimeException {
    public PasswordMustDifferentException() {
        super("As senha nova deve ser diferente da senha atual. Por favor, escolha uma senha diferente.");
    }
}
