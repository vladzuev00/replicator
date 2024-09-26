package by.aurorasoft.testapp.crud.service;

import by.aurorasoft.replicator.annotation.operation.ReplicatedSave;
import by.aurorasoft.replicator.annotation.service.ReplicatedService;
import by.aurorasoft.replicator.annotation.service.ReplicatedService.DtoViewConfig;
import by.aurorasoft.replicator.annotation.service.ReplicatedService.ProducerConfig;
import by.aurorasoft.replicator.annotation.service.ReplicatedService.TopicConfig;
import by.aurorasoft.testapp.crud.dto.Person;
import by.aurorasoft.testapp.crud.entity.PersonEntity;
import by.aurorasoft.testapp.crud.mapper.PersonMapper;
import by.aurorasoft.testapp.crud.repository.PersonRepository;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import org.apache.kafka.common.serialization.LongSerializer;

import static by.aurorasoft.testapp.crud.dto.Person.Fields.address;
import static by.aurorasoft.testapp.crud.dto.Person.Fields.birthDate;

@ReplicatedService(
        producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
        topicConfig = @TopicConfig(name = "person-sync"),
        dtoViewConfigs = @DtoViewConfig(type = Person.class, includedFields = {birthDate, address})
)
public class PersonService extends AbsServiceCRUD<Long, PersonEntity, Person, PersonRepository> {

    public PersonService(PersonMapper mapper, PersonRepository repository) {
        super(mapper, repository);
    }

    @Override
    @ReplicatedSave
    public Person save(Person person) {
        return super.save(person);
    }
}
