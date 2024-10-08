package by.aurorasoft.replicator.starter;

import by.aurorasoft.replicator.annotation.service.ReplicatedService;
import by.aurorasoft.replicator.factory.producer.ReplicationProducerFactory;
import by.aurorasoft.replicator.producer.ReplicationProducer;
import by.aurorasoft.replicator.topicallocator.ReplicationTopicAllocator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static org.springframework.aop.support.AopUtils.getTargetClass;

@Component
@RequiredArgsConstructor
public final class ReplicationProducingStarter {
    private final ReplicationTopicAllocator topicAllocator;
    private final ReplicationProducerFactory producerFactory;

    public ReplicationProducer startReturningProducer(Object service) {
        ReplicatedService serviceConfig = getTargetClass(service).getAnnotation(ReplicatedService.class);
        topicAllocator.allocate(serviceConfig.topicConfig());
        return producerFactory.create(serviceConfig);
    }
}
