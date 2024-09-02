package by.aurorasoft.replicator.annotation.processor.operation;

import by.aurorasoft.replicator.annotation.operation.ReplicatedSaveAll;
import com.google.auto.service.AutoService;

import javax.annotation.processing.Processor;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Optional;

import static by.aurorasoft.replicator.util.PropertyUtil.*;

@AutoService(Processor.class)
public final class ReplicatedSaveAllProcessor extends ReplicatedMethodAnnotationProcessor {
    private static final String RETURN_TYPE_REQUIREMENT = "Returned objects should contain id";

    public ReplicatedSaveAllProcessor() {
        super(ReplicatedSaveAll.class);
    }

    @Override
    protected boolean isValidReturnType(TypeMirror mirror) {
        return isList(mirror) && isContainId(getFirstGenericType(mirror));
    }

    @Override
    protected boolean isValidParameters(List<? extends VariableElement> parameters) {
        return false;
    }

    @Override
    protected Optional<String> getReturnTypeRequirement() {
        return Optional.of(RETURN_TYPE_REQUIREMENT);
    }

    @Override
    protected Optional<String> getParametersRequirement() {
        return Optional.empty();
    }
}
