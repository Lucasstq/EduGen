package dev.lucas.edugen.EduGen.service.worksheet;

import dev.lucas.edugen.EduGen.domain.Worksheet;
import dev.lucas.edugen.EduGen.domain.WorksheetVersion;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiWorksheetService {

    private final ChatClient chatClient;

    public String generateSpec(Worksheet worksheet, WorksheetVersion version) {

        String system = """
                Você gera atividades escolares para alunos do ensino fundamental.
                Responda SOMENTE com JSON válido (sem markdown, sem texto fora do JSON).
                Se não tiver certeza, ainda assim retorne um JSON seguindo o schema.
                """;

        String user = """
                Gere uma atividade em JSON com exatamente %d questões.
                
                Contexto:
                - subject: %s
                - grade: %s
                - topic: %s
                - difficulty: %s
                - seed: %d
                - includeAnswers: %s
                - includeExplanations: %s
                
                O JSON deve ter este formato:
                {
                  "questions": [
                    {
                      "orderNumber": 1..%d,
                      "type": "MCQ" | "OPEN" | "TRUE_FALSE" | "FILL_BLANK",
                      "statement": "texto",
                      "options": [{"label":"A"|"B"|"C"|"D","text":"texto"}],   // somente quando type="MCQ"
                      "correctAnswer": "A"|"B"|"C"|"D" ou "texto",             // somente se includeAnswers=true
                      "explanation": "texto"                                   // somente se includeExplanations=true
                    }
                  ]
                }
                
                Regras:
                - "questions" deve ter exatamente %d itens.
                - orderNumber sequencial começando em 1.
                - Se type=MCQ: options deve ter 4 itens (A,B,C,D) e correctAnswer deve ser "A"|"B"|"C"|"D" (quando includeAnswers=true).
                - Se type=TRUE_FALSE: correctAnswer deve ser "TRUE" ou "FALSE" (quando includeAnswers=true).
                - Se type=FILL_BLANK: correctAnswer é o texto esperado (quando includeAnswers=true).
                - Se includeAnswers=false, NÃO inclua o campo correctAnswer.
                - Se includeExplanations=false, NÃO inclua o campo explanation.
                - Não invente campos além dos listados.
                """.formatted(
                worksheet.getQuestionCount(),
                worksheet.getSubject().name(),
                worksheet.getGrade().name(),
                worksheet.getTopic(),
                worksheet.getDifficulty().name(),
                version.getSeed(),
                version.isIncludeAnswers(),
                version.isIncludeExplanations(),
                worksheet.getQuestionCount(),
                worksheet.getQuestionCount()
        );

        return chatClient.prompt()
                .system(system)
                .user(user)
                .call()
                .content();

    }
}
