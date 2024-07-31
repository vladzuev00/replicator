package by.aurorasoft.replicator.annotation.processor;

import org.joor.CompileOptions;
import org.joor.Reflect;
import org.joor.ReflectException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public final class ReplicatedRepositoryProcessorTest {
    private static final String WRONG_ANNOTATING_MESSAGE = "@by.aurorasoft.replicator.annotation.ReplicatedRepository "
            + "can be applied only for instant of org.springframework.data.jpa.repository.JpaRepository";

    @Test
    public void sourceCodeShouldBeCompiled() {
        String givenInterfaceName = "TestJpaRepository";
        String givenSourceCode = """
                package by.aurorasoft.replicator;
                                
                import by.aurorasoft.replicator.annotation.ReplicatedRepository;
                import by.aurorasoft.replicator.annotation.ReplicatedRepository.Producer;
                import by.aurorasoft.replicator.annotation.ReplicatedRepository.Topic;
                import org.apache.kafka.common.serialization.LongSerializer;
                import org.springframework.data.jpa.repository.JpaRepository;
                                
                @ReplicatedRepository(producer = @Producer(idSerializer = LongSerializer.class), topic = @Topic(name = "test-sync"))
                public interface TestJpaRepository extends JpaRepository<Object, Long> {
                                
                }""";

        compile(givenInterfaceName, givenSourceCode);
    }

    @Test
    public void sourceCodeShouldNotBeCompiled() {
        String givenInterfaceName = "TestRepository";
        String givenSourceCode = """
                package by.aurorasoft.replicator;
                                
                import by.aurorasoft.replicator.annotation.ReplicatedRepository;
                import by.aurorasoft.replicator.annotation.ReplicatedRepository.Producer;
                import by.aurorasoft.replicator.annotation.ReplicatedRepository.Topic;
                import org.apache.kafka.common.serialization.LongSerializer;
                                
                @ReplicatedRepository(producer = @Producer(idSerializer = LongSerializer.class), topic = @Topic(name = "test-sync"))
                public interface TestRepository {
                                
                }""";

        compileExpectingAnnotatingError(givenInterfaceName, givenSourceCode);
    }

    private void compile(String interfaceName, String sourceCode) {
        Reflect.compile(interfaceName, sourceCode, getCompileOptions());
    }

    private void compileExpectingAnnotatingError(String interfaceName, String sourceCode) {
        try {
            compile(interfaceName, sourceCode);
        } catch (ReflectException exception) {
            assertTrue(isWrongAnnotatingError(exception));
        }
    }

    private boolean isWrongAnnotatingError(ReflectException exception) {
        return exception.getMessage().contains(WRONG_ANNOTATING_MESSAGE);
    }

    private CompileOptions getCompileOptions() {
        return new CompileOptions().processors(new ReplicatedRepositoryProcessor());
    }
}
