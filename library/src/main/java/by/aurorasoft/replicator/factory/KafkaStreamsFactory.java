package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.consuming.exceptionhandler.ReplicationConsumeExceptionHandler;
import by.aurorasoft.replicator.holder.KafkaStreamsHolder;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.apache.kafka.clients.admin.AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.APPLICATION_ID_CONFIG;

@Component
public final class KafkaStreamsFactory {
    private final ReplicationConsumeExceptionHandler exceptionHandler;
    private final KafkaStreamsHolder streamsHolder;
    private final String bootstrapAddress;

    public KafkaStreamsFactory(final ReplicationConsumeExceptionHandler exceptionHandler,
                               final KafkaStreamsHolder streamsHolder,
                               @Value("${spring.kafka.bootstrap-servers}") final String bootstrapAddress) {
        this.exceptionHandler = exceptionHandler;
        this.streamsHolder = streamsHolder;
        this.bootstrapAddress = bootstrapAddress;
    }

    public KafkaStreams create(final StreamsBuilder builder, final String applicationId) {
        final StreamsConfig config = createStreamsConfig(applicationId);
        final Topology topology = builder.build();
        final KafkaStreams streams = new KafkaStreams(topology, config);
        configure(streams);
        return streams;
    }

    private StreamsConfig createStreamsConfig(final String applicationId) {
        final Map<String, Object> configsByNames = Map.of(
                APPLICATION_ID_CONFIG, applicationId,
                BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress
        );
        return new StreamsConfig(configsByNames);
    }

    private void configure(final KafkaStreams streams) {
        try {
            streams.setUncaughtExceptionHandler(exceptionHandler);
            streamsHolder.add(streams);
        } catch (final Exception exception) {
            streams.close();
            throw exception;
        }
    }
}
