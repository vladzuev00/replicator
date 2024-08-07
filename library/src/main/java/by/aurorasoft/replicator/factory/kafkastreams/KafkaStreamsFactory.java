package by.aurorasoft.replicator.factory.kafkastreams;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.springframework.stereotype.Component;

import static java.lang.Runtime.getRuntime;

@Component
public final class KafkaStreamsFactory {

    public KafkaStreams create(Topology topology, StreamsConfig config) {
        KafkaStreams streams = new KafkaStreams(topology, config);
        try {
            closeOnShutdown(streams);
            return streams;
        } catch (Exception exception) {
            streams.close();
            throw exception;
        }
    }

    private void closeOnShutdown(KafkaStreams streams) {
        getRuntime().addShutdownHook(new Thread(streams::close));
    }
}
