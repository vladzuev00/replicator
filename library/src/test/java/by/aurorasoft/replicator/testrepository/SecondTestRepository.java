package by.aurorasoft.replicator.testrepository;

import by.aurorasoft.replicator.annotation.ReplicatedRepository;
import by.aurorasoft.replicator.annotation.ReplicatedRepository.EntityView;
import by.aurorasoft.replicator.annotation.ReplicatedRepository.Producer;
import by.aurorasoft.replicator.annotation.ReplicatedRepository.Topic;
import by.aurorasoft.replicator.testentity.TestEntity;
import org.apache.kafka.common.serialization.LongSerializer;

@ReplicatedRepository(
        producer = @Producer(
                idSerializer = LongSerializer.class,
                batchSize = 15,
                lingerMs = 515,
                deliveryTimeoutMs = 110000
        ),
        topic = @Topic(name = "second-topic", partitionCount = 2, replicationFactor = 2),
        entityViews = {
                @EntityView(type = TestEntity.class, excludedFields = "first-field"),
                @EntityView(type = TestEntity.class, excludedFields = {"second-field", "third-field", "fourth-field"})
        }
)
public class SecondTestRepository extends TestRepository {

}
