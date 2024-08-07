package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.registry.replicationproducer.DeleteReplicationProducerRegistry;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

@Component
public final class DeleteReplicationProducerRegistryFactory {

    public DeleteReplicationProducerRegistry create() {
        return new DeleteReplicationProducerRegistry(new LinkedHashMap<>());
    }
}
