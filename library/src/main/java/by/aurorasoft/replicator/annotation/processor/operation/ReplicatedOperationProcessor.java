package by.aurorasoft.replicator.annotation.processor.operation;

import by.aurorasoft.replicator.annotation.ReplicatedService;
import by.aurorasoft.replicator.annotation.processor.ReplicateAnnotationProcessor;

import javax.lang.model.element.ExecutableElement;
import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toUnmodifiableSet;
import static java.util.stream.Stream.concat;
import static javax.lang.model.element.Modifier.PUBLIC;

public abstract class ReplicatedOperationProcessor extends ReplicateAnnotationProcessor<ExecutableElement> {
    private static final String TEMPLATE_CLASS_REQUIREMENT = "Method should be inside class annotated by @%s";
    private static final String MODIFIER_REQUIREMENT = "Method should be public";

    public ReplicatedOperationProcessor(Class<? extends Annotation> annotation) {
        super(annotation, ExecutableElement.class);
    }

    @Override
    protected final boolean isValid(ExecutableElement method) {
        return isPublic(method) && isInsideReplicatedService(method) && isValidInternal(method);
    }

    @Override
    protected final Set<String> getRequirements() {
        return concat(
                Stream.of(getClassRequirement(), MODIFIER_REQUIREMENT),
                getRequirementsInternal()
        ).collect(toUnmodifiableSet());
    }

    protected abstract boolean isValidInternal(ExecutableElement method);

    protected abstract Stream<String> getRequirementsInternal();

    private boolean isPublic(ExecutableElement method) {
        return method.getModifiers().contains(PUBLIC);
    }

    private boolean isInsideReplicatedService(ExecutableElement method) {
        return method.getEnclosingElement().getAnnotation(ReplicatedService.class) != null;
    }

    private String getClassRequirement() {
        return TEMPLATE_CLASS_REQUIREMENT.formatted(ReplicatedService.class.getSimpleName());
    }
}
