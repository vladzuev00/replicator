package by.aurorasoft.replicator.holder.producer;

import by.aurorasoft.replicator.holder.producer.ReplicationProducerFactory;
import by.aurorasoft.replicator.holder.service.ReplicatedServiceHolder;
import by.aurorasoft.replicator.holder.producer.ReplicationProducerHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static java.util.stream.Collectors.toMap;

@Component
@RequiredArgsConstructor
public final class ReplicationProducerHolderFactory {
    private final ReplicatedServiceHolder serviceHolder;
    private final ReplicationProducerFactory producerFactory;

    public ReplicationProducerHolder create() {
//        return serviceHolder.getServices()
//                .stream()
//                .collect(collectingAndThen(toMap(identity(), producerFactory::create), ReplicationProducerHolder::new));
        return null;
    }

}
