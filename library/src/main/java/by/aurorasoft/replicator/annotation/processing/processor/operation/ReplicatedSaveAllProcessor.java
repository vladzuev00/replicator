package by.aurorasoft.replicator.annotation.processing.processor.operation;

import by.aurorasoft.replicator.annotation.operation.ReplicatedSaveAll;
import com.google.auto.service.AutoService;

import javax.annotation.processing.Processor;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.util.List;
import java.util.Optional;

import static by.aurorasoft.replicator.util.annotationprocessing.AnnotationProcessUtil.isList;
import static by.aurorasoft.replicator.util.annotationprocessing.TypeMirrorUtil.getFirstTypeArgument;
import static by.aurorasoft.replicator.util.annotationprocessing.TypeMirrorUtil.isContainIdGetter;
import static java.util.Optional.empty;

@AutoService(Processor.class)
public final class ReplicatedSaveAllProcessor extends ReplicatedMethodAnnotationProcessor {
    static final String RETURN_TYPE_REQUIREMENT = "Returned list's objects should contain id's getter";

    public ReplicatedSaveAllProcessor() {
        super(ReplicatedSaveAll.class);
    }

    @Override
    protected boolean isValidReplicatedService(TypeElement element) {
        return true;
    }

    @Override
    protected boolean isValidReturnType(TypeElement element) {
        return isList(element, processingEnv) && isContainIdGetter(getFirstTypeArgument(element), processingEnv);
    }

    @Override
    protected boolean isValidParameters(List<? extends VariableElement> elements) {
        return true;
    }

    @Override
    protected Optional<String> getReplicatedServiceRequirement() {
        return empty();
    }

    @Override
    protected Optional<String> getReturnTypeRequirement() {
        return Optional.of(RETURN_TYPE_REQUIREMENT);
    }

    @Override
    protected Optional<String> getParametersRequirement() {
        return empty();
    }
}
