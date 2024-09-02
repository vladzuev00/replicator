package by.aurorasoft.replicator.annotation.processor.operation;

import com.google.auto.service.AutoService;
import org.checkerframework.javacutil.ElementUtils;
import org.checkerframework.javacutil.TypesUtils;

import javax.annotation.processing.Processor;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;

import static org.checkerframework.javacutil.TypesUtils.getClassFromType;

@AutoService(Processor.class)
public final class ReplicatedSaveAllProcessor extends ReplicatedMethodAnnotationProcessor {
    private static final String RETURN_TYPE_REQUIREMENT = "Returned object should contain id";

    public ReplicatedSaveAllProcessor(Class<? extends Annotation> annotation) {
        super(annotation);
    }

    @Override
    protected boolean isValidReturnType(TypeMirror mirror) {
        Class<?> type = getClassFromType(mirror);
        type.getGenericSuperclass()
        return getClassFromType(mirror) == List.class && ElementUtils.();
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
