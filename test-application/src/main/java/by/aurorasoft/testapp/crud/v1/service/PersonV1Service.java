package by.aurorasoft.testapp.crud.v1.service;

import by.aurorasoft.replicator.annotation.ReplicatedRepository;
import by.aurorasoft.replicator.annotation.ReplicatedRepository.Producer;
import by.aurorasoft.replicator.annotation.ReplicatedRepository.Topic;
import by.aurorasoft.replicator.annotation.ReplicatedRepository.EntityView;
import by.aurorasoft.testapp.crud.entity.PersonEntity;
import by.aurorasoft.testapp.crud.repository.PersonRepository;
import by.aurorasoft.testapp.crud.v1.dto.PersonV1;
import by.aurorasoft.testapp.crud.v1.mapper.PersonV1Mapper;
import by.nhorushko.crudgeneric.service.CrudGenericService;
import org.apache.kafka.common.serialization.LongSerializer;

@SuppressWarnings("deprecation")
@ReplicatedRepository(
        producer = @Producer(idSerializer = LongSerializer.class),
        topicConfig = @Topic(name = "sync-person"),
        entityViews = @EntityView(type = PersonV1.class, includedFields = "surname")
)
public class PersonV1Service extends CrudGenericService<PersonV1, PersonEntity, PersonRepository, PersonV1Mapper> {

    public PersonV1Service(PersonRepository repository, PersonV1Mapper mapper) {
        super(repository, mapper, PersonV1.class, PersonEntity.class);
    }
}
