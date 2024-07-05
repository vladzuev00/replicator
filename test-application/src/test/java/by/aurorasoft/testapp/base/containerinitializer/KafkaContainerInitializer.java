package by.aurorasoft.testapp.base.containerinitializer;

import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;
import java.util.Optional;

import static java.util.Optional.empty;

public final class KafkaContainerInitializer extends ContainerInitializer<KafkaContainer> {
    private static final String KEY_BOOTSTRAP_SERVERS = "spring.kafka.bootstrap-servers";

    private static final String IMAGE_NAME = "confluentinc/cp-kafka:latest";

    @Override
    protected String getImageName() {
        return IMAGE_NAME;
    }

    @Override
    protected Optional<String> getOtherImageName() {
        return empty();
    }

    @Override
    protected KafkaContainer createContainer(DockerImageName imageName) {
        return new KafkaContainer(imageName);
    }

    @Override
    protected void configure(KafkaContainer container) {

    }

    @Override
    protected Map<String, String> getPropertiesByKeys(KafkaContainer container) {
        return Map.of(KEY_BOOTSTRAP_SERVERS, container.getBootstrapServers());
    }
}
