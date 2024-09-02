package by.aurorasoft.replicator.annotation.processor.operation;

import by.aurorasoft.replicator.annotation.operation.ReplicatedSave;
import com.google.auto.service.AutoService;
import org.springframework.asm.Type;
import org.springframework.cglib.core.CodeEmitter;
import org.springframework.cglib.core.FieldTypeCustomizer;
import org.springframework.javapoet.FieldSpec;
import org.springframework.javapoet.MethodSpec;

import javax.annotation.processing.Processor;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;

@AutoService(Processor.class)
public final class ReplicatedSaveProcessor extends ReplicatedMethodAnnotationProcessor {
    private static final String RETURN_TYPE_REQUIREMENT = "Return type should contain 'id' field";

    public ReplicatedSaveProcessor() {
        super(ReplicatedSave.class);
    }

    @Override
    protected boolean isValidReturnType(TypeMirror type) {
        processingEnv.getTypeUtils().contains(type, );
        //check if return type contain field 'id'
        return false;
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
