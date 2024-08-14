package by.aurorasoft.replicator.annotation.processor.operation;

import by.aurorasoft.replicator.annotation.processor.ReplicateAnnotationProcessor;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;

public abstract class ReplicatedOperationProcessor extends ReplicateAnnotationProcessor {

    public ReplicatedOperationProcessor(Class<Annotation> annotation, String errorMessage) {
        super(annotation, errorMessage);
    }

    @Override
    protected boolean isValid(Element element) {
        return false;
    }
}
