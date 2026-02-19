package dev.lucas.edugen.EduGen.mapper;

import dev.lucas.edugen.EduGen.domain.Question;
import dev.lucas.edugen.EduGen.domain.QuestionOption;
import dev.lucas.edugen.EduGen.domain.WorksheetVersion;
import dev.lucas.edugen.EduGen.domain.enums.OptionLabel;
import dev.lucas.edugen.EduGen.domain.enums.QuestionType;
import dev.lucas.edugen.EduGen.dtos.request.worksheet.OptionSpec;
import dev.lucas.edugen.EduGen.dtos.request.worksheet.QuestionSpec;
import dev.lucas.edugen.EduGen.dtos.request.worksheet.WorksheetSpec;
import lombok.experimental.UtilityClass;

@UtilityClass
public class WorksheetSpecEntityMapper {

    public static void fillQuestions(WorksheetVersion version, WorksheetSpec spec) {

        if (spec.questions() == null || spec.questions().isEmpty()) {
            throw new IllegalArgumentException("Spec inválido: questions vazio");
        }

        version.getQuestions().clear();
        version.setAiDescription(spec.description());

        for (QuestionSpec qs : spec.questions()) {
            if (qs.orderNumber() == null) {
                throw new IllegalArgumentException("Spec inválido: orderNumber null");
            }
            if (qs.statement() == null || qs.statement().isBlank()) {
                throw new IllegalArgumentException("Spec inválido: statement vazio na questão " + qs.orderNumber());
            }

            Question q = new Question();
            q.setVersion(version);
            q.setOrderNumber(qs.orderNumber());

            try {
                q.setType(QuestionType.valueOf(qs.type()));
            } catch (Exception ex) {
                throw new IllegalArgumentException("Tipo de questão inválido: " + qs.type());
            }

            q.setStatement(qs.statement());
            q.setCorrectAnswer(qs.correctAnswer());
            q.setExplanation(qs.explanation());

            if (q.getType() == QuestionType.MCQ) {
                if (qs.options() == null || qs.options().size() != 4) {
                    throw new IllegalArgumentException("MCQ precisa de 4 opções (A-D) na questão " + qs.orderNumber());
                }

                for (OptionSpec os : qs.options()) {
                    QuestionOption opt = new QuestionOption();
                    opt.setQuestion(q);

                    try {
                        opt.setLabel(OptionLabel.valueOf(os.label()));
                    } catch (Exception ex) {
                        throw new IllegalArgumentException("Label de opção inválida: " + os.label());
                    }

                    opt.setText(os.text());
                    q.getOptions().add(opt);
                }
            }

            if (q.getType() == QuestionType.TRUE_FALSE && q.getCorrectAnswer() != null) {
                String a = q.getCorrectAnswer().trim().toUpperCase();
                if (!a.equals("TRUE") && !a.equals("FALSE")) {
                    throw new IllegalArgumentException("TRUE_FALSE precisa de correctAnswer TRUE/FALSE na questão " + qs.orderNumber());
                }
                q.setCorrectAnswer(a);
            }

            version.getQuestions().add(q);
        }
    }
}
