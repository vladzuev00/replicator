package by.aurorasoft.replicator.annotation.processing.processor.operation;

import by.aurorasoft.replicator.annotation.operation.ReplicatedDeleteIterable;
import com.google.auto.service.AutoService;

import javax.annotation.processing.Processor;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Optional;

import static by.aurorasoft.replicator.util.annotationprocessing.ElementUtil.getFirstTypeArgument;
import static by.aurorasoft.replicator.util.annotationprocessing.ElementUtil.isIterable;
import static by.aurorasoft.replicator.util.annotationprocessing.TypeMirrorUtil.isContainIdGetter;
import static java.util.Optional.empty;

@AutoService(Processor.class)
public final class ReplicatedDeleteIterableProcessor extends ReplicatedMethodAnnotationProcessor {
    static final String PARAMETERS_REQUIREMENT = "Method should have at least one parameter as Iterable. "
            + "Elements should contain id's getter";

    public ReplicatedDeleteIterableProcessor() {
        super(ReplicatedDeleteIterable.class);
    }

    @Override
    protected boolean isValidReplicatedService(TypeElement element) {
        return true;
    }

    @Override
    protected boolean isValidReturnType(TypeMirror mirror) {
        return true;
    }

    @Override
    protected boolean isValidParameters(List<? extends VariableElement> elements) {
        return !elements.isEmpty()
                && isIterable(elements.get(0), getElementUtil(), getTypeUtil())
                && isContainIdGetter(getFirstTypeArgument(elements.get(0)), getElementUtil(), getTypeUtil());
    }

    @Override
    protected Optional<String> getReplicatedServiceRequirement() {
        return empty();
    }

    @Override
    protected Optional<String> getReturnTypeRequirement() {
        return empty();
    }

    @Override
    protected Optional<String> getParametersRequirement() {
        return Optional.of(PARAMETERS_REQUIREMENT);
    }
}
