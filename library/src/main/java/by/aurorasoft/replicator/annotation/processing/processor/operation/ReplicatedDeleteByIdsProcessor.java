package by.aurorasoft.replicator.annotation.processing.processor.operation;

import by.aurorasoft.replicator.annotation.operation.ReplicatedDeleteByIds;
import com.google.auto.service.AutoService;

import javax.annotation.processing.Processor;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Optional;

import static by.aurorasoft.replicator.util.AnnotationProcessUtil.isIterable;
import static java.util.Optional.empty;

@AutoService(Processor.class)
public final class ReplicatedDeleteByIdsProcessor extends ReplicatedMethodAnnotationProcessor {
    static final String PARAMETERS_REQUIREMENT = "Method should have at least one parameter as Iterable";

    public ReplicatedDeleteByIdsProcessor() {
        super(ReplicatedDeleteByIds.class);
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
        return elements.size() > 0 && isIterable(elements.get(0), processingEnv);
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
