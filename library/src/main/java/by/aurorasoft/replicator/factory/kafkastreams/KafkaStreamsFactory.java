package by.aurorasoft.replicator.factory.kafkastreams;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.springframework.stereotype.Component;

import static by.aurorasoft.replicator.util.ShutdownHookUtil.addShutdownHook;

@Component
public final class KafkaStreamsFactory {

    public KafkaStreams create(Topology topology, StreamsConfig config) {
        KafkaStreams streams = new KafkaStreams(topology, config);
        try {
            addShutdownHook(streams::close);
            return streams;
        } catch (Exception exception) {
            streams.close();
            throw exception;
        }
    }
}
