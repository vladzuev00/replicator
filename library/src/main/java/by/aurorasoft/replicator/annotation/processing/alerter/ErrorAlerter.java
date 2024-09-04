package by.aurorasoft.replicator.annotation.processing.alerter;

import by.aurorasoft.replicator.annotation.processing.error.AnnotationError;
import by.aurorasoft.replicator.annotation.service.ReplicatedService;

import javax.annotation.processing.ProcessingEnvironment;

import static java.lang.String.join;
import static javax.tools.Diagnostic.Kind.ERROR;

public final class ErrorAlerter {
    private static final String TEXT_TEMPLATE = "Element annotated by @%s should match next requirements: %s";
    private static final String REQUIREMENTS_DELIMITER = "\n\t";

    public void alert(ProcessingEnvironment environment, AnnotationError message) {
        String requirements = join(REQUIREMENTS_DELIMITER, message.getRequirements());
        String text = TEXT_TEMPLATE.formatted(ReplicatedService.class.getSimpleName(), requirements);
        environment.getMessager().printMessage(ERROR, text, message.getElement());
    }

    private void createText(AnnotationError message) {

    }
}
