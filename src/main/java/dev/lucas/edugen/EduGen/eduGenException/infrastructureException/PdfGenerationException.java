package dev.lucas.edugen.EduGen.eduGenException.infrastructureException;

public class PdfGenerationException extends RuntimeException {
    public PdfGenerationException() {
        super("Falha ao gerar atividade");
    }
}
