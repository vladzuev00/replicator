package by.aurorasoft.replicator.consuming.consumer;

import by.aurorasoft.replicator.factory.ReplicationConsumerContainerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class ReplicationConsumerStarter {
    private final ReplicationConsumerContainerFactory containerFactory;

    public void start(final ReplicationConsumer<?, ?> consumer) {
        containerFactory.create(consumer).start();
    }
}
