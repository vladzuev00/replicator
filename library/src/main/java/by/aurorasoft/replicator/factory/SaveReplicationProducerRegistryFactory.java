package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.registry.replicationproducer.SaveReplicationProducerRegistry;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class SaveReplicationProducerRegistryFactory {

    public SaveReplicationProducerRegistry create() {
        return new SaveReplicationProducerRegistry(new HashMap<>());
    }
}
