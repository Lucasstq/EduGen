package dev.lucas.edugen.EduGen.service.worksheet;

import dev.lucas.edugen.EduGen.domain.Worksheet;
import dev.lucas.edugen.EduGen.domain.WorksheetVersion;
import dev.lucas.edugen.EduGen.eduGenException.infrastructureException.AiSpecGenerationException;
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
                - description: %s
                
                O JSON deve ter este formato:
                {
                  "description": "texto" ,                       // use "" quando não houver texto introdutório
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
                - "description" deve ser um texto introdutório e não pode conter HTML ou quebras de linha desnecessárias; use "" quando nenhuma descrição for necessária.
                - "questions" deve ter exatamente %d itens.
                - orderNumber sequencial começando em 1.
                - Se type=MCQ: options deve ter 4 itens (A,B,C,D) e correctAnswer deve ser "A"|"B"|"C"|"D" (quando includeAnswers=true).
                - Se type=TRUE_FALSE: correctAnswer deve ser "TRUE" ou "FALSE" (quando includeAnswers=true).
                - Se type=FILL_BLANK: correctAnswer é o texto esperado (quando includeAnswers=true).
                - Se type=VARIABLE: as questões devem ser geradas com varios tipos de perguntas (MCQ, OPEN, TRUE_FALSE, FILL_BLANK) e o campo correctAnswer deve seguir as regras do tipo específico da questão (quando includeAnswers=true).
                - Se includeAnswers=false, NÃO inclua o campo correctAnswer.
                - Se includeExplanations=false, NÃO inclua o campo explanation.
                - Não invente campos além dos listados.
                - NUNCA gere questões que façam referência a figuras, imagens, gráficos, tabelas ou qualquer elemento visual ("observe a figura", "veja o gráfico", "na imagem abaixo", etc). O sistema não suporta imagens nas questões. Todas as questões devem ser autocontidas em texto.
                - Todas as questões devem ser do tipo: %s
                - Se o campo description tiver um texto relevante, use-o para criar as questões. Se for vazio ou não tiver informações úteis, ignore-o e envie ""; caso especial: se pedir um texto antes das questões, gere um parágrafo introdutório usando o topic como base. 
                - So use isso se o campo description não for vazio. Se o tipo de questão for OPEN, TRUE_FALSE ou FILL_BLANK, use o description para criar um enunciado mais rico e contextualizado de forma que de para responder as questões com base nele. 
                - So use isso se o campo description não for vazio. Se o tipo for MCQ, use o description para criar um cenário ou contexto para a questão, mas mantenha o enunciado da questão claro e objetivo.
                - So use isso se o campo description não for vazio. Se a description tiver apenas pedidos, intruções utilize para fazer as questões, mas não inclua essas instruções no enunciado. Exemplo: "Faça questões que realmente exijam um bom raciocinio para os alunos" ou que se pareçam com isso, não inclua no enunciado.
                """.formatted(
                worksheet.getQuestionCount(),
                worksheet.getSubject().name(),
                worksheet.getGrade().name(),
                worksheet.getTopic(),
                worksheet.getDifficulty().name(),
                version.getSeed(),
                version.isIncludeAnswers(),
                version.isIncludeExplanations(),
                worksheet.getDescription(),
                worksheet.getQuestionCount(),
                worksheet.getQuestionCount(),
                worksheet.getQuestionType().name()
        );


        try {
            return chatClient.prompt()
                    .system(system)
                    .user(user)
                    .call()
                    .content();

        } catch (Exception e) {
            throw new AiSpecGenerationException();
        }

    }
}
