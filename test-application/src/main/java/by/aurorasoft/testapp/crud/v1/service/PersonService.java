package by.aurorasoft.testapp.crud.v1.service;

import by.aurorasoft.testapp.crud.dto.Person;
import by.aurorasoft.testapp.crud.entity.PersonEntity;
import by.aurorasoft.testapp.crud.repository.PersonRepository;
import by.aurorasoft.testapp.crud.v1.mapper.PersonMapper;
import by.nhorushko.crudgeneric.service.ImmutableGenericService;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("deprecation")
public class PersonService extends ImmutableGenericService<Person, PersonEntity, PersonRepository, PersonMapper> {

    public PersonService(final PersonRepository repository, final PersonMapper mapper) {
        super(repository, mapper, Person.class, PersonEntity.class);
    }
}
