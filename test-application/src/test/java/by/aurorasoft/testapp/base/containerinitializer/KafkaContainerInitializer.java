package by.aurorasoft.testapp.base.containerinitializer;

import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;

import static org.testcontainers.utility.DockerImageName.parse;

public final class KafkaContainerInitializer extends ContainerInitializer<KafkaContainer> {
    private static final String PROPERTY_KEY_BOOTSTRAP_SERVERS = "spring.kafka.bootstrap-servers";

    private static final String FULL_IMAGE_NAME = "confluentinc/cp-kafka:latest";
    private static final DockerImageName DOCKER_IMAGE_NAME = parse(FULL_IMAGE_NAME);

    @Override
    protected KafkaContainer createContainer() {
        return new KafkaContainer(DOCKER_IMAGE_NAME);
    }

    @Override
    protected void configure(final KafkaContainer container) {

    }

    @Override
    protected Map<String, String> getPropertiesByKeys(final KafkaContainer container) {
        return Map.of(PROPERTY_KEY_BOOTSTRAP_SERVERS, container.getBootstrapServers());
    }
}
