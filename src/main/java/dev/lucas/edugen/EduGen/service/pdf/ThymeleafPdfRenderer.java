package dev.lucas.edugen.EduGen.service.pdf;

import dev.lucas.edugen.EduGen.domain.WorksheetVersion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class ThymeleafPdfRenderer {

    private final TemplateEngine templateEngine;
    private final LogoImageProvider logoImageProvider;

    public String toHtml(WorksheetVersion version, String templateName,
                         boolean showAnswers, boolean showExplanations) {
        Context ctx = new Context();
        ctx.setVariable("version", version);
        ctx.setVariable("worksheet", version.getWorksheet());
        ctx.setVariable("questions", version.getQuestions());
        ctx.setVariable("logoUri", logoImageProvider.getDataUri());
        ctx.setVariable("aiDescription", version.getAiDescription());
        ctx.setVariable("showAnswers", showAnswers);
        ctx.setVariable("showExplanations", showExplanations);
        return templateEngine.process(templateName, ctx);
    }
}
