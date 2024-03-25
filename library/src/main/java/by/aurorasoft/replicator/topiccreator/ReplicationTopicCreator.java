package by.aurorasoft.replicator.topiccreator;

import by.aurorasoft.replicator.factory.ReplicationTopicFactory;
import by.aurorasoft.replicator.holder.ReplicatedServiceHolder;
import by.nhorushko.crudgeneric.v2.service.AbsServiceRUD;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class ReplicationTopicCreator {
    private final ReplicatedServiceHolder serviceHolder;
    private final ReplicationTopicFactory topicFactory;
    private final KafkaAdmin kafkaAdmin;

    @PostConstruct
    public void createTopics() {
        serviceHolder.getServices().forEach(this::createTopic);
    }

    private void createTopic(final AbsServiceRUD<?, ?, ?, ?, ?> service) {
        final NewTopic topic = topicFactory.create(service);
        kafkaAdmin.createOrModifyTopics(topic);
    }
}
