package by.aurorasoft.testapp.crud.v1.mapper;

import by.aurorasoft.testapp.crud.entity.PersonEntity;
import by.aurorasoft.testapp.crud.v1.dto.Address;
import by.aurorasoft.testapp.crud.v1.dto.Person;
import by.nhorushko.crudgeneric.mapper.ImmutableDtoAbstractMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("deprecation")
public final class PersonV1Mapper extends ImmutableDtoAbstractMapper<PersonEntity, Person> {

    public PersonV1Mapper(final ModelMapper modelMapper) {
        super(PersonEntity.class, Person.class, modelMapper);
    }

    @Override
    protected Person mapDto(final PersonEntity entity) {
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
        return mapper.map(entity.getAddress(), Address.class);
    }
}
