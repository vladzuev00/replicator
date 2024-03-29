package by.aurorasoft.replicator.consuming.starter;

import by.aurorasoft.replicator.consuming.pipeline.ReplicationConsumePipeline;
import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.apache.kafka.clients.admin.AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.APPLICATION_ID_CONFIG;
import static org.apache.kafka.streams.kstream.Consumed.with;

@Component
public final class ReplicationConsumePipelineStarter {
    private final String bootstrapAddress;

    public ReplicationConsumePipelineStarter(@Value("${spring.kafka.bootstrap-servers}") final String bootstrapAddress) {
        this.bootstrapAddress = bootstrapAddress;
    }

    public <ID, E extends AbstractEntity<ID>> void start(final ReplicationConsumePipeline<ID, E> pipeline) {
        final StreamsBuilder streamsBuilder = new StreamsBuilder();
        streamsBuilder
                .stream(pipeline.getTopic(), with(pipeline.getIdSerde(), pipeline.getReplicationSerde()))
                .foreach((id, replication) -> replication.execute(pipeline.getRepository()));
        final KafkaStreams kafkaStreams = new KafkaStreams(streamsBuilder.build(), createStreamsConfig(pipeline));
        kafkaStreams.setUncaughtExceptionHandler(e -> StreamsUncaughtExceptionHandler.StreamThreadExceptionResponse.REPLACE_THREAD);
        kafkaStreams.start();
    }

    private StreamsConfig createStreamsConfig(final ReplicationConsumePipeline<?, ?> pipeline) {
        final Map<String, Object> configsByNames = Map.of(
                APPLICATION_ID_CONFIG, pipeline.getId(),
                BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress
        );
        return new StreamsConfig(configsByNames);
    }
}
