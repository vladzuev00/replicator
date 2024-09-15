package by.aurorasoft.replicator.annotation.processing.processor.operation;

import by.aurorasoft.replicator.annotation.operation.ReplicatedDeleteAll;
import com.google.auto.service.AutoService;

import javax.annotation.processing.Processor;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Optional;

import static by.aurorasoft.replicator.util.AnnotationProcessUtil.isContainRepository;
import static java.util.Optional.empty;

@AutoService(Processor.class)
public final class ReplicatedDeleteAllProcessor extends ReplicatedMethodAnnotationProcessor {
    static final String REPLICATED_SERVICE_REQUIREMENT = "Service should contain repository";

    public ReplicatedDeleteAllProcessor() {
        super(ReplicatedDeleteAll.class);
    }

    @Override
    protected boolean isValidReplicatedService(TypeMirror mirror) {
        return isContainRepository(mirror, processingEnv);
    }

    @Override
    protected boolean isValidReturnType(TypeMirror mirror) {
        return true;
    }

    @Override
    protected boolean isValidParameters(List<? extends VariableElement> elements) {
        return true;
    }

    @Override
    protected Optional<String> getReplicatedServiceRequirement() {
        return Optional.of(REPLICATED_SERVICE_REQUIREMENT);
    }

    @Override
    protected Optional<String> getReturnTypeRequirement() {
        return empty();
    }

    @Override
    protected Optional<String> getParametersRequirement() {
        return empty();
    }
}
