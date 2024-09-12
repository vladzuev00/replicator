package by.aurorasoft.replicator.it.annotationprocessing;

import by.aurorasoft.replicator.annotation.processing.processor.operation.*;
import by.aurorasoft.replicator.annotation.processing.processor.service.ReplicatedServiceProcessor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.joor.CompileOptions;
import org.joor.Reflect;
import org.joor.ReflectException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static by.aurorasoft.replicator.testutil.AssertExceptionUtil.executeExpectingException;

public final class AnnotationProcessingIT {

    @ParameterizedTest
    @MethodSource("provideSuccessCompileArguments")
    public void sourceCodeShouldBeCompiled(SuccessCompileTestArgument argument) {
        compile(argument);
    }

    @ParameterizedTest
    @MethodSource("provideFailedCompileArguments")
    public void sourceCodeShouldNotBeCompiled(FailedCompileTestArgument argument) {
        compileExpectingError(argument);
    }

    private void compile(CompileTestArgument argument) {
        CompileOptions options = createCompileOptions();
        Reflect.compile(argument.getClassName(), argument.getSourceCode(), options);
    }

    private void compileExpectingError(FailedCompileTestArgument argument) {
        executeExpectingException(() -> compile(argument), ReflectException.class, argument.getExpectedErrorMessage());
    }

    private CompileOptions createCompileOptions() {
        return new CompileOptions()
                .processors(
                        new ReplicatedServiceProcessor(),
                        new ReplicatedDeleteAllProcessor(),
                        new ReplicatedDeleteByIdProcessor(),
                        new ReplicatedDeleteByIdsProcessor(),
                        new ReplicatedDeleteIterableProcessor(),
                        new ReplicatedDeleteProcessor(),
                        new ReplicatedSaveAllProcessor(),
                        new ReplicatedSaveProcessor()
                );
    }

    @RequiredArgsConstructor
    @Getter
    private static abstract class CompileTestArgument {
        private final String className;
        private final String sourceCode;
    }

    private static final class SuccessCompileTestArgument extends CompileTestArgument {

        public SuccessCompileTestArgument(String className, String sourceCode) {
            super(className, sourceCode);
        }
    }

    @Getter
    private static final class FailedCompileTestArgument extends CompileTestArgument {
        private final String expectedErrorMessage;

        public FailedCompileTestArgument(String className, String sourceCode, String expectedErrorMessage) {
            super(className, sourceCode);
            this.expectedErrorMessage = expectedErrorMessage;
        }
    }

    private static Stream<Arguments> provideSuccessCompileArguments() {
        return Stream.of(
                Arguments.of(
                        new SuccessCompileTestArgument(
                                "by.aurorasoft.replicator.TestService",
                                """
                                        package by.aurorasoft.replicator;
                                                                        
                                        import by.aurorasoft.replicator.annotation.service.ReplicatedService;
                                        import by.aurorasoft.replicator.annotation.service.ReplicatedService.ProducerConfig;
                                        import by.aurorasoft.replicator.annotation.service.ReplicatedService.TopicConfig;
                                        import org.apache.kafka.common.serialization.LongSerializer;
                                                                        
                                        @ReplicatedService(
                                                producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
                                                topicConfig = @TopicConfig(name = "sync-dto")
                                        )
                                        public class TestService {
                                           
                                        }
                                        """
                        )
                ),
                Arguments.of(
                        new SuccessCompileTestArgument(
                                "by.aurorasoft.replicator.TestService",
                                """
                                        package by.aurorasoft.replicator;
                                                                                        
                                        import by.aurorasoft.replicator.annotation.operation.ReplicatedSave;
                                        import by.aurorasoft.replicator.annotation.service.ReplicatedService;
                                        import by.aurorasoft.replicator.annotation.service.ReplicatedService.ProducerConfig;
                                        import by.aurorasoft.replicator.annotation.service.ReplicatedService.TopicConfig;
                                        import by.aurorasoft.replicator.testcrud.TestDto;
                                        import org.apache.kafka.common.serialization.LongSerializer;
                                                                                        
                                        @ReplicatedService(
                                                producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
                                                topicConfig = @TopicConfig(name = "sync-dto")
                                        )
                                        public class TestService {
                                                                                        
                                            @ReplicatedSave
                                            public TestDto save() {
                                                throw new UnsupportedOperationException();
                                            }
                                        }"""
                        )
                )
        );
    }

    private static Stream<Arguments> provideFailedCompileArguments() {
        return Stream.of(
                Arguments.of(
                        new FailedCompileTestArgument(
                                "by.aurorasoft.replicator.TestService",
                                """
                                        package by.aurorasoft.replicator;
                                                                
                                        import by.aurorasoft.replicator.annotation.service.ReplicatedService;
                                        import by.aurorasoft.replicator.annotation.service.ReplicatedService.ProducerConfig;
                                        import by.aurorasoft.replicator.annotation.service.ReplicatedService.TopicConfig;
                                        import org.apache.kafka.common.serialization.LongSerializer;
                                                                
                                        @ReplicatedService(
                                                producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
                                                topicConfig = @TopicConfig(name = "sync-dto")
                                        )
                                        class TestService {
                                                                
                                        }""",
                                "Element annotated by @ReplicatedService should match next requirements: Element should be public"
                        )
                ),
                Arguments.of(
                        new FailedCompileTestArgument(
                                "by.aurorasoft.replicator.TestService",
                                """
                                        package by.aurorasoft.replicator;
                                                                        
                                        import by.aurorasoft.replicator.annotation.operation.ReplicatedSave;
                                        import by.aurorasoft.replicator.annotation.service.ReplicatedService;
                                        import by.aurorasoft.replicator.annotation.service.ReplicatedService.ProducerConfig;
                                        import by.aurorasoft.replicator.annotation.service.ReplicatedService.TopicConfig;
                                        import org.apache.kafka.common.serialization.LongSerializer;
                                                                        
                                        @ReplicatedService(
                                                producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
                                                topicConfig = @TopicConfig(name = "sync-dto")
                                        )
                                        public class TestService {
                                                                        
                                            @ReplicatedSave
                                            public void save() {
                                                throw new UnsupportedOperationException();
                                            }
                                        }""",
                                "gfggggggggggggggggggggggggggggggggggggggggggggg"
                        )
                )
        );
    }
}
