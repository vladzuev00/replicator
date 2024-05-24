package by.aurorasoft.testapp.crud.v2.mapper;

import by.aurorasoft.testapp.crud.v2.dto.AddressV2;
import by.aurorasoft.testapp.crud.v2.dto.PersonV2;
import by.aurorasoft.testapp.crud.entity.PersonEntity;
import by.nhorushko.crudgeneric.v2.mapper.AbsMapperEntityDto;
import jakarta.persistence.EntityManager;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public final class PersonV2Mapper extends AbsMapperEntityDto<PersonEntity, PersonV2> {

    public PersonV2Mapper(final ModelMapper modelMapper, final EntityManager entityManager) {
        super(modelMapper, entityManager, PersonEntity.class, PersonV2.class);
    }

    @Override
    protected PersonV2 create(final PersonEntity entity) {
        return new PersonV2(
                entity.getId(),
                entity.getName(),
                entity.getSurname(),
                entity.getPatronymic(),
                entity.getBirthDate(),
                mapAddress(entity)
        );
    }

    private AddressV2 mapAddress(final PersonEntity entity) {
        return map(entity.getAddress(), AddressV2.class);
    }
}
