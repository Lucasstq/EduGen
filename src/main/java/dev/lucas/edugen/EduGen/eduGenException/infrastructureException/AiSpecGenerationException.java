package dev.lucas.edugen.EduGen.eduGenException.infrastructureException;

public class AiSpecGenerationException extends RuntimeException {
    public AiSpecGenerationException() {
        super("Erro ao gerar atividade");
    }
}
