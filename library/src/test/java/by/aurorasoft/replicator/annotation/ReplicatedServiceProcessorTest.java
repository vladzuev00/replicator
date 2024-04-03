package by.aurorasoft.replicator.annotation;

import org.joor.CompileOptions;
import org.joor.Reflect;
import org.joor.ReflectException;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public final class ReplicatedServiceProcessorTest {
    private static final String WRONG_ANNOTATING_MESSAGE = "'@by.aurorasoft.replicator.annotation.ReplicatedService' "
            + "can be applied only for subclass of 'by.nhorushko.crudgeneric.v2.service.AbsServiceRUD'";

    @Test
    public void replicatedRUDServiceShouldBeCompiled() {
        compile(
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
        );
    }

    @Test
    public void replicatedCRUDServiceShouldBeCompiled() {
        compile(
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
        );
    }

    @Test
    public void replicatedReadServiceShouldNotBeCompiled() {
        compileExpectingAnnotatingError(
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
        );
    }

    @Test
    public void notCorrespondingServiceShouldNotBeCompiled() {
        compileExpectingAnnotatingError(
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
