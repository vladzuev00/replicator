package by.aurorasoft.replicator.consumer.starter;

import by.aurorasoft.replicator.consumer.KafkaReplicationConsumer;
import by.aurorasoft.replicator.factory.ReplicationListenerContainerFactory;
import by.aurorasoft.replicator.factory.ReplicationListenerEndpointFactory;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class KafkaReplicationConsumerStarter {
    private final ReplicationListenerContainerFactory containerFactory;
    private final ReplicationListenerEndpointFactory endpointFactory;

    public <ID, DTO extends AbstractDto<ID>> void start(final KafkaReplicationConsumer<ID, DTO> consumer) {
        containerFactory.create(consumer.getConfig())
                .createListenerContainer(endpointFactory.create(consumer))
                .start();
    }
}
