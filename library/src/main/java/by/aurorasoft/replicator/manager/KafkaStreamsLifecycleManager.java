package by.aurorasoft.replicator.manager;

import org.apache.kafka.streams.KafkaStreams;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public final class KafkaStreamsLifecycleManager {
    private final List<KafkaStreams> elements = new ArrayList<>();

    public void register(final KafkaStreams element) {
        elements.add(element);
    }

    @EventListener(ContextClosedEvent.class)
    public void closeAll() {
        elements.forEach(KafkaStreams::close);
    }
}
