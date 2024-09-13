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
                                           
                                        }"""
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
                ),
                Arguments.of(
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
                        )
                ),
                Arguments.of(
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
                        )
                ),
                Arguments.of(
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
                        )
                ),
                Arguments.of(
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
                        )
                ),
                Arguments.of(
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
                                """
                                        Compilation error: /by/aurorasoft/replicator/TestService.java:12: error: Element annotated by @ReplicatedService should match next requirements:
                                        class TestService {
                                        ^
                                          	 - Element should be public
                                        1 error
                                        """
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
                                """
                                        Compilation error: /by/aurorasoft/replicator/TestService.java:16: error: Element annotated by @ReplicatedSave should match next requirements:
                                            public void save() {
                                                        ^
                                          	 - Element should be public
                                          	 - It should be inside class annotated by @ReplicatedService
                                          	 - Returned object should contain id's getter
                                        1 error
                                        """
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
                        )
                ),
                Arguments.of(
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
                        )
                ),
                Arguments.of(
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
                        )
                ),
                Arguments.of(
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
                        )
                ),
                Arguments.of(
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
                        )
                ),
                Arguments.of(
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
                        )
                ),
                Arguments.of(
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
                        )
                ),
                Arguments.of(
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
                        )
                ),
                Arguments.of(
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
                        )
                ),
                Arguments.of(
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
                        )
                ),
                Arguments.of(
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
                        )
                ),
                Arguments.of(
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
                        )
                ),
                Arguments.of(
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
                        )
                ),
                Arguments.of(
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
                        )
                ),
                Arguments.of(
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
                        )
                )
        );
    }
}
