package by.aurorasoft.replicator.annotation.processor.operation;

import by.aurorasoft.replicator.annotation.operation.ReplicatedSave;
import com.google.auto.service.AutoService;

import javax.annotation.processing.Processor;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.util.List;
import java.util.Optional;

import static by.aurorasoft.replicator.util.PropertyUtil.isContainId;
import static java.util.Optional.empty;

@AutoService(Processor.class)
public final class ReplicatedSaveProcessor extends ReplicatedMethodAnnotationProcessor {
    private static final String RETURN_TYPE_REQUIREMENT = "Returned object should contain id";

    public ReplicatedSaveProcessor() {
        super(ReplicatedSave.class);
    }

    @Override
    protected boolean isValidReturnType(TypeElement type) {
        return isContainId(type);
    }

    @Override
    protected boolean isValidParameters(List<? extends VariableElement> parameters) {
        return true;
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
