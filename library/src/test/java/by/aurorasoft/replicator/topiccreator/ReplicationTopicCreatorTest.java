package by.aurorasoft.replicator.topiccreator;

import by.aurorasoft.replicator.annotation.ReplicatedService;
import by.aurorasoft.replicator.base.AbstractSpringBootTest;
import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
import by.nhorushko.crudgeneric.v2.mapper.AbsMapperEntityDto;
import by.nhorushko.crudgeneric.v2.service.AbsServiceRUD;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.LongSerializer;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
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
        verify(kafkaAdmin, times(3)).createOrModifyTopics(topicArgumentCaptor.capture());

        final Set<NewTopic> actualCreatedTopics = new HashSet<>(topicArgumentCaptor.getAllValues());
        final Set<NewTopic> expectedCreatedTopics = Set.of(
                new NewTopic("first-topic", 1, (short) 1),
                new NewTopic("second-topic", 1, (short) 1),
                new NewTopic("third-topic", 1, (short) 1)
        );
        assertEquals(expectedCreatedTopics, actualCreatedTopics);
    }

    @ReplicatedService(topicName = "first-topic", keySerializer = LongSerializer.class)
    static class TestFirstService extends AbsServiceRUD<
            Long,
            AbstractEntity<Long>,
            AbstractDto<Long>,
            AbsMapperEntityDto<AbstractEntity<Long>, AbstractDto<Long>>,
            JpaRepository<AbstractEntity<Long>, Long>
            > {

        public TestFirstService() {
            super(null, null);
        }
    }

    @ReplicatedService(topicName = "second-topic", keySerializer = LongSerializer.class)
    static class TestSecondService extends AbsServiceRUD<
            Long,
            AbstractEntity<Long>,
            AbstractDto<Long>,
            AbsMapperEntityDto<AbstractEntity<Long>, AbstractDto<Long>>,
            JpaRepository<AbstractEntity<Long>, Long>
            > {

        public TestSecondService() {
            super(null, null);
        }
    }

    @ReplicatedService(topicName = "third-topic", keySerializer = LongSerializer.class)
    static class TestThirdService extends AbsServiceRUD<
            Long,
            AbstractEntity<Long>,
            AbstractDto<Long>,
            AbsMapperEntityDto<AbstractEntity<Long>, AbstractDto<Long>>,
            JpaRepository<AbstractEntity<Long>, Long>
            > {

        public TestThirdService() {
            super(null, null);
        }
    }
}
