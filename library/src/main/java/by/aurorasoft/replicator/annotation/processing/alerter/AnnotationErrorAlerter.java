package by.aurorasoft.replicator.annotation.processing.alerter;

import by.aurorasoft.replicator.annotation.processing.error.AnnotationError;
import by.aurorasoft.replicator.annotation.processing.factory.AnnotationErrorTextFactory;

import javax.annotation.processing.ProcessingEnvironment;

import static javax.tools.Diagnostic.Kind.ERROR;

public final class AnnotationErrorAlerter {
    private final AnnotationErrorTextFactory textFactory = new AnnotationErrorTextFactory();

    public void alert(ProcessingEnvironment environment, AnnotationError error) {
        String text = textFactory.create(error);
        environment.getMessager().printMessage(ERROR, text, error.getElement());
    }
}
