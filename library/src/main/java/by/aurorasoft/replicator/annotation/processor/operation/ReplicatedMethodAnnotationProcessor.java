package by.aurorasoft.replicator.annotation.processor.operation;

import by.aurorasoft.replicator.annotation.processor.ReplicaAnnotationProcessor;
import by.aurorasoft.replicator.annotation.service.ReplicatedService;
import org.checkerframework.javacutil.AnnotationUtils;
import org.checkerframework.javacutil.ElementUtils;
import org.checkerframework.javacutil.TypesUtils;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Stream.concat;

public abstract class ReplicatedMethodAnnotationProcessor extends ReplicaAnnotationProcessor<ExecutableElement> {
    private static final String INSIDE_REPLICATED_SERVICE_REQUIREMENT = "It should be inside class annotated by @"
            + ReplicatedService.class.getSimpleName();

    public ReplicatedMethodAnnotationProcessor(Class<? extends Annotation> annotation) {
        super(annotation, ExecutableElement.class);
    }

    @Override
    protected final boolean isValidPublicElement(ExecutableElement element) {
//        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "" + isValidEnclosingClass(element));
//        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "" + isValidReturnType(element.getReturnType()));
//        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "" + isValidParameters(element.getParameters()));
        return isValidEnclosingClass(element)
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

    private boolean isValidEnclosingClass(ExecutableElement element) {
        TypeMirror mirror = element.getEnclosingElement().asType();
        return AnnotationUtils.containsSameByClass(element.getEnclosingElement().getAnnotationMirrors(), ReplicatedService.class) && isValidReplicatedService(mirror);
    }

    private Stream<Optional<String>> getEnclosingClassRequirement() {
        return Stream.of(Optional.of(INSIDE_REPLICATED_SERVICE_REQUIREMENT), getReplicatedServiceRequirement());
    }
}
