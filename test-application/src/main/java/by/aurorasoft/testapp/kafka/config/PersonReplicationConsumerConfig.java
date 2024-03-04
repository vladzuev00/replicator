package by.aurorasoft.testapp.kafka.config;

import by.aurorasoft.replicator.consumer.KafkaReplicationConsumerConfig;
import by.aurorasoft.testapp.crud.dto.ReplicatedPerson;
import by.aurorasoft.testapp.crud.service.ReplicatedPersonService;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.springframework.stereotype.Component;

@Component
public final class PersonReplicationConsumerConfig extends KafkaReplicationConsumerConfig<Long, ReplicatedPerson> {

    public PersonReplicationConsumerConfig(ReplicatedPersonService service) {
        super("sync-person-group", "sync-person", new LongDeserializer(), ReplicatedPerson.class, service);
    }

}
