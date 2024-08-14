package by.aurorasoft.replicator.annotation.processor.operation;

import by.aurorasoft.replicator.annotation.ReplicatedService;
import by.aurorasoft.replicator.annotation.processor.ReplicateAnnotationProcessor;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toUnmodifiableSet;
import static java.util.stream.Stream.concat;

public abstract class ReplicatedOperationProcessor extends ReplicateAnnotationProcessor {
    private static final String TEMPLATE_CLASS_REQUIREMENT = "Should be inside class annotated by @%s";

    public ReplicatedOperationProcessor(Class<Annotation> annotation) {
        super(annotation);
    }

    @Override
    protected final boolean isValid(Element element) {
        return isInsideReplicatedService(element) && isValidInternal(element);
    }

    @Override
    protected final Set<String> getRequirements() {
        return concat(Stream.of(getClassRequirement()), getRequirementsInternal()).collect(toUnmodifiableSet());
    }

    protected abstract boolean isValidInternal(Element element);

    protected abstract Stream<String> getRequirementsInternal();

    private boolean isInsideReplicatedService(Element element) {
        return element.getEnclosingElement().getAnnotation(ReplicatedService.class) != null;
    }

    private String getClassRequirement() {
        return TEMPLATE_CLASS_REQUIREMENT.formatted(ReplicatedService.class.getSimpleName());
    }
}
