package by.aurorasoft.testapp.crud.v1.service;

import by.aurorasoft.testapp.crud.entity.PersonEntity;
import by.aurorasoft.testapp.crud.repository.PersonRepository;
import by.aurorasoft.testapp.crud.v1.dto.Person;
import by.aurorasoft.testapp.crud.v1.mapper.PersonV1Mapper;
import by.nhorushko.crudgeneric.service.ImmutableGenericService;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("deprecation")
public class PersonV1Service extends ImmutableGenericService<Person, PersonEntity, PersonRepository, PersonV1Mapper> {

    public PersonV1Service(final PersonRepository repository, final PersonV1Mapper mapper) {
        super(repository, mapper, Person.class, PersonEntity.class);
    }
}
