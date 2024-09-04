package by.aurorasoft.replicator.annotation.processing.processor.operation;

import by.aurorasoft.replicator.annotation.operation.ReplicatedSave;
import com.google.auto.service.AutoService;

import javax.annotation.processing.Processor;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Optional;

import static by.aurorasoft.replicator.util.PropertyUtil.isContainId;
import static java.util.Optional.empty;

@AutoService(Processor.class)
public final class ReplicatedSaveProcessor extends ReplicatedMethodAnnotationProcessor {
    private static final String RETURN_TYPE_REQUIREMENT = "Returned object should contain id's getter";

    public ReplicatedSaveProcessor() {
        super(ReplicatedSave.class);
    }

    @Override
    protected boolean isValidReplicatedService(TypeMirror mirror) {
        return true;
    }

    @Override
    protected boolean isValidReturnType(TypeMirror mirror) {
        processingEnv.getElementUtils()
                .getAllMembers()
        return isContainId(mirror);
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
