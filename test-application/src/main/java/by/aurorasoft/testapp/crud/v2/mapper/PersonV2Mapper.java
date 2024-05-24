package by.aurorasoft.testapp.crud.v2.mapper;

import by.aurorasoft.testapp.crud.v2.dto.Address;
import by.aurorasoft.testapp.crud.v2.dto.Person;
import by.aurorasoft.testapp.crud.entity.PersonEntity;
import by.nhorushko.crudgeneric.v2.mapper.AbsMapperEntityDto;
import jakarta.persistence.EntityManager;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public final class PersonV2Mapper extends AbsMapperEntityDto<PersonEntity, Person> {

    public PersonV2Mapper(final ModelMapper modelMapper, final EntityManager entityManager) {
        super(modelMapper, entityManager, PersonEntity.class, Person.class);
    }

    @Override
    protected Person create(final PersonEntity entity) {
        return new Person(
                entity.getId(),
                entity.getName(),
                entity.getSurname(),
                entity.getPatronymic(),
                entity.getBirthDate(),
                mapAddress(entity)
        );
    }

    private Address mapAddress(final PersonEntity entity) {
        return map(entity.getAddress(), Address.class);
    }
}
