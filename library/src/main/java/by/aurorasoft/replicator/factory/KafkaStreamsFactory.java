package by.aurorasoft.replicator.factory;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.springframework.stereotype.Component;

import static java.lang.Runtime.getRuntime;
import static org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler.StreamThreadExceptionResponse.SHUTDOWN_APPLICATION;

@Component
public final class KafkaStreamsFactory {

    public KafkaStreams create(final Topology topology, final StreamsConfig config) {
        final KafkaStreams streams = new KafkaStreams(topology, config);
        try {
            streams.setUncaughtExceptionHandler(exception -> SHUTDOWN_APPLICATION);
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
