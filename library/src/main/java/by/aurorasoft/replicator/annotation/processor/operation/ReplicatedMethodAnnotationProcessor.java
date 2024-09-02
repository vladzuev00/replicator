package by.aurorasoft.replicator.annotation.processor.operation;

import by.aurorasoft.replicator.annotation.ReplicatedService;
import by.aurorasoft.replicator.annotation.processor.ReplicaAnnotationProcessor;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class ReplicatedMethodAnnotationProcessor extends ReplicaAnnotationProcessor<ExecutableElement> {
    private static final String CLASS_REQUIREMENT = "It should be inside class annotated by @"
            + ReplicatedService.class.getSimpleName();

    public ReplicatedMethodAnnotationProcessor(Class<? extends Annotation> annotation) {
        super(annotation, ExecutableElement.class);
    }

    @Override
    protected final boolean isValidPublicElement(ExecutableElement element) {
        return isInsideReplicatedService(element)
                && isValidReturnType(element.getReturnType())
                && isValidParameters(element.getParameters());
    }

    @Override
    protected final Stream<String> getRequirementsInternal() {
        return Stream.of(getClassRequirement(), getReturnTypeRequirement(), getParametersRequirement())
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    protected abstract boolean isValidReturnType(TypeMirror type);

    protected abstract boolean isValidParameters(List<? extends VariableElement> parameters);

    protected abstract Optional<String> getReturnTypeRequirement();

    protected abstract Optional<String> getParametersRequirement();

    private boolean isInsideReplicatedService(ExecutableElement method) {
        return method.getEnclosingElement().getAnnotation(ReplicatedService.class) != null;
    }

    private Optional<String> getClassRequirement() {
        return Optional.of(CLASS_REQUIREMENT);
    }
}
