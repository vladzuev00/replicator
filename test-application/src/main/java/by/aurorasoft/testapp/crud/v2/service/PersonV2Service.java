package by.aurorasoft.testapp.crud.v2.service;

import by.aurorasoft.replicator.annotation.ReplicatedRepository;
import by.aurorasoft.replicator.annotation.ReplicatedRepository.Producer;
import by.aurorasoft.replicator.annotation.ReplicatedRepository.Topic;
import by.aurorasoft.testapp.crud.v2.dto.PersonV2;
import by.aurorasoft.testapp.crud.entity.PersonEntity;
import by.aurorasoft.testapp.crud.v2.mapper.PersonV2Mapper;
import by.aurorasoft.testapp.crud.repository.PersonRepository;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import org.apache.kafka.common.serialization.LongSerializer;

@ReplicatedRepository(
        producer = @Producer(idSerializer = LongSerializer.class),
        topic = @Topic(name = "sync-person")
)
public class PersonV2Service extends AbsServiceCRUD<Long, PersonEntity, PersonV2, PersonRepository> {

    public PersonV2Service(PersonV2Mapper mapper, PersonRepository repository) {
        super(mapper, repository);
    }
}
