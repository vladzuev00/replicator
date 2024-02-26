package by.aurorasoft.testapp.crud.service;

import by.aurorasoft.replicator.annotation.ReplicatedService;
import by.aurorasoft.testapp.crud.dto.Person;
import by.aurorasoft.testapp.crud.entity.PersonEntity;
import by.aurorasoft.testapp.crud.mapper.PersonMapper;
import by.aurorasoft.testapp.crud.repository.PersonRepository;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import org.apache.kafka.common.serialization.LongSerializer;

@ReplicatedService(topicName = "sync-person", keySerializer = LongSerializer.class)
public class PersonService extends AbsServiceCRUD<Long, PersonEntity, Person, PersonRepository> {

    public PersonService(final PersonMapper mapper, final PersonRepository repository) {
        super(mapper, repository);
    }
}
