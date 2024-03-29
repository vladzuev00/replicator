package by.aurorasoft.replicator.holder;

import org.apache.kafka.streams.KafkaStreams;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public final class KafkaStreamsHolder {
    private final List<KafkaStreams> elements = new ArrayList<>();

    public void add(final KafkaStreams element) {
        elements.add(element);
    }

    @EventListener(ContextClosedEvent.class)
    public void closeStreams() {
        elements.forEach(KafkaStreams::close);
    }
}
