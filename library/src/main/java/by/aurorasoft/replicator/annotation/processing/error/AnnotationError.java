package by.aurorasoft.replicator.annotation.processing.error;

import lombok.Value;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.Set;

@Value
public class AnnotationError {
    Element element;
    Class<? extends Annotation> annotation;
    Set<String> requirements;
}
