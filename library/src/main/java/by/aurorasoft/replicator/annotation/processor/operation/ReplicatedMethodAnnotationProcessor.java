package by.aurorasoft.replicator.annotation.processor.operation;

import by.aurorasoft.replicator.annotation.processor.ReplicaAnnotationProcessor;

import javax.lang.model.element.ExecutableElement;
import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Stream;

public abstract class ReplicatedMethodAnnotationProcessor extends ReplicaAnnotationProcessor<ExecutableElement> {
    private static final String TEMPLATE_CLASS_REQUIREMENT = "It should be inside class annotated by @%s";

    public ReplicatedMethodAnnotationProcessor(Class<? extends Annotation> annotation) {
        super(annotation, ExecutableElement.class);
    }

    @Override
    protected boolean isValidPublicElement(ExecutableElement element) {
        return false;
    }

    @Override
    protected Stream<String> getRequirementsInternal() {
        return null;
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
