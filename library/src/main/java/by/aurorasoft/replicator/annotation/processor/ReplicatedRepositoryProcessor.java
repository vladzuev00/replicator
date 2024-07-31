package by.aurorasoft.replicator.annotation.processor;

import by.aurorasoft.replicator.annotation.ReplicatedRepository;
import com.google.auto.service.AutoService;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.Set;

import static javax.lang.model.SourceVersion.latestSupported;
import static javax.tools.Diagnostic.Kind.ERROR;

@AutoService(Processor.class)
public final class ReplicatedRepositoryProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        annotations.stream()
                .flatMap(annotation -> env.getElementsAnnotatedWith(annotation).stream())
                .filter(element -> !isJpaRepository(element))
                .forEach(this::alertWrongAnnotating);
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(ReplicatedRepository.class.getName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return latestSupported();
    }

    private boolean isJpaRepository(Element element) {
        TypeMirror jpaRepositoryRawType = getRawType(JpaRepository.class.getName());
        return isInstanceOf(element, jpaRepositoryRawType);
    }

    private TypeMirror getRawType(String name) {
        return getTypeUtils().erasure(getType(name));
    }

    private TypeMirror getType(String name) {
        return getElementUtils()
                .getTypeElement(name)
                .asType();
    }

    private boolean isInstanceOf(Element element, TypeMirror type) {
        return getTypeUtils().isAssignable(element.asType(), type);
    }

    private void alertWrongAnnotating(Element element) {
        getMessager().printMessage(ERROR, getWrongAnnotatingMessage(), element);
    }

    private String getWrongAnnotatingMessage() {
        return "@%s can be applied only for instant of %s".formatted(
                ReplicatedRepository.class.getName(),
                JpaRepository.class.getName()
        );
    }

    private Types getTypeUtils() {
        return processingEnv.getTypeUtils();
    }

    private Elements getElementUtils() {
        return processingEnv.getElementUtils();
    }

    private Messager getMessager() {
        return processingEnv.getMessager();
    }
}
