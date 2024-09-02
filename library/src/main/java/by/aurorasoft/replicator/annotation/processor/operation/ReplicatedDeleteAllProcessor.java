package by.aurorasoft.replicator.annotation.processor.operation;

import by.aurorasoft.replicator.annotation.operation.ReplicatedDeleteAll;
import com.google.auto.service.AutoService;

import javax.annotation.processing.Processor;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Optional;

@AutoService(Processor.class)
public final class ReplicatedDeleteAllProcessor extends ReplicatedMethodAnnotationProcessor {

    public ReplicatedDeleteAllProcessor() {
        super(ReplicatedDeleteAll.class);
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
    protected Optional<String> getReturnTypeRequirement() {
        return Optional.empty();
    }

    @Override
    protected Optional<String> getParametersRequirement() {
        return Optional.empty();
    }
}
