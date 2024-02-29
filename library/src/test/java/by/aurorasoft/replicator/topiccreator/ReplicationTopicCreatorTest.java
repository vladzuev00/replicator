package by.aurorasoft.replicator.topiccreator;

import by.aurorasoft.replicator.base.AbstractSpringBootTest;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public final class ReplicationTopicCreatorTest extends AbstractSpringBootTest {

    @Autowired
    private KafkaAdmin kafkaAdmin;

    @Captor
    private ArgumentCaptor<NewTopic> topicArgumentCaptor;

    @Test
    public void topicsShouldBeCreated() {
        verify(kafkaAdmin, times(2)).createOrModifyTopics(topicArgumentCaptor.capture());

        final Set<NewTopic> actualCreatedTopics = new HashSet<>(topicArgumentCaptor.getAllValues());
        final Set<NewTopic> expectedCreatedTopics = Set.of(
                new NewTopic("first-topic", 1, (short) 1),
                new NewTopic("second-topic", 2, (short) 2)
        );
        assertEquals(expectedCreatedTopics, actualCreatedTopics);
    }
}
