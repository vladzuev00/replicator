package by.aurorasoft.replicator.annotation.processor.operation;

import by.aurorasoft.replicator.annotation.operation.ReplicatedSave;
import com.google.auto.service.AutoService;

import javax.annotation.processing.Processor;
import javax.lang.model.element.ExecutableElement;
import java.util.stream.Stream;

//@AutoService(Processor.class)
//public final class ReplicatedSaveProcessor extends ReplicatedMethodAnnotationProcessor {
//
//    public ReplicatedSaveProcessor() {
//        super(ReplicatedSave.class);
//    }
//
//    @Override
//    protected boolean isValidInternal(ExecutableElement method) {
////        method.getReturnType().
////        return method.getParameters().size() == 1 && method.getReturnType();
//        return false;
//    }
//
//    @Override
//    protected Stream<String> getRequirementsInternal() {
//        return null;
//    }
//}
