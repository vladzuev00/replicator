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

    private static Stream<Arguments> provideClassNameAndCompiledSourceCode() {
        return Stream.of(
                Arguments.of(
                        "by.aurorasoft.replicator.TestRUDService",
                        """
                                package by.aurorasoft.replicator;
                                                        
                                import by.aurorasoft.replicator.annotation.ReplicatedService;
                                import by.aurorasoft.replicator.annotation.ReplicatedService.ProducerConfig;
                                import by.aurorasoft.replicator.annotation.ReplicatedService.TopicConfig;
                                import by.aurorasoft.replicator.base.v1.dto.TestDto;
                                import by.aurorasoft.replicator.base.v1.entity.TestEntity;
                                import by.nhorushko.crudgeneric.mapper.AbstractMapper;
                                import by.nhorushko.crudgeneric.service.RudGenericService;
                                import org.apache.kafka.common.serialization.LongSerializer;
                                import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
                                                        
                                @SuppressWarnings("deprecation")
                                @ReplicatedService(
                                        producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
                                        topicConfig = @TopicConfig(name = "sync-dto")
                                )
                                public class TestRUDService extends RudGenericService<TestDto, TestEntity, JpaRepositoryImplementation<TestEntity, Long>, AbstractMapper<TestEntity, TestDto>> {
                                   
                                    public TestRUDService(final JpaRepositoryImplementation<TestEntity, Long> repository,
                                                          final AbstractMapper<TestEntity, TestDto> mapper) {
                                        super(repository, mapper, TestDto.class, TestEntity.class);
                                    }
                                }
                                """
                ),
                Arguments.of(
                        "by.aurorasoft.replicator.TestRUDService",
                        """
                                package by.aurorasoft.replicator;
                                                        
                                import by.aurorasoft.replicator.annotation.ReplicatedService;
                                import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
                                import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
                                import by.nhorushko.crudgeneric.v2.mapper.AbsMapperEntityDto;
                                import by.nhorushko.crudgeneric.v2.service.AbsServiceRUD;
                                import org.apache.kafka.common.serialization.LongSerializer;
                                import org.springframework.data.jpa.repository.JpaRepository;
                                                        
                                import static by.aurorasoft.replicator.annotation.ReplicatedService.ProducerConfig;
                                import static by.aurorasoft.replicator.annotation.ReplicatedService.TopicConfig;
                                                        
                                @ReplicatedService(
                                        producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
                                        topicConfig = @TopicConfig(name = "sync-person")
                                )
                                public class TestRUDService extends AbsServiceRUD<Object, AbstractEntity<Object>, AbstractDto<Object>, AbsMapperEntityDto<AbstractEntity<Object>, AbstractDto<Object>>, JpaRepository<AbstractEntity<Object>, Object>> {
                                                        
                                    public TestRUDService(final AbsMapperEntityDto<AbstractEntity<Object>, AbstractDto<Object>> mapper,
                                                          final JpaRepository<AbstractEntity<Object>, Object> repository) {
                                        super(mapper, repository);
                                    }
                                }
                                """
                ),
                Arguments.of(
                        "by.aurorasoft.replicator.TestCRUDService",
                        """
                                package by.aurorasoft.replicator;
                                                        
                                import by.aurorasoft.replicator.annotation.ReplicatedService;
                                import by.aurorasoft.replicator.annotation.ReplicatedService.ProducerConfig;
                                import by.aurorasoft.replicator.annotation.ReplicatedService.TopicConfig;
                                import by.aurorasoft.replicator.base.v1.dto.TestDto;
                                import by.aurorasoft.replicator.base.v1.entity.TestEntity;
                                import by.nhorushko.crudgeneric.mapper.AbstractMapper;
                                import by.nhorushko.crudgeneric.service.CrudGenericService;
                                import org.apache.kafka.common.serialization.LongSerializer;
                                import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
                                                        
                                @SuppressWarnings("deprecation")
                                @ReplicatedService(
                                        producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
                                        topicConfig = @TopicConfig(name = "sync-dto")
                                )
                                public class TestCRUDService extends CrudGenericService<TestDto, TestEntity, JpaRepositoryImplementation<TestEntity, Long>, AbstractMapper<TestEntity, TestDto>> {
                                                        
                                    public TestCRUDService(final JpaRepositoryImplementation<TestEntity, Long> repository,
                                                           final AbstractMapper<TestEntity, TestDto> mapper) {
                                        super(repository, mapper, TestDto.class, TestEntity.class);
                                    }
                                }
                                """
                ),
                Arguments.of(
                        "by.aurorasoft.replicator.TestCRUDService",
                        """
                                package by.aurorasoft.replicator;

                                import by.aurorasoft.replicator.annotation.ReplicatedService;
                                import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
                                import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
                                import by.nhorushko.crudgeneric.v2.mapper.AbsMapperEntityDto;
                                import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
                                import org.apache.kafka.common.serialization.LongSerializer;
                                import org.springframework.data.jpa.repository.JpaRepository;

                                import static by.aurorasoft.replicator.annotation.ReplicatedService.ProducerConfig;
                                import static by.aurorasoft.replicator.annotation.ReplicatedService.TopicConfig;

                                @ReplicatedService(
                                        producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
                                        topicConfig = @TopicConfig(name = "sync-person")
                                )
                                public class TestCRUDService extends AbsServiceCRUD<Object, AbstractEntity<Object>, AbstractDto<Object>, JpaRepository<AbstractEntity<Object>, Object>> {

                                    public TestCRUDService(final AbsMapperEntityDto<AbstractEntity<Object>, AbstractDto<Object>> mapper,
                                                           final JpaRepository<AbstractEntity<Object>, Object> repository) {
                                        super(mapper, repository);
                                    }
                                }"""
                )
        );
    }

    private static Stream<Arguments> provideClassNameAndNotCompiledSourceCode() {
        return Stream.of(
                Arguments.of(
                        "by.aurorasoft.replicator.TestRService",
                        """
                                package by.aurorasoft.replicator;
                                                        
                                import by.aurorasoft.replicator.annotation.ReplicatedService;
                                import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
                                import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
                                import by.nhorushko.crudgeneric.v2.mapper.AbsMapperEntityDto;
                                import by.nhorushko.crudgeneric.v2.service.AbsServiceR;
                                import org.apache.kafka.common.serialization.LongSerializer;
                                import org.springframework.data.jpa.repository.JpaRepository;
                                                       
                                import static by.aurorasoft.replicator.annotation.ReplicatedService.ProducerConfig;
                                import static by.aurorasoft.replicator.annotation.ReplicatedService.TopicConfig;
                                                        
                                @ReplicatedService(
                                        producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
                                        topicConfig = @TopicConfig(name = "sync-person")
                                )
                                public class TestRService extends AbsServiceR<Object, AbstractEntity<Object>, AbstractDto<Object>, AbsMapperEntityDto<AbstractEntity<Object>, AbstractDto<Object>>, JpaRepository<AbstractEntity<Object>, Object>> {
                                                        
                                    public TestRService(final AbsMapperEntityDto<AbstractEntity<Object>, AbstractDto<Object>> mapper,
                                                        final JpaRepository<AbstractEntity<Object>, Object> repository) {
                                        super(mapper, repository);
                                    }
                                }
                                """
                ),
                Arguments.of(
                        "by.aurorasoft.replicator.TestRService",
                        """
                                package by.aurorasoft.replicator;
                                                                
                                import by.aurorasoft.replicator.annotation.ReplicatedService;
                                import by.aurorasoft.replicator.annotation.ReplicatedService.ProducerConfig;
                                import by.aurorasoft.replicator.annotation.ReplicatedService.TopicConfig;
                                import by.aurorasoft.replicator.base.v1.dto.TestDto;
                                import by.nhorushko.crudgeneric.service.ImmutableGenericServiceI;
                                import org.apache.kafka.common.serialization.LongSerializer;
                                                                
                                import java.util.Collection;
                                import java.util.List;
                                                                
                                import static java.util.Collections.emptyList;
                                                                
                                @SuppressWarnings("deprecation")
                                @ReplicatedService(
                                        producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
                                        topicConfig = @TopicConfig(name = "sync-dto")
                                )
                                public class TestRService implements ImmutableGenericServiceI<TestDto> {
                                                                
                                    @Override
                                    public List<TestDto> list() {
                                        return emptyList();
                                    }
                                                                
                                    @Override
                                    public TestDto getById(final Long id) {
                                        return null;
                                    }
                                                                
                                    @Override
                                    public List<TestDto> getById(final Collection<Long> collection) {
                                        return emptyList();
                                    }
                                                                
                                    @Override
                                    public boolean existById(final Long id) {
                                        return false;
                                    }
                                                                
                                    @Override
                                    public long count() {
                                        return 0;
                                    }
                                }
                                """
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
}
