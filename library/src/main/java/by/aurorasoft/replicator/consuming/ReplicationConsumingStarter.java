package by.aurorasoft.replicator.consuming;

import by.aurorasoft.replicator.consuming.consumer.ReplicationConsumingPipelineStarter;
import by.aurorasoft.replicator.consuming.consumer.ReplicationConsumerStarter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public final class ReplicationConsumingStarter {
    private final List<ReplicationConsumingPipeline<?, ?>> pipelines;



    //TODO: StreamsBuilder streamsBuilder
    private final List<ReplicationConsumingPipelineStarter<?, ?>> consumers;
    private final ReplicationConsumerStarter consumerStarter;

    @EventListener(ContextRefreshedEvent.class)
    public void start() {
        consumers.forEach(consumerStarter::start);
    }
}
