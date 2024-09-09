package by.aurorasoft.replicator.topicallocator;

import by.aurorasoft.replicator.annotation.service.ReplicatedService.TopicConfig;
import by.aurorasoft.replicator.factory.newtopic.NewTopicFactory;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;

//TODO
@Component
@RequiredArgsConstructor
public final class ReplicationTopicAllocator {
    private final NewTopicFactory topicFactory;
    private final KafkaAdmin kafkaAdmin;

    public void allocate(TopicConfig config) {
        NewTopic topic = topicFactory.create(config);
        kafkaAdmin.createOrModifyTopics(topic);
    }
}
