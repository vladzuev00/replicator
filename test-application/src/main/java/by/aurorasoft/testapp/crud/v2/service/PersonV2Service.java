package by.aurorasoft.testapp.crud.v2.service;

import by.aurorasoft.replicator.annotation.ReplicatedService;
import by.aurorasoft.replicator.annotation.ReplicatedService.ProducerConfig;
import by.aurorasoft.replicator.annotation.ReplicatedService.TopicConfig;
import by.aurorasoft.testapp.crud.v2.dto.PersonV2;
import by.aurorasoft.testapp.crud.entity.PersonEntity;
import by.aurorasoft.testapp.crud.v2.mapper.PersonV2Mapper;
import by.aurorasoft.testapp.crud.repository.PersonRepository;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import org.apache.kafka.common.serialization.LongSerializer;

@ReplicatedService(
        producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
        topicConfig = @TopicConfig(name = "sync-person")
)
public class PersonV2Service extends AbsServiceCRUD<Long, PersonEntity, PersonV2, PersonRepository> {

    public PersonV2Service(final PersonV2Mapper mapper, final PersonRepository repository) {
        super(mapper, repository);
    }
}
