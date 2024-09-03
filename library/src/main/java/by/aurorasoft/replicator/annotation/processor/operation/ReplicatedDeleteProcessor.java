package by.aurorasoft.replicator.annotation.processor.operation;

import by.aurorasoft.replicator.annotation.operation.ReplicatedDelete;
import com.google.auto.service.AutoService;
import org.checkerframework.javacutil.ElementUtils;
import org.checkerframework.javacutil.TypesUtils;

import javax.annotation.processing.Processor;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static by.aurorasoft.replicator.util.PropertyUtil.isContainId;
import static java.util.Optional.empty;

@AutoService(Processor.class)
public final class ReplicatedDeleteProcessor extends ReplicatedMethodAnnotationProcessor {
    private static final String PARAMETERS_REQUIREMENT = "Method should contain at least one parameter and "
            + "this parameter should contain id";

    public ReplicatedDeleteProcessor() {
        super(ReplicatedDelete.class);
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
//        return elements.size() > 0 && isContainId(elements.get(0));
        return elements.size() > 0 && !ElementUtils.findFieldsInTypeOrSuperType(elements.get(0).asType(), Collections.singleton("id")).isEmpty();
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
