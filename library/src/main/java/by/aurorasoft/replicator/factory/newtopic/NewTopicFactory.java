package by.aurorasoft.replicator.factory.newtopic;

import by.aurorasoft.replicator.annotation.service.ReplicatedService.TopicConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.stereotype.Component;

@Component
public final class NewTopicFactory {

    public NewTopic create(TopicConfig config) {
        return new NewTopic(config.name(), config.partitionCount(), config.replicationFactor());
    }
}
