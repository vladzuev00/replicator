package by.aurorasoft.replicator.annotation;

import org.joor.CompileOptions;
import org.joor.Reflect;
import org.joor.ReflectException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public final class ReplicatedServiceProcessorTest {
    private static final String WRONG_ANNOTATING_MESSAGE = "'@by.aurorasoft.replicator.annotation.ReplicatedService' "
            + "can be applied only for subclass one of "
            + "'[by.nhorushko.crudgeneric.v2.service.AbsServiceRUD, by.nhorushko.crudgeneric.service.RudGenericService]'";

    @ParameterizedTest
    @MethodSource("provideClassNameAndCompiledSourceCode")
    public void sourceCodeShouldBeCompiled(final String givenClassName, final String givenSourceCode) {
        compile(givenClassName, givenSourceCode);
    }

    @ParameterizedTest
    @MethodSource("provideClassNameAndNotCompiledSourceCode")
    public void sourceCodeShouldNotBeCompiled(final String givenClassName, final String givenSourceCode) {
        compileExpectingAnnotatingError(givenClassName, givenSourceCode);
    }

    private static void compile(final String className, final String sourceCode) {
        Reflect.compile(className, sourceCode, getCompileOptions());
    }

    private static void compileExpectingAnnotatingError(final String className, final String sourceCode) {
        try {
            compile(className, sourceCode);
        } catch (final ReflectException exception) {
            assertTrue(isWrongAnnotatingError(exception));
        }
    }

    private static boolean isWrongAnnotatingError(final ReflectException exception) {
        return exception.getMessage().contains(WRONG_ANNOTATING_MESSAGE);
    }

    private static CompileOptions getCompileOptions() {
        return new CompileOptions().processors(new ReplicatedServiceProcessor());
    }

    private static Stream<Arguments> provideClassNameAndCompiledSourceCode() {
        return Stream.of(
                Arguments.of(
                        "by.aurorasoft.replicator.TestV1RUDService",
                        """
                                package by.aurorasoft.replicator;
                                                                
                                import by.aurorasoft.replicator.annotation.ReplicatedService;
                                import by.aurorasoft.replicator.annotation.ReplicatedService.ProducerConfig;
                                import by.aurorasoft.replicator.annotation.ReplicatedService.TopicConfig;
                                import by.aurorasoft.replicator.base.v1.dto.TestV1Dto;
                                import by.aurorasoft.replicator.base.v1.entity.TestV1Entity;
                                import by.nhorushko.crudgeneric.mapper.AbstractMapper;
                                import by.nhorushko.crudgeneric.service.RudGenericService;
                                import org.apache.kafka.common.serialization.LongSerializer;
                                import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
                                                                
                                @SuppressWarnings("deprecation")
                                @ReplicatedService(
                                        producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
                                        topicConfig = @TopicConfig(name = "sync-dto")
                                )
                                public class TestV1RUDService extends RudGenericService<TestV1Dto, TestV1Entity, JpaRepositoryImplementation<TestV1Entity, Long>, AbstractMapper<TestV1Entity, TestV1Dto>> {
                                                                
                                    public TestV1RUDService() {
                                        super(null, null, TestV1Dto.class, TestV1Entity.class);
                                    }
                                }"""
                ),
                Arguments.of(
                        "by.aurorasoft.replicator.TestV2RUDService",
                        """
                                package by.aurorasoft.replicator;
                                                                
                                import by.aurorasoft.replicator.annotation.ReplicatedService;
                                import by.aurorasoft.replicator.annotation.ReplicatedService.ProducerConfig;
                                import by.aurorasoft.replicator.annotation.ReplicatedService.TopicConfig;
                                import by.aurorasoft.replicator.base.v2.dto.TestV2Dto;
                                import by.aurorasoft.replicator.base.v2.entity.TestV2Entity;
                                import by.nhorushko.crudgeneric.v2.mapper.AbsMapperEntityDto;
                                import by.nhorushko.crudgeneric.v2.service.AbsServiceRUD;
                                import org.apache.kafka.common.serialization.LongSerializer;
                                import org.springframework.data.jpa.repository.JpaRepository;
                                                                
                                @ReplicatedService(
                                        producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
                                        topicConfig = @TopicConfig(name = "sync-dto")
                                )
                                public class TestV2RUDService extends AbsServiceRUD<Long, TestV2Entity, TestV2Dto, AbsMapperEntityDto<TestV2Entity, TestV2Dto>, JpaRepository<TestV2Entity, Long>> {
                                                                
                                    public TestV2RUDService() {
                                        super(null, null);
                                    }
                                }"""
                ),
                Arguments.of(
                        "by.aurorasoft.replicator.TestV1CRUDService",
                        """
                                package by.aurorasoft.replicator;
                                                                
                                import by.aurorasoft.replicator.annotation.ReplicatedService;
                                import by.aurorasoft.replicator.annotation.ReplicatedService.ProducerConfig;
                                import by.aurorasoft.replicator.annotation.ReplicatedService.TopicConfig;
                                import by.aurorasoft.replicator.base.v1.dto.TestV1Dto;
                                import by.aurorasoft.replicator.base.v1.entity.TestV1Entity;
                                import by.nhorushko.crudgeneric.mapper.AbstractMapper;
                                import by.nhorushko.crudgeneric.service.CrudGenericService;
                                import org.apache.kafka.common.serialization.LongSerializer;
                                import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
                                                                
                                @SuppressWarnings("deprecation")
                                @ReplicatedService(
                                        producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
                                        topicConfig = @TopicConfig(name = "sync-dto")
                                )
                                public class TestV1CRUDService extends CrudGenericService<TestV1Dto, TestV1Entity, JpaRepositoryImplementation<TestV1Entity, Long>, AbstractMapper<TestV1Entity, TestV1Dto>> {
                                                                
                                    public TestV1CRUDService() {
                                        super(null, null, TestV1Dto.class, TestV1Entity.class);
                                    }
                                }"""
                ),
                Arguments.of(
                        "by.aurorasoft.replicator.TestV2CRUDService",
                        """
                                package by.aurorasoft.replicator;
                                                                
                                import by.aurorasoft.replicator.annotation.ReplicatedService;
                                import by.aurorasoft.replicator.annotation.ReplicatedService.ProducerConfig;
                                import by.aurorasoft.replicator.annotation.ReplicatedService.TopicConfig;
                                import by.aurorasoft.replicator.base.v2.dto.TestV2Dto;
                                import by.aurorasoft.replicator.base.v2.entity.TestV2Entity;
                                import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
                                import org.apache.kafka.common.serialization.LongSerializer;
                                import org.springframework.data.jpa.repository.JpaRepository;
                                                                
                                @ReplicatedService(
                                        producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
                                        topicConfig = @TopicConfig(name = "sync-dto")
                                )
                                public class TestV2CRUDService extends AbsServiceCRUD<Long, TestV2Entity, TestV2Dto, JpaRepository<TestV2Entity, Long>> {
                                                                
                                    public TestV2CRUDService() {
                                        super(null, null);
                                    }
                                }"""
                )
        );
    }

    private static Stream<Arguments> provideClassNameAndNotCompiledSourceCode() {
        return Stream.of(
                Arguments.of(
                        "by.aurorasoft.replicator.TestV1ReadService",
                        """
                                package by.aurorasoft.replicator;
                                                                
                                import by.aurorasoft.replicator.annotation.ReplicatedService;
                                import by.aurorasoft.replicator.annotation.ReplicatedService.ProducerConfig;
                                import by.aurorasoft.replicator.annotation.ReplicatedService.TopicConfig;
                                import by.aurorasoft.replicator.base.v1.dto.TestV1Dto;
                                import by.aurorasoft.replicator.base.v1.entity.TestV1Entity;
                                import by.nhorushko.crudgeneric.mapper.AbstractMapper;
                                import by.nhorushko.crudgeneric.service.ImmutableGenericService;
                                import org.apache.kafka.common.serialization.LongSerializer;
                                import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
                                                                
                                @SuppressWarnings("deprecation")
                                @ReplicatedService(
                                        producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
                                        topicConfig = @TopicConfig(name = "sync-person")
                                )
                                public class TestV1ReadService extends ImmutableGenericService<TestV1Dto, TestV1Entity, JpaRepositoryImplementation<TestV1Entity, Long>, AbstractMapper<TestV1Entity, TestV1Dto>> {

                                    public TestV1ReadService() {
                                        super(null, null, TestV1Dto.class, TestV1Entity.class);
                                    }
                                }"""
                ),
                Arguments.of(
                        "by.aurorasoft.replicator.TestV2ReadService",
                        """
                                package by.aurorasoft.replicator;
                                                                
                                import by.aurorasoft.replicator.annotation.ReplicatedService;
                                import by.aurorasoft.replicator.annotation.ReplicatedService.ProducerConfig;
                                import by.aurorasoft.replicator.annotation.ReplicatedService.TopicConfig;
                                import by.aurorasoft.replicator.base.v2.dto.TestV2Dto;
                                import by.aurorasoft.replicator.base.v2.entity.TestV2Entity;
                                import by.nhorushko.crudgeneric.v2.mapper.AbsMapperDto;
                                import by.nhorushko.crudgeneric.v2.service.AbsServiceR;
                                import org.apache.kafka.common.serialization.LongSerializer;
                                import org.springframework.data.jpa.repository.JpaRepository;
                                                                
                                @ReplicatedService(
                                        producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
                                        topicConfig = @TopicConfig(name = "sync-person")
                                )
                                public class TestV2ReadService extends AbsServiceR<Long, TestV2Entity, TestV2Dto, AbsMapperDto<TestV2Entity, TestV2Dto>, JpaRepository<TestV2Entity, Long>> {
                                                                
                                    public TestV2ReadService() {
                                        super(null, null);
                                    }
                                }"""
                ),
                Arguments.of(
                        "by.aurorasoft.replicator.TestService",
                        """
                                package by.aurorasoft.replicator;
                                                        
                                import by.aurorasoft.replicator.annotation.ReplicatedService;
                                import org.apache.kafka.common.serialization.LongSerializer;
                                                        
                                import static by.aurorasoft.replicator.annotation.ReplicatedService.ProducerConfig;
                                import static by.aurorasoft.replicator.annotation.ReplicatedService.TopicConfig;
                                                        
                                @ReplicatedService(
                                        producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
                                        topicConfig = @TopicConfig(name = "sync-person")
                                )
                                public final class TestService {
                                                        
                                }"""
                )
        );
    }
}
