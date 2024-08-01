package by.aurorasoft.replicator.testrepository;

import by.aurorasoft.replicator.annotation.ReplicatedRepository;
import by.aurorasoft.replicator.annotation.ReplicatedRepository.Producer;
import by.aurorasoft.replicator.annotation.ReplicatedRepository.Topic;
import org.apache.kafka.common.serialization.LongSerializer;

@ReplicatedRepository(producer = @Producer(idSerializer = LongSerializer.class), topic = @Topic(name = "first-topic"))
public class FirstTestRepository extends TestRepository {

}
