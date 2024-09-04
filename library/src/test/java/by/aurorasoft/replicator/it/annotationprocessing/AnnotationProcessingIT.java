package by.aurorasoft.replicator.it.annotationprocessing;

import by.aurorasoft.replicator.annotation.processing.operation.*;
import by.aurorasoft.replicator.annotation.processing.service.ReplicatedServiceProcessor;
import org.joor.CompileOptions;
import org.joor.Reflect;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public final class AnnotationProcessingIT {

    @ParameterizedTest
    @MethodSource("provideClassNameAndCompiledSourceCode")
    public void sourceCodeShouldBeCompiled(String givenClassName, String givenSourceCode) {
        compile(givenClassName, givenSourceCode);
    }

    private void compile(String className, String sourceCode) {
        CompileOptions options = createCompileOptions();
        Reflect.compile(className, sourceCode, options);
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

    private static Stream<Arguments> provideClassNameAndCompiledSourceCode() {
        return Stream.of(
                Arguments.of(
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
                ),
                Arguments.of(
                        "by.aurorasoft.replicator.TestService",
                        """
                                package by.aurorasoft.replicator;
                                                                
                                import by.aurorasoft.replicator.annotation.operation.ReplicatedDelete;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService.ProducerConfig;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService.TopicConfig;
                                import by.aurorasoft.replicator.testdto.TestDto;
                                import org.apache.kafka.common.serialization.LongSerializer;
                                                                
                                @ReplicatedService(
                                        producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
                                        topicConfig = @TopicConfig(name = "sync-dto")
                                )
                                public class TestService {
                                                                
                                    @ReplicatedDelete
                                    public void delete(TestDto dto) {
                                                                
                                    }
                                }
                                """
                )
        );
    }
}
