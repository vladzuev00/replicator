package by.aurorasoft.replicator.annotation.processing.processor.operation;

import by.aurorasoft.replicator.annotation.operation.ReplicatedSave;
import com.google.auto.service.AutoService;
import org.checkerframework.javacutil.TypesUtils;
import org.springframework.cglib.core.TypeUtils;

import javax.annotation.processing.Processor;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.util.List;
import java.util.Optional;

import static by.aurorasoft.replicator.util.AnnotationProcessUtil.isContainIdGetter;
import static java.util.Optional.empty;

@AutoService(Processor.class)
public final class ReplicatedSaveProcessor extends ReplicatedMethodAnnotationProcessor {
    static final String RETURN_TYPE_REQUIREMENT = "Returned object should contain id's getter";

    public ReplicatedSaveProcessor() {
        super(ReplicatedSave.class);
    }

    @Override
    protected boolean isValidReplicatedService(TypeMirror mirror) {
        return true;
    }

    @Override
    protected boolean isValidReturnType(TypeMirror mirror) {
        processingEnv.getTypeUtils().
        processingEnv.getTypeUtils().asElement().getKind()
        mirror.getKind().isPrimitive() && mirror.getKind() == TypeKind.VOID
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Return type: " + mirror);
        return mirror.isPrimitiveOrVoid() && isContainIdGetter(processingEnv.getElementUtils().getTypeElement(mirror.toString()));
    }

    @Override
    protected boolean isValidParameters(List<? extends VariableElement> elements) {
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
