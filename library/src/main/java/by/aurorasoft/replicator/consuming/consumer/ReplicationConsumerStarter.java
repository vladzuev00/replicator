package by.aurorasoft.replicator.consuming.consumer;

import by.aurorasoft.replicator.factory.ReplicationConsumerContainerFactory;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class ReplicationConsumerStarter {
    private final ReplicationConsumerContainerFactory containerFactory;

    public <ID, DTO extends AbstractDto<ID>> void start(final ReplicationConsumer<ID, DTO> consumer) {
        containerFactory.create(consumer).start();
    }
}