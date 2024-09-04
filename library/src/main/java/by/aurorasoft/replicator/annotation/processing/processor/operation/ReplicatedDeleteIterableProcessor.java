package by.aurorasoft.replicator.annotation.processing.processor.operation;

import by.aurorasoft.replicator.annotation.operation.ReplicatedDeleteIterable;
import com.google.auto.service.AutoService;
import org.checkerframework.javacutil.TypesUtils;

import javax.annotation.processing.Processor;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Optional;

import static by.aurorasoft.replicator.util.PropertyUtil.getFirstGenericParameterType;
import static by.aurorasoft.replicator.util.PropertyUtil.isContainId;
import static java.util.Optional.empty;

@AutoService(Processor.class)
public final class ReplicatedDeleteIterableProcessor extends ReplicatedMethodAnnotationProcessor {
    private static final String PARAMETERS_REQUIREMENT = "";

    public ReplicatedDeleteIterableProcessor() {
        super(ReplicatedDeleteIterable.class);
    }

    @Override
    protected boolean isValidReplicatedService(TypeMirror mirror) {
        return true;
    }

    @Override
    protected boolean isValidReturnType(TypeMirror mirror) {
        return true;
    }

    @Override
    protected boolean isValidParameters(List<? extends VariableElement> elements) {
        return elements.size() > 0
                && TypesUtils.getClassFromType(elements.get(0).asType()).isAssignableFrom(Iterable.class)
                && isContainId(getFirstGenericParameterType(elements.get(0).asType()));
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
