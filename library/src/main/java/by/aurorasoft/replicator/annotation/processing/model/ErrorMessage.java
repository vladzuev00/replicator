package by.aurorasoft.replicator.annotation.processing.model;

import lombok.Value;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.Set;

@Value
public class ErrorMessage {
    Element element;
    Class<? extends Annotation> annotation;
    Set<String> requirements;
}
