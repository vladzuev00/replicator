package by.aurorasoft.testapp.crud.v1.mapper;

import by.aurorasoft.testapp.crud.entity.PersonEntity;
import by.aurorasoft.testapp.crud.v1.dto.AddressV1;
import by.aurorasoft.testapp.crud.v1.dto.PersonV1;
import by.nhorushko.crudgeneric.mapper.ImmutableDtoAbstractMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("deprecation")
public final class PersonV1Mapper extends ImmutableDtoAbstractMapper<PersonEntity, PersonV1> {

    public PersonV1Mapper(ModelMapper modelMapper) {
        super(PersonEntity.class, PersonV1.class, modelMapper);
    }

    @Override
    protected PersonV1 mapDto(PersonEntity entity) {
        return new PersonV1(
                entity.getId(),
                entity.getName(),
                entity.getSurname(),
                entity.getPatronymic(),
                entity.getBirthDate(),
                mapAddress(entity)
        );
    }

    private AddressV1 mapAddress(PersonEntity entity) {
        return mapper.map(entity.getAddress(), AddressV1.class);
    }
}
