package by.aurorasoft.replicator.annotation.processor.service;

import by.aurorasoft.replicator.annotation.processor.ReplicaAnnotationProcessor;
import by.aurorasoft.replicator.annotation.service.ReplicatedService;
import com.google.auto.service.AutoService;

import javax.annotation.processing.Processor;
import javax.lang.model.element.TypeElement;
import java.util.stream.Stream;

import static java.util.stream.Stream.empty;

@AutoService(Processor.class)
public final class ReplicatedServiceProcessor extends ReplicaAnnotationProcessor<TypeElement> {

    public ReplicatedServiceProcessor() {
        super(ReplicatedService.class, TypeElement.class);
    }

    @Override
    protected boolean isValidPublicElement(TypeElement element) {
        return true;
    }

    @Override
    protected Stream<String> getRequirementsInternal() {
        return empty();
    }
}
