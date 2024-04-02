package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.consuming.exceptionhandler.ReplicationConsumeExceptionHandler;
import by.aurorasoft.replicator.consuming.pipeline.ReplicationConsumePipeline;
import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

import static java.lang.Runtime.getRuntime;
import static org.apache.kafka.clients.admin.AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.APPLICATION_ID_CONFIG;
import static org.apache.kafka.streams.kstream.Consumed.with;

@Component
public final class ReplicationKafkaStreamsFactory {
    private final ReplicationConsumeExceptionHandler exceptionHandler;
    private final String bootstrapAddress;

    public ReplicationKafkaStreamsFactory(final ReplicationConsumeExceptionHandler exceptionHandler,
                                          @Value("${spring.kafka.bootstrap-servers}") final String bootstrapAddress) {
        this.exceptionHandler = exceptionHandler;
        this.bootstrapAddress = bootstrapAddress;
    }

    public <ID, E extends AbstractEntity<ID>> KafkaStreams create(final ReplicationConsumePipeline<ID, E> pipeline) {
        final StreamsConfig config = createStreamsConfig(pipeline);
        final StreamsBuilder builder = new StreamsBuilder();
        builder
                .stream(pipeline.getTopic(), with(pipeline.getIdSerde(), pipeline.getReplicationSerde()))
                .foreach((id, replication) -> replication.execute(pipeline.getRepository()));
        final Topology topology = builder.build();
        return create(topology, config);
    }

    private StreamsConfig createStreamsConfig(final ReplicationConsumePipeline<?, ?> pipeline) {
        final Map<String, Object> configsByNames = Map.of(
                APPLICATION_ID_CONFIG, pipeline.getId(),
                BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress
                //TODO:
//                StreamsConfig.EXACTLY_ONCE_V2
        );
        return new StreamsConfig(configsByNames);
    }

    private KafkaStreams create(final Topology topology, final StreamsConfig config) {
        final KafkaStreams streams = new KafkaStreams(topology, config);
        try {
            streams.setUncaughtExceptionHandler(exceptionHandler);
            closeOnShutdown(streams);
            return streams;
        } catch (final Exception exception) {
            streams.close();
            throw exception;
        }
    }

    private static void closeOnShutdown(final KafkaStreams streams) {
        getRuntime().addShutdownHook(new Thread(streams::close));
    }
}
