package by.aurorasoft.replicator.annotation.processing.operation;

import by.aurorasoft.replicator.annotation.operation.ReplicatedSaveAll;
import com.google.auto.service.AutoService;

import javax.annotation.processing.Processor;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Optional;

import static by.aurorasoft.replicator.util.PropertyUtil.*;
import static java.util.Optional.empty;

@AutoService(Processor.class)
public final class ReplicatedSaveAllProcessor extends ReplicatedMethodAnnotationProcessor {
    private static final String RETURN_TYPE_REQUIREMENT = "Returned list's objects should contain id";

    public ReplicatedSaveAllProcessor() {
        super(ReplicatedSaveAll.class);
    }

    @Override
    protected boolean isValidReplicatedService(TypeMirror mirror) {
        return true;
    }

    @Override
    protected boolean isValidReturnType(TypeMirror mirror) {
        return isList(mirror) && isContainId(getFirstGenericParameterType(mirror));
    }

    @Override
    protected boolean isValidParameters(List<? extends VariableElement> parameters) {
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