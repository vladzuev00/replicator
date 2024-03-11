package by.aurorasoft.replicator.annotation;

import org.joor.CompileOptions;
import org.joor.Reflect;
import org.joor.ReflectException;
import org.junit.Test;

public final class ReplicatedServiceProcessorTest {

    @Test
    public void replicatedRUDServiceShouldBeCompiled() {
        compile(
                "by.aurorasoft.replicator.EntityRUDService",
                """
                        package by.aurorasoft.replicator;
                                                
                        import by.aurorasoft.replicator.annotation.ReplicatedService;
                        import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
                        import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
                        import by.nhorushko.crudgeneric.v2.mapper.AbsMapperEntityDto;
                        import by.nhorushko.crudgeneric.v2.service.AbsServiceRUD;
                        import org.apache.kafka.common.serialization.LongSerializer;
                        import org.springframework.data.jpa.repository.JpaRepository;
                                                
                        @ReplicatedService(
                                producerConfig = @ReplicatedService.ProducerConfig(idSerializer = LongSerializer.class),
                                topicConfig = @ReplicatedService.TopicConfig(name = "sync-person")
                        )
                        public class EntityRUDService extends AbsServiceRUD<Object, AbstractEntity<Object>, AbstractDto<Object>, AbsMapperEntityDto<AbstractEntity<Object>, AbstractDto<Object>>, JpaRepository<AbstractEntity<Object>, Object>> {
                                                
                            public EntityRUDService(final AbsMapperEntityDto<AbstractEntity<Object>, AbstractDto<Object>> mapper,
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
                "by.aurorasoft.replicator.EntityCRUDService",
                """
                        package by.aurorasoft.replicator;

                        import by.aurorasoft.replicator.annotation.ReplicatedService;
                        import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
                        import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
                        import by.nhorushko.crudgeneric.v2.mapper.AbsMapperEntityDto;
                        import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
                        import org.apache.kafka.common.serialization.LongSerializer;
                        import org.springframework.data.jpa.repository.JpaRepository;

                        @ReplicatedService(
                                producerConfig = @ReplicatedService.ProducerConfig(idSerializer = LongSerializer.class),
                                topicConfig = @ReplicatedService.TopicConfig(name = "sync-person")
                        )
                        public class EntityCRUDService extends AbsServiceCRUD<Object, AbstractEntity<Object>, AbstractDto<Object>, JpaRepository<AbstractEntity<Object>, Object>> {

                            public EntityCRUDService(final AbsMapperEntityDto<AbstractEntity<Object>, AbstractDto<Object>> mapper,
                                                     final JpaRepository<AbstractEntity<Object>, Object> repository) {
                                super(mapper, repository);
                            }
                        }"""
        );
    }

    @Test(expected = ReflectException.class)
    public void replicatedReadServiceShouldNotBeCompiled() {
        compile(
                "by.aurorasoft.replicator.EntityRService",
                """
                        package by.aurorasoft.replicator;
                                                
                        import by.aurorasoft.replicator.annotation.ReplicatedService;
                        import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
                        import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
                        import by.nhorushko.crudgeneric.v2.mapper.AbsMapperEntityDto;
                        import by.nhorushko.crudgeneric.v2.service.AbsServiceR;
                        import org.apache.kafka.common.serialization.LongSerializer;
                        import org.springframework.data.jpa.repository.JpaRepository;
                                                
                        @ReplicatedService(
                                producerConfig = @ReplicatedService.ProducerConfig(idSerializer = LongSerializer.class),
                                topicConfig = @ReplicatedService.TopicConfig(name = "sync-person")
                        )
                        public class EntityRService extends AbsServiceR<Object, AbstractEntity<Object>, AbstractDto<Object>, AbsMapperEntityDto<AbstractEntity<Object>, AbstractDto<Object>>, JpaRepository<AbstractEntity<Object>, Object>> {
                                                
                            public EntityRService(final AbsMapperEntityDto<AbstractEntity<Object>, AbstractDto<Object>> mapper,
                                                  final JpaRepository<AbstractEntity<Object>, Object> repository) {
                                super(mapper, repository);
                            }
                        }
                        """
        );
    }

    @Test(expected = ReflectException.class)
    public void notCorrespondingServiceShouldNotBeCompiled() {
        compile(
                "by.aurorasoft.replicator.Service",
                """
                        package by.aurorasoft.replicator;
                                                
                        import by.aurorasoft.replicator.annotation.ReplicatedService;
                        import org.apache.kafka.common.serialization.LongSerializer;
                                                
                        @ReplicatedService(
                                producerConfig = @ReplicatedService.ProducerConfig(idSerializer = LongSerializer.class),
                                topicConfig = @ReplicatedService.TopicConfig(name = "sync-person")
                        )
                        public final class Service {
                                                
                        }"""
        );
    }

    private static void compile(final String className, final String sourceCode) {
        Reflect.compile(className, sourceCode, getCompileOptions());
    }

    private static CompileOptions getCompileOptions() {
        return new CompileOptions().processors(new ReplicatedServiceProcessor());
    }
}
