package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.holder.ReplicatedServiceHolder;
import by.aurorasoft.replicator.holder.ReplicationProducerHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toMap;

@Component
@RequiredArgsConstructor
public final class ReplicationProducerHolderFactory {
    private final ReplicatedServiceHolder serviceHolder;
    private final ReplicationProducerFactory producerFactory;

    public ReplicationProducerHolder create() {
        return serviceHolder.getServices()
                .stream()
                .collect(collectingAndThen(toMap(identity(), producerFactory::create), ReplicationProducerHolder::new));
    }

}
