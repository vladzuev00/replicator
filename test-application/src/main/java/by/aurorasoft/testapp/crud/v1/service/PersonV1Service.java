package by.aurorasoft.testapp.crud.v1.service;

import by.aurorasoft.replicator.annotation.ReplicatedService;
import by.aurorasoft.replicator.annotation.ReplicatedService.ProducerConfig;
import by.aurorasoft.replicator.annotation.ReplicatedService.TopicConfig;
import by.aurorasoft.testapp.crud.entity.PersonEntity;
import by.aurorasoft.testapp.crud.repository.PersonRepository;
import by.aurorasoft.testapp.crud.v1.dto.PersonV1;
import by.aurorasoft.testapp.crud.v1.mapper.PersonV1Mapper;
import by.nhorushko.crudgeneric.service.CrudGenericService;
import org.apache.kafka.common.serialization.LongSerializer;

@SuppressWarnings("deprecation")
@ReplicatedService(
        producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
        topicConfig = @TopicConfig(name = "sync-person")
)
public class PersonV1Service extends CrudGenericService<PersonV1, PersonEntity, PersonRepository, PersonV1Mapper> {

    public PersonV1Service(PersonRepository repository, PersonV1Mapper mapper) {
        super(repository, mapper, PersonV1.class, PersonEntity.class);
    }
}
