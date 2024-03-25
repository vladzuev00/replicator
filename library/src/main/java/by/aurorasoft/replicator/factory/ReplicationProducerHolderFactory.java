package by.aurorasoft.replicator.factory;

import by.aurorasoft.replicator.annotation.ReplicatedService;
import by.aurorasoft.replicator.annotation.ReplicatedService.ProducerConfig;
import by.aurorasoft.replicator.annotation.ReplicatedService.TopicConfig;
import by.aurorasoft.replicator.holder.ReplicatedServiceHolder;
import by.aurorasoft.replicator.holder.ReplicationProducerHolder;
import by.aurorasoft.replicator.producer.ReplicationProducer;
import by.nhorushko.crudgeneric.v2.service.AbsServiceRUD;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Component;

import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toMap;
import static org.apache.kafka.clients.producer.ProducerConfig.*;

@Component
public final class ReplicationProducerHolderFactory {
    private final ReplicatedServiceHolder serviceHolder;
    private final String bootstrapAddress;

    public ReplicationProducerHolderFactory(final ReplicatedServiceHolder serviceHolder,
                                            @Value("${spring.kafka.bootstrap-servers}") final String bootstrapAddress) {
        this.serviceHolder = serviceHolder;
        this.bootstrapAddress = bootstrapAddress;
    }

    public ReplicationProducerHolder create() {
        return serviceHolder.getServices()
                .stream()
                .collect(collectingAndThen(toMap(identity(), this::createProducer), ReplicationProducerHolder::new));
    }

}
