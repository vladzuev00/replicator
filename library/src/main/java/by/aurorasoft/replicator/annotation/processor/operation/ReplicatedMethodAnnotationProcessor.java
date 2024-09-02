package by.aurorasoft.replicator.annotation.processor.operation;

import lombok.RequiredArgsConstructor;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.Set;

import static javax.lang.model.SourceVersion.latestSupported;

@RequiredArgsConstructor
public class ReplicatedMethodAnnotationProcessor extends AbstractProcessor {
    private static final String TEMPLATE_CLASS_REQUIREMENT = "Method should be inside class annotated by @%s";
    private static final String MODIFIER_REQUIREMENT = "Method should be public";

    private final Class<? extends Annotation> annotation;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return false;
    }

    @Override
    public final Set<String> getSupportedAnnotationTypes() {
        return Set.of(annotation.getName());
    }

    @Override
    public final SourceVersion getSupportedSourceVersion() {
        return latestSupported();
    }

//    public ReplicatedOperationProcessor(Class<? extends Annotation> annotation) {
//        super(annotation, ExecutableElement.class);
//    }

//    @Override
//    protected final boolean isValid(ExecutableElement method) {
//        return isPublic(method) && isInsideReplicatedService(method) && isValidParameters(method.getParameters());
//    }
//
//    @Override
//    protected final Set<String> getRequirements() {
//        return concat(
//                Stream.of(getClassRequirement(), MODIFIER_REQUIREMENT),
//                getRequirementsInternal()
//        ).collect(toUnmodifiableSet());
//    }
//
//    protected abstract boolean isValidParameters(List<? extends VariableElement> parameters);
//
//    protected abstract Stream<String> getRequirementsInternal();
//
//    private boolean isPublic(ExecutableElement method) {
//        return method.getModifiers().contains(PUBLIC);
//    }
//
//    private boolean isInsideReplicatedService(ExecutableElement method) {
//        return method.getEnclosingElement().getAnnotation(ReplicatedService.class) != null;
//    }
//
//    private String getClassRequirement() {
//        return TEMPLATE_CLASS_REQUIREMENT.formatted(ReplicatedService.class.getSimpleName());
//    }
}
