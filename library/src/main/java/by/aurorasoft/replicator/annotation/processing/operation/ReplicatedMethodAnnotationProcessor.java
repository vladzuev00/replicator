package by.aurorasoft.replicator.annotation.processing.operation;

import by.aurorasoft.replicator.annotation.processing.ReplicaAnnotationProcessor;
import by.aurorasoft.replicator.annotation.service.ReplicatedService;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Stream.concat;
import static org.checkerframework.javacutil.AnnotationUtils.containsSameByClass;

public abstract class ReplicatedMethodAnnotationProcessor extends ReplicaAnnotationProcessor<ExecutableElement> {
    private static final String INSIDE_REPLICATED_SERVICE_REQUIREMENT = "It should be inside class annotated by @"
            + ReplicatedService.class.getSimpleName();

    public ReplicatedMethodAnnotationProcessor(Class<? extends Annotation> annotation) {
        super(annotation, ExecutableElement.class);
    }

    @Override
    protected final boolean isValidPublicElement(ExecutableElement element) {
        return isValidEnclosingClass(element.getEnclosingElement())
                && isValidReturnType(element.getReturnType())
                && isValidParameters(element.getParameters());
    }

    @Override
    protected final Stream<String> getRequirementsInternal() {
        return concat(getEnclosingClassRequirement(), Stream.of(getReturnTypeRequirement(), getParametersRequirement()))
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    protected abstract boolean isValidReplicatedService(TypeMirror mirror);

    protected abstract boolean isValidReturnType(TypeMirror mirror);

    protected abstract boolean isValidParameters(List<? extends VariableElement> elements);

    protected abstract Optional<String> getReplicatedServiceRequirement();

    protected abstract Optional<String> getReturnTypeRequirement();

    protected abstract Optional<String> getParametersRequirement();

    private boolean isValidEnclosingClass(Element element) {
        return containsSameByClass(element.getAnnotationMirrors(), ReplicatedService.class)
                && isValidReplicatedService(element.asType());
    }

    private Stream<Optional<String>> getEnclosingClassRequirement() {
        return Stream.of(Optional.of(INSIDE_REPLICATED_SERVICE_REQUIREMENT), getReplicatedServiceRequirement());
    }
}