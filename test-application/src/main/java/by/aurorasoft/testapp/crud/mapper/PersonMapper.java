package by.aurorasoft.testapp.crud.mapper;

import by.aurorasoft.testapp.crud.dto.Person;
import by.aurorasoft.testapp.crud.entity.PersonEntity;
import by.nhorushko.crudgeneric.v2.mapper.AbsMapperEntityDto;
import jakarta.persistence.EntityManager;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public final class PersonMapper extends AbsMapperEntityDto<PersonEntity, Person> {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public PersonMapper(final ModelMapper modelMapper, final EntityManager entityManager) {
        super(modelMapper, entityManager, PersonEntity.class, Person.class);
    }

    @Override
    protected Person create(final PersonEntity entity) {
        return new Person(
                entity.getId(),
                entity.getName(),
                entity.getSurname(),
                entity.getPatronymic(),
                entity.getBirthDate()
        );
    }
}
