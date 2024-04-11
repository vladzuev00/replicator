package by.aurorasoft.testapp.base.containerinitializer;

import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.stream.Stream;

import static org.testcontainers.utility.DockerImageName.parse;

public final class KafkaContainerInitializer extends ContainerInitializer {
    private static final String PROPERTY_KEY_BOOTSTRAP_SERVERS = "spring.kafka.bootstrap-servers";

    private static final String FULL_IMAGE_NAME = "confluentinc/cp-kafka:latest";
    private static final DockerImageName DOCKER_IMAGE_NAME = parse(FULL_IMAGE_NAME);
    private static final KafkaContainer CONTAINER = new KafkaContainer(DOCKER_IMAGE_NAME);

    static {
        start(CONTAINER);
    }

    @Override
    protected Stream<TestProperty> getProperties() {
        return Stream.of(new TestProperty(PROPERTY_KEY_BOOTSTRAP_SERVERS, CONTAINER.getBootstrapServers()));
    }
}
