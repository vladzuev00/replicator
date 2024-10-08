package by.aurorasoft.testapp.crud.mapper;

import by.aurorasoft.testapp.crud.dto.Address;
import by.aurorasoft.testapp.crud.dto.Person;
import by.aurorasoft.testapp.crud.entity.PersonEntity;
import by.nhorushko.crudgeneric.v2.mapper.AbsMapperEntityDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Component
public final class PersonMapper extends AbsMapperEntityDto<PersonEntity, Person> {

    public PersonMapper(ModelMapper modelMapper, EntityManager entityManager) {
        super(modelMapper, entityManager, PersonEntity.class, Person.class);
    }

    @Override
    protected Person create(PersonEntity entity) {
        return new Person(
                entity.getId(),
                entity.getName(),
                entity.getSurname(),
                entity.getPatronymic(),
                entity.getBirthDate(),
                map(entity.getAddress(), Address.class)
        );
    }
}
