package by.aurorasoft.replicator.it.annotationprocessing;

import by.aurorasoft.replicator.annotation.processing.processor.operation.*;
import by.aurorasoft.replicator.annotation.processing.processor.service.ReplicatedServiceProcessor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.joor.CompileOptions;
import org.joor.Reflect;
import org.joor.ReflectException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static by.aurorasoft.replicator.testutil.AssertExceptionUtil.assertException;

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
        Reflect.compile(argument.getClassName(), argument.getSourceCode(), getCompileOptions());
    }

    private void compileExpectingError(FailedCompileTestArgument argument) {
        assertException(() -> compile(argument), ReflectException.class, argument.getExpectedErrorMessage());
    }

    private CompileOptions getCompileOptions() {
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

    private static Stream<SuccessCompileTestArgument> provideSuccessCompileArguments() {
        return Stream.of(
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
                                   
                                }"""
                ),
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
                ),
                new SuccessCompileTestArgument(
                        "by.aurorasoft.replicator.TestService",
                        """
                                package by.aurorasoft.replicator;
                                                                
                                import by.aurorasoft.replicator.annotation.operation.ReplicatedSaveAll;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService.ProducerConfig;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService.TopicConfig;
                                import by.aurorasoft.replicator.testcrud.TestDto;
                                import org.apache.kafka.common.serialization.LongSerializer;
                                                                
                                import java.util.List;
                                                                
                                @ReplicatedService(
                                        producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
                                        topicConfig = @TopicConfig(name = "sync-dto")
                                )
                                public class TestService {
                                                                
                                    @ReplicatedSaveAll
                                    public List<TestDto> saveAll() {
                                        throw new UnsupportedOperationException();
                                    }
                                }"""
                ),
                new SuccessCompileTestArgument(
                        "by.aurorasoft.replicator.TestService",
                        """
                                package by.aurorasoft.replicator;
                                                                        
                                import by.aurorasoft.replicator.annotation.operation.ReplicatedSaveAll;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService.ProducerConfig;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService.TopicConfig;
                                import by.aurorasoft.replicator.testcrud.TestDto;
                                import org.apache.kafka.common.serialization.LongSerializer;
                                                                        
                                import java.util.ArrayList;
                                import java.util.List;
                                                                        
                                @ReplicatedService(
                                        producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
                                        topicConfig = @TopicConfig(name = "sync-dto")
                                )
                                public class TestService {
                                                                        
                                    @ReplicatedSaveAll
                                    public ArrayList<TestDto> saveAll() {
                                        throw new UnsupportedOperationException();
                                    }
                                }"""
                ),
                new SuccessCompileTestArgument(
                        "by.aurorasoft.replicator.TestService",
                        """
                                package by.aurorasoft.replicator;
                                                                                
                                        import by.aurorasoft.replicator.annotation.operation.ReplicatedDelete;
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
                                                                                
                                            @ReplicatedDelete
                                            public void delete(TestDto dto) {
                                                throw new UnsupportedOperationException();
                                            }
                                        }"""
                ),
                new SuccessCompileTestArgument(
                        "by.aurorasoft.replicator.TestService",
                        """
                                package by.aurorasoft.replicator;
                                                                
                                import by.aurorasoft.replicator.annotation.operation.ReplicatedDeleteIterable;
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
                                                                
                                    @ReplicatedDeleteIterable
                                    public void delete(Iterable<TestDto> dtos) {
                                        throw new UnsupportedOperationException();
                                    }
                                }
                                """
                ),
                new SuccessCompileTestArgument(
                        "by.aurorasoft.replicator.TestService",
                        """
                                package by.aurorasoft.replicator;
                                                                        
                                import by.aurorasoft.replicator.annotation.operation.ReplicatedDeleteIterable;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService.ProducerConfig;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService.TopicConfig;
                                import by.aurorasoft.replicator.testcrud.TestDto;
                                import org.apache.kafka.common.serialization.LongSerializer;
                                                                        
                                import java.util.List;
                                                                        
                                @ReplicatedService(
                                        producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
                                        topicConfig = @TopicConfig(name = "sync-dto")
                                )
                                public class TestService {
                                                                        
                                    @ReplicatedDeleteIterable
                                    public void delete(List<TestDto> dtos) {
                                        throw new UnsupportedOperationException();
                                    }
                                }
                                """
                ),
                new SuccessCompileTestArgument(
                        "by.aurorasoft.replicator.TestService",
                        """
                                package by.aurorasoft.replicator;
                                                                        
                                import by.aurorasoft.replicator.annotation.operation.ReplicatedDeleteByIds;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService.ProducerConfig;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService.TopicConfig;
                                import org.apache.kafka.common.serialization.LongSerializer;
                                                                        
                                @ReplicatedService(
                                        producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
                                        topicConfig = @TopicConfig(name = "sync-dto")
                                )
                                public class TestService {
                                                                        
                                    @ReplicatedDeleteByIds
                                    public void delete(Iterable<Long> ids) {
                                        throw new UnsupportedOperationException();
                                    }
                                }
                                """
                ),
                new SuccessCompileTestArgument(
                        "by.aurorasoft.replicator.TestService",
                        """
                                package by.aurorasoft.replicator;
                                                                
                                import by.aurorasoft.replicator.annotation.operation.ReplicatedDeleteByIds;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService.ProducerConfig;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService.TopicConfig;
                                import org.apache.kafka.common.serialization.LongSerializer;
                                                                
                                import java.util.List;
                                                                
                                @ReplicatedService(
                                        producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
                                        topicConfig = @TopicConfig(name = "sync-dto")
                                )
                                public class TestService {
                                                                
                                    @ReplicatedDeleteByIds
                                    public void delete(List<Long> ids) {
                                        throw new UnsupportedOperationException();
                                    }
                                }
                                """
                ),
                new SuccessCompileTestArgument(
                        "by.aurorasoft.replicator.TestService",
                        """
                                package by.aurorasoft.replicator;
                                                                
                                import by.aurorasoft.replicator.annotation.operation.ReplicatedDeleteById;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService.ProducerConfig;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService.TopicConfig;
                                import org.apache.kafka.common.serialization.LongSerializer;
                                                                
                                @ReplicatedService(
                                        producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
                                        topicConfig = @TopicConfig(name = "sync-dto")
                                )
                                public class TestService {
                                                                
                                    @ReplicatedDeleteById
                                    public void delete(Long id) {
                                        throw new UnsupportedOperationException();
                                    }
                                }
                                """
                ),
                new SuccessCompileTestArgument(
                        "by.aurorasoft.replicator.TestService",
                        """
                                package by.aurorasoft.replicator;
                                 
                                 import by.aurorasoft.replicator.annotation.operation.ReplicatedDeleteAll;
                                 import by.aurorasoft.replicator.annotation.service.ReplicatedService;
                                 import by.aurorasoft.replicator.annotation.service.ReplicatedService.ProducerConfig;
                                 import by.aurorasoft.replicator.annotation.service.ReplicatedService.TopicConfig;
                                 import by.aurorasoft.replicator.testcrud.TestEntity;
                                 import org.apache.kafka.common.serialization.LongSerializer;
                                 import org.springframework.data.jpa.repository.JpaRepository;
                                 
                                 @ReplicatedService(
                                         producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
                                         topicConfig = @TopicConfig(name = "sync-dto")
                                 )
                                 public class TestService {
                                     private final JpaRepository<Long, TestEntity> repository;
                                 
                                     public TestService(JpaRepository<Long, TestEntity> repository) {
                                         this.repository = repository;
                                     }
                                 
                                     @ReplicatedDeleteAll
                                     public void deleteAll() {
                                         throw new UnsupportedOperationException();
                                     }
                                 }"""
                ),
                new SuccessCompileTestArgument(
                        "by.aurorasoft.replicator.TestService",
                        """
                                package by.aurorasoft.replicator;
                                                                        
                                import by.aurorasoft.replicator.annotation.operation.ReplicatedDeleteAll;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService.ProducerConfig;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService.TopicConfig;
                                import by.aurorasoft.replicator.testcrud.TestEntity;
                                import org.apache.kafka.common.serialization.LongSerializer;
                                import org.springframework.data.jpa.repository.JpaRepository;
                                                                        
                                abstract class AbstractService<E, ID> {
                                    private final JpaRepository<E, ID> repository;
                                                                        
                                    public AbstractService(JpaRepository<E, ID> repository) {
                                        this.repository = repository;
                                    }
                                }
                                                                        
                                @ReplicatedService(
                                        producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
                                        topicConfig = @TopicConfig(name = "sync-dto")
                                )
                                public class TestService extends AbstractService<TestEntity, Long> {
                                                                        
                                    public TestService(JpaRepository<TestEntity, Long> repository) {
                                        super(repository);
                                    }
                                                                        
                                    @ReplicatedDeleteAll
                                    public void deleteAll() {
                                        throw new UnsupportedOperationException();
                                    }
                                }"""
                ),
                new SuccessCompileTestArgument(
                        "by.aurorasoft.replicator.TestService",
                        """
                                package by.aurorasoft.replicator;
                                                                        
                                import by.aurorasoft.replicator.annotation.operation.ReplicatedDeleteAll;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService.ProducerConfig;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService.TopicConfig;
                                import by.aurorasoft.replicator.testcrud.TestEntity;
                                import org.apache.kafka.common.serialization.LongSerializer;
                                import org.springframework.data.jpa.repository.JpaRepository;
                                                                        
                                abstract class FirstAbstractService<E, ID> {
                                    private final JpaRepository<E, ID> repository;
                                                                        
                                    public FirstAbstractService(JpaRepository<E, ID> repository) {
                                        this.repository = repository;
                                    }
                                }
                                                                        
                                abstract class SecondAbstractService<E, ID> extends FirstAbstractService<E, ID> {
                                                                        
                                    public SecondAbstractService(JpaRepository<E, ID> repository) {
                                        super(repository);
                                    }
                                }
                                                                        
                                @ReplicatedService(
                                        producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
                                        topicConfig = @TopicConfig(name = "sync-dto")
                                )
                                public class TestService extends SecondAbstractService<TestEntity, Long> {
                                                                        
                                    public TestService(JpaRepository<TestEntity, Long> repository) {
                                        super(repository);
                                    }
                                                                        
                                    @ReplicatedDeleteAll
                                    public void deleteAll() {
                                        throw new UnsupportedOperationException();
                                    }
                                }"""
                )
        );
    }

    private static Stream<FailedCompileTestArgument> provideFailedCompileArguments() {
        return Stream.of(
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
                        """
                                Compilation error: /by/aurorasoft/replicator/TestService.java:12: error: Element annotated by @ReplicatedService should match next requirements:
                                class TestService {
                                ^
                                       - Element should be public
                                1 error
                                """
                ),
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
                        """
                                Compilation error: /by/aurorasoft/replicator/TestService.java:16: error: Element annotated by @ReplicatedSave should match next requirements:
                                    public void save() {
                                                ^
                                       - Element should be public
                                       - It should be inside class annotated by @ReplicatedService
                                       - Returned object should contain id's getter
                                1 error
                                """
                ),
                new FailedCompileTestArgument(
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
                                    TestDto save() {
                                        throw new UnsupportedOperationException();
                                    }
                                }""",
                        """
                                Compilation error: /by/aurorasoft/replicator/TestService.java:17: error: Element annotated by @ReplicatedSave should match next requirements:
                                    TestDto save() {
                                            ^
                                       - Element should be public
                                       - It should be inside class annotated by @ReplicatedService
                                       - Returned object should contain id's getter
                                1 error
                                """
                ),
                new FailedCompileTestArgument(
                        "by.aurorasoft.replicator.TestService",
                        """
                                package by.aurorasoft.replicator;
                                                                        
                                import by.aurorasoft.replicator.annotation.operation.ReplicatedSave;
                                import by.aurorasoft.replicator.testcrud.TestDto;
                                                                        
                                public class TestService {
                                                                        
                                    @ReplicatedSave
                                    public TestDto save() {
                                        throw new UnsupportedOperationException();
                                    }
                                }""",
                        """
                                Compilation error: /by/aurorasoft/replicator/TestService.java:9: error: Element annotated by @ReplicatedSave should match next requirements:
                                    public TestDto save() {
                                                   ^
                                       - Element should be public
                                       - It should be inside class annotated by @ReplicatedService
                                       - Returned object should contain id's getter
                                1 error
                                """
                ),
                new FailedCompileTestArgument(
                        "by.aurorasoft.replicator.TestService",
                        """
                                package by.aurorasoft.replicator;
                                                                
                                import by.aurorasoft.replicator.annotation.operation.ReplicatedSaveAll;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService.ProducerConfig;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService.TopicConfig;
                                import by.aurorasoft.replicator.testcrud.TestDto;
                                import org.apache.kafka.common.serialization.LongSerializer;
                                                                
                                import java.util.List;
                                                                
                                @ReplicatedService(
                                        producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
                                        topicConfig = @TopicConfig(name = "sync-dto")
                                )
                                public class TestService {
                                                                
                                    @ReplicatedSaveAll
                                    List<TestDto> saveAll() {
                                        throw new UnsupportedOperationException();
                                    }
                                }""",
                        """
                                Compilation error: /by/aurorasoft/replicator/TestService.java:19: error: Element annotated by @ReplicatedSaveAll should match next requirements:
                                    List<TestDto> saveAll() {
                                                  ^
                                       - Element should be public
                                       - It should be inside class annotated by @ReplicatedService
                                       - Returned list's objects should contain id's getter
                                1 error
                                """
                ),
                new FailedCompileTestArgument(
                        "by.aurorasoft.replicator.TestService",
                        """
                                package by.aurorasoft.replicator;
                                                                
                                import by.aurorasoft.replicator.annotation.operation.ReplicatedSaveAll;
                                import by.aurorasoft.replicator.testcrud.TestDto;
                                                                
                                import java.util.List;
                                                                
                                public class TestService {
                                                                
                                    @ReplicatedSaveAll
                                    public List<TestDto> saveAll() {
                                        throw new UnsupportedOperationException();
                                    }
                                }""",
                        """
                                Compilation error: /by/aurorasoft/replicator/TestService.java:11: error: Element annotated by @ReplicatedSaveAll should match next requirements:
                                    public List<TestDto> saveAll() {
                                                         ^
                                       - Element should be public
                                       - It should be inside class annotated by @ReplicatedService
                                       - Returned list's objects should contain id's getter
                                1 error
                                """
                ),
                new FailedCompileTestArgument(
                        "by.aurorasoft.replicator.TestService",
                        """
                                package by.aurorasoft.replicator;
                                                                
                                import by.aurorasoft.replicator.annotation.operation.ReplicatedSaveAll;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService.ProducerConfig;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService.TopicConfig;
                                import org.apache.kafka.common.serialization.LongSerializer;
                                                                
                                import java.util.List;
                                                                
                                @ReplicatedService(
                                        producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
                                        topicConfig = @TopicConfig(name = "sync-dto")
                                )
                                public class TestService {
                                                                
                                    @ReplicatedSaveAll
                                    public List<Object> saveAll() {
                                        throw new UnsupportedOperationException();
                                    }
                                }""",
                        """
                                Compilation error: /by/aurorasoft/replicator/TestService.java:18: error: Element annotated by @ReplicatedSaveAll should match next requirements:
                                    public List<Object> saveAll() {
                                                        ^
                                       - Element should be public
                                       - It should be inside class annotated by @ReplicatedService
                                       - Returned list's objects should contain id's getter
                                1 error
                                """
                ),
                new FailedCompileTestArgument(
                        "by.aurorasoft.replicator.TestService",
                        """
                                package by.aurorasoft.replicator;
                                                                        
                                import by.aurorasoft.replicator.annotation.operation.ReplicatedSaveAll;
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
                                                                        
                                    @ReplicatedSaveAll
                                    public Iterable<TestDto> saveAll() {
                                        throw new UnsupportedOperationException();
                                    }
                                }""",
                        """
                                Compilation error: /by/aurorasoft/replicator/TestService.java:17: error: Element annotated by @ReplicatedSaveAll should match next requirements:
                                    public Iterable<TestDto> saveAll() {
                                                             ^
                                       - Element should be public
                                       - It should be inside class annotated by @ReplicatedService
                                       - Returned list's objects should contain id's getter
                                1 error
                                """
                ),
                new FailedCompileTestArgument(
                        "by.aurorasoft.replicator.TestService",
                        """
                                package by.aurorasoft.replicator;
                                                                
                                import by.aurorasoft.replicator.annotation.operation.ReplicatedDelete;
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
                                                                
                                    @ReplicatedDelete
                                    void delete(TestDto dto) {
                                        throw new UnsupportedOperationException();
                                    }
                                }""",
                        """
                                Compilation error: /by/aurorasoft/replicator/TestService.java:17: error: Element annotated by @ReplicatedDelete should match next requirements:
                                    void delete(TestDto dto) {
                                         ^
                                       - Element should be public
                                       - It should be inside class annotated by @ReplicatedService
                                       - Method should contain at least one parameter and this parameter should contain id's getter
                                1 error
                                """
                ),
                new FailedCompileTestArgument(
                        "by.aurorasoft.replicator.TestService",
                        """
                                package by.aurorasoft.replicator;
                                                                
                                import by.aurorasoft.replicator.annotation.operation.ReplicatedDelete;
                                import by.aurorasoft.replicator.testcrud.TestDto;
                                                                
                                public class TestService {
                                                                
                                    @ReplicatedDelete
                                    public void delete(TestDto dto) {
                                        throw new UnsupportedOperationException();
                                    }
                                }""",
                        """
                                Compilation error: /by/aurorasoft/replicator/TestService.java:9: error: Element annotated by @ReplicatedDelete should match next requirements:
                                    public void delete(TestDto dto) {
                                                ^
                                       - Element should be public
                                       - It should be inside class annotated by @ReplicatedService
                                       - Method should contain at least one parameter and this parameter should contain id's getter
                                1 error
                                """
                ),
                new FailedCompileTestArgument(
                        "by.aurorasoft.replicator.TestService",
                        """
                                package by.aurorasoft.replicator;
                                                                        
                                import by.aurorasoft.replicator.annotation.operation.ReplicatedDelete;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService.ProducerConfig;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService.TopicConfig;
                                import org.apache.kafka.common.serialization.LongSerializer;
                                                                        
                                @ReplicatedService(
                                        producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
                                        topicConfig = @TopicConfig(name = "sync-dto")
                                )
                                public class TestService {
                                                                        
                                    @ReplicatedDelete
                                    public void delete() {
                                        throw new UnsupportedOperationException();
                                    }
                                }
                                """,
                        """
                                Compilation error: /by/aurorasoft/replicator/TestService.java:16: error: Element annotated by @ReplicatedDelete should match next requirements:
                                    public void delete() {
                                                ^
                                       - Element should be public
                                       - It should be inside class annotated by @ReplicatedService
                                       - Method should contain at least one parameter and this parameter should contain id's getter
                                1 error
                                """
                ),
                new FailedCompileTestArgument(
                        "by.aurorasoft.replicator.TestService",
                        """
                                package by.aurorasoft.replicator;
                                                                
                                import by.aurorasoft.replicator.annotation.operation.ReplicatedDelete;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService.ProducerConfig;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService.TopicConfig;
                                import org.apache.kafka.common.serialization.LongSerializer;
                                                                
                                @ReplicatedService(
                                        producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
                                        topicConfig = @TopicConfig(name = "sync-dto")
                                )
                                public class TestService {
                                                                
                                    @ReplicatedDelete
                                    public void delete(Object object) {
                                        throw new UnsupportedOperationException();
                                    }
                                }""",
                        """
                                Compilation error: /by/aurorasoft/replicator/TestService.java:16: error: Element annotated by @ReplicatedDelete should match next requirements:
                                    public void delete(Object object) {
                                                ^
                                       - Element should be public
                                       - It should be inside class annotated by @ReplicatedService
                                       - Method should contain at least one parameter and this parameter should contain id's getter
                                1 error
                                """
                ),
                new FailedCompileTestArgument(
                        "by.aurorasoft.replicator.TestService",
                        """
                                package by.aurorasoft.replicator;
                                                                        
                                import by.aurorasoft.replicator.annotation.operation.ReplicatedDeleteIterable;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService.ProducerConfig;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService.TopicConfig;
                                import by.aurorasoft.replicator.testcrud.TestDto;
                                import org.apache.kafka.common.serialization.LongSerializer;
                                                                        
                                import java.util.List;
                                                                        
                                @ReplicatedService(
                                        producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
                                        topicConfig = @TopicConfig(name = "sync-dto")
                                )
                                public class TestService {
                                                                        
                                    @ReplicatedDeleteIterable
                                    void delete(List<TestDto> dtos) {
                                        throw new UnsupportedOperationException();
                                    }
                                }""",
                        """
                                Compilation error: /by/aurorasoft/replicator/TestService.java:19: error: Element annotated by @ReplicatedDeleteIterable should match next requirements:
                                    void delete(List<TestDto> dtos) {
                                         ^
                                       - Element should be public
                                       - It should be inside class annotated by @ReplicatedService
                                       - Method should have at least one parameter as Iterable. Elements should contain id's getter
                                1 error
                                """
                ),
                new FailedCompileTestArgument(
                        "by.aurorasoft.replicator.TestService",
                        """
                                package by.aurorasoft.replicator;
                                                                
                                import by.aurorasoft.replicator.annotation.operation.ReplicatedDeleteIterable;
                                import by.aurorasoft.replicator.testcrud.TestDto;
                                                                
                                import java.util.List;
                                                                
                                public class TestService {
                                                                
                                    @ReplicatedDeleteIterable
                                    public void delete(List<TestDto> dtos) {
                                        throw new UnsupportedOperationException();
                                    }
                                }""",
                        """
                                Compilation error: /by/aurorasoft/replicator/TestService.java:11: error: Element annotated by @ReplicatedDeleteIterable should match next requirements:
                                    public void delete(List<TestDto> dtos) {
                                                ^
                                       - Element should be public
                                       - It should be inside class annotated by @ReplicatedService
                                       - Method should have at least one parameter as Iterable. Elements should contain id's getter
                                1 error
                                """
                ),
                new FailedCompileTestArgument(
                        "by.aurorasoft.replicator.TestService",
                        """
                                package by.aurorasoft.replicator;
                                                                        
                                import by.aurorasoft.replicator.annotation.operation.ReplicatedDeleteIterable;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService.ProducerConfig;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService.TopicConfig;
                                import org.apache.kafka.common.serialization.LongSerializer;
                                                                        
                                @ReplicatedService(
                                        producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
                                        topicConfig = @TopicConfig(name = "sync-dto")
                                )
                                public class TestService {
                                                                        
                                    @ReplicatedDeleteIterable
                                    public void delete() {
                                        throw new UnsupportedOperationException();
                                    }
                                }""",
                        """
                                Compilation error: /by/aurorasoft/replicator/TestService.java:16: error: Element annotated by @ReplicatedDeleteIterable should match next requirements:
                                    public void delete() {
                                                ^
                                       - Element should be public
                                       - It should be inside class annotated by @ReplicatedService
                                       - Method should have at least one parameter as Iterable. Elements should contain id's getter
                                1 error
                                """
                ),
                new FailedCompileTestArgument(
                        "by.aurorasoft.replicator.TestService",
                        """
                                package by.aurorasoft.replicator;
                                                                        
                                import by.aurorasoft.replicator.annotation.operation.ReplicatedDeleteIterable;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService.ProducerConfig;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService.TopicConfig;
                                import org.apache.kafka.common.serialization.LongSerializer;
                                                                        
                                @ReplicatedService(
                                        producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
                                        topicConfig = @TopicConfig(name = "sync-dto")
                                )
                                public class TestService {
                                                                        
                                    @ReplicatedDeleteIterable
                                    public void delete(Object object) {
                                        throw new UnsupportedOperationException();
                                    }
                                }""",
                        """
                                Compilation error: /by/aurorasoft/replicator/TestService.java:16: error: Element annotated by @ReplicatedDeleteIterable should match next requirements:
                                    public void delete(Object object) {
                                                ^
                                       - Element should be public
                                       - It should be inside class annotated by @ReplicatedService
                                       - Method should have at least one parameter as Iterable. Elements should contain id's getter
                                1 error
                                """
                ),
                new FailedCompileTestArgument(
                        "by.aurorasoft.replicator.TestService",
                        """
                                package by.aurorasoft.replicator;
                                                                        
                                import by.aurorasoft.replicator.annotation.operation.ReplicatedDeleteIterable;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService.ProducerConfig;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService.TopicConfig;
                                import org.apache.kafka.common.serialization.LongSerializer;
                                                                        
                                @ReplicatedService(
                                        producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
                                        topicConfig = @TopicConfig(name = "sync-dto")
                                )
                                public class TestService {
                                                                        
                                    @ReplicatedDeleteIterable
                                    public void delete(Iterable<Object> objects) {
                                        throw new UnsupportedOperationException();
                                    }
                                }""",
                        """
                                Compilation error: /by/aurorasoft/replicator/TestService.java:16: error: Element annotated by @ReplicatedDeleteIterable should match next requirements:
                                    public void delete(Iterable<Object> objects) {
                                                ^
                                       - Element should be public
                                       - It should be inside class annotated by @ReplicatedService
                                       - Method should have at least one parameter as Iterable. Elements should contain id's getter
                                1 error
                                """
                ),
                new FailedCompileTestArgument(
                        "by.aurorasoft.replicator.TestService",
                        """
                                package by.aurorasoft.replicator;
                                                                        
                                import by.aurorasoft.replicator.annotation.operation.ReplicatedDeleteByIds;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService.ProducerConfig;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService.TopicConfig;
                                import org.apache.kafka.common.serialization.LongSerializer;
                                                                        
                                import java.util.List;
                                                                        
                                @ReplicatedService(
                                        producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
                                        topicConfig = @TopicConfig(name = "sync-dto")
                                )
                                public class TestService {
                                                                        
                                    @ReplicatedDeleteByIds
                                    void delete(List<Long> ids) {
                                        throw new UnsupportedOperationException();
                                    }
                                }""",
                        """
                                Compilation error: /by/aurorasoft/replicator/TestService.java:18: error: Element annotated by @ReplicatedDeleteByIds should match next requirements:
                                    void delete(List<Long> ids) {
                                         ^
                                       - Element should be public
                                       - It should be inside class annotated by @ReplicatedService
                                       - Method should have at least one parameter as Iterable
                                1 error
                                """
                ),
                new FailedCompileTestArgument(
                        "by.aurorasoft.replicator.TestService",
                        """
                                package by.aurorasoft.replicator;
                                 
                                 import by.aurorasoft.replicator.annotation.operation.ReplicatedDeleteByIds;
                                 
                                 import java.util.List;
                                 
                                 public class TestService {
                                 
                                     @ReplicatedDeleteByIds
                                     public void delete(List<Long> ids) {
                                         throw new UnsupportedOperationException();
                                     }
                                 }""",
                        """
                                Compilation error: /by/aurorasoft/replicator/TestService.java:10: error: Element annotated by @ReplicatedDeleteByIds should match next requirements:
                                     public void delete(List<Long> ids) {
                                                 ^
                                       - Element should be public
                                       - It should be inside class annotated by @ReplicatedService
                                       - Method should have at least one parameter as Iterable
                                1 error
                                """
                ),
                new FailedCompileTestArgument(
                        "by.aurorasoft.replicator.TestService",
                        """
                                package by.aurorasoft.replicator;
                                                                        
                                import by.aurorasoft.replicator.annotation.operation.ReplicatedDeleteByIds;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService.ProducerConfig;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService.TopicConfig;
                                import org.apache.kafka.common.serialization.LongSerializer;
                                                                        
                                @ReplicatedService(
                                        producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
                                        topicConfig = @TopicConfig(name = "sync-dto")
                                )
                                public class TestService {
                                                                        
                                    @ReplicatedDeleteByIds
                                    public void delete() {
                                        throw new UnsupportedOperationException();
                                    }
                                }""",
                        """
                                Compilation error: /by/aurorasoft/replicator/TestService.java:16: error: Element annotated by @ReplicatedDeleteByIds should match next requirements:
                                    public void delete() {
                                                ^
                                       - Element should be public
                                       - It should be inside class annotated by @ReplicatedService
                                       - Method should have at least one parameter as Iterable
                                1 error
                                """
                ),
                new FailedCompileTestArgument(
                        "by.aurorasoft.replicator.TestService",
                        """
                                package by.aurorasoft.replicator;
                                                                
                                import by.aurorasoft.replicator.annotation.operation.ReplicatedDeleteByIds;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService.ProducerConfig;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService.TopicConfig;
                                import org.apache.kafka.common.serialization.LongSerializer;
                                                                
                                @ReplicatedService(
                                        producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
                                        topicConfig = @TopicConfig(name = "sync-dto")
                                )
                                public class TestService {
                                                                
                                    @ReplicatedDeleteByIds
                                    public void delete(Object object) {
                                        throw new UnsupportedOperationException();
                                    }
                                }""",
                        """
                                Compilation error: /by/aurorasoft/replicator/TestService.java:16: error: Element annotated by @ReplicatedDeleteByIds should match next requirements:
                                    public void delete(Object object) {
                                                ^
                                       - Element should be public
                                       - It should be inside class annotated by @ReplicatedService
                                       - Method should have at least one parameter as Iterable
                                1 error
                                """
                ),
                new FailedCompileTestArgument(
                        "by.aurorasoft.replicator.TestService",
                        """
                                package by.aurorasoft.replicator;
                                                                
                                import by.aurorasoft.replicator.annotation.operation.ReplicatedDeleteById;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService.ProducerConfig;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService.TopicConfig;
                                import org.apache.kafka.common.serialization.LongSerializer;
                                                                
                                @ReplicatedService(
                                        producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
                                        topicConfig = @TopicConfig(name = "sync-dto")
                                )
                                public class TestService {
                                                                
                                    @ReplicatedDeleteById
                                    void delete(Long id) {
                                        throw new UnsupportedOperationException();
                                    }
                                }
                                """,
                        """
                                Compilation error: /by/aurorasoft/replicator/TestService.java:16: error: Element annotated by @ReplicatedDeleteById should match next requirements:
                                    void delete(Long id) {
                                         ^
                                       - Element should be public
                                       - It should be inside class annotated by @ReplicatedService
                                       - Method should have at least one parameter as id
                                1 error
                                """
                ),
                new FailedCompileTestArgument(
                        "by.aurorasoft.replicator.TestService",
                        """
                                package by.aurorasoft.replicator;
                                                                
                                import by.aurorasoft.replicator.annotation.operation.ReplicatedDeleteById;
                                                                
                                public class TestService {
                                                                
                                    @ReplicatedDeleteById
                                    public void delete(Long id) {
                                        throw new UnsupportedOperationException();
                                    }
                                }
                                """,
                        """
                                Compilation error: /by/aurorasoft/replicator/TestService.java:8: error: Element annotated by @ReplicatedDeleteById should match next requirements:
                                    public void delete(Long id) {
                                                ^
                                       - Element should be public
                                       - It should be inside class annotated by @ReplicatedService
                                       - Method should have at least one parameter as id
                                1 error
                                """
                ),
                new FailedCompileTestArgument(
                        "by.aurorasoft.replicator.TestService",
                        """
                                package by.aurorasoft.replicator;
                                                                        
                                import by.aurorasoft.replicator.annotation.operation.ReplicatedDeleteById;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService.ProducerConfig;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService.TopicConfig;
                                import org.apache.kafka.common.serialization.LongSerializer;
                                                                        
                                @ReplicatedService(
                                        producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
                                        topicConfig = @TopicConfig(name = "sync-dto")
                                )
                                public class TestService {
                                                                        
                                    @ReplicatedDeleteById
                                    public void delete() {
                                        throw new UnsupportedOperationException();
                                    }
                                }
                                """,
                        """
                                Compilation error: /by/aurorasoft/replicator/TestService.java:16: error: Element annotated by @ReplicatedDeleteById should match next requirements:
                                    public void delete() {
                                                ^
                                       - Element should be public
                                       - It should be inside class annotated by @ReplicatedService
                                       - Method should have at least one parameter as id
                                1 error
                                """
                ),
                new FailedCompileTestArgument(
                        "by.aurorasoft.replicator.TestService",
                        """
                                package by.aurorasoft.replicator;
                                                                        
                                import by.aurorasoft.replicator.annotation.operation.ReplicatedDeleteAll;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService.ProducerConfig;
                                import by.aurorasoft.replicator.annotation.service.ReplicatedService.TopicConfig;
                                import by.aurorasoft.replicator.testcrud.TestEntity;
                                import org.apache.kafka.common.serialization.LongSerializer;
                                import org.springframework.data.jpa.repository.JpaRepository;
                                                                        
                                @ReplicatedService(
                                        producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
                                        topicConfig = @TopicConfig(name = "sync-dto")
                                )
                                public class TestService {
                                    private final JpaRepository<Long, TestEntity> repository;
                                                                        
                                    public TestService(JpaRepository<Long, TestEntity> repository) {
                                        this.repository = repository;
                                    }
                                                                        
                                    @ReplicatedDeleteAll
                                    void deleteAll() {
                                        throw new UnsupportedOperationException();
                                    }
                                }""",
                        """
                                Compilation error: /by/aurorasoft/replicator/TestService.java:23: error: Element annotated by @ReplicatedDeleteAll should match next requirements:
                                    void deleteAll() {
                                         ^
                                       - Element should be public
                                       - It should be inside class annotated by @ReplicatedService
                                       - Service should contain repository
                                1 error
                                """
                ),
                new FailedCompileTestArgument(
                        "by.aurorasoft.replicator.TestService",
                        """
                                package by.aurorasoft.replicator;
                                                                
                                import by.aurorasoft.replicator.annotation.operation.ReplicatedDeleteAll;
                                import by.aurorasoft.replicator.testcrud.TestEntity;
                                import org.springframework.data.jpa.repository.JpaRepository;
                                                                
                                public class TestService {
                                    private final JpaRepository<Long, TestEntity> repository;
                                                                
                                    public TestService(JpaRepository<Long, TestEntity> repository) {
                                        this.repository = repository;
                                    }
                                                                
                                    @ReplicatedDeleteAll
                                    public void deleteAll() {
                                        throw new UnsupportedOperationException();
                                    }
                                }""",
                        """
                                Compilation error: /by/aurorasoft/replicator/TestService.java:15: error: Element annotated by @ReplicatedDeleteAll should match next requirements:
                                    public void deleteAll() {
                                                ^
                                       - Element should be public
                                       - It should be inside class annotated by @ReplicatedService
                                       - Service should contain repository
                                1 error
                                """
                )
        );
    }
}
