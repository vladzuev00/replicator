package by.aurorasoft.testapp.crud.v2.mapper;

import by.aurorasoft.testapp.crud.v2.dto.AddressV2;
import by.aurorasoft.testapp.crud.entity.AddressEntity;
import by.nhorushko.crudgeneric.v2.mapper.AbsMapperEntityDto;
import jakarta.persistence.EntityManager;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public final class AddressV2Mapper extends AbsMapperEntityDto<AddressEntity, AddressV2> {

    public AddressV2Mapper(final ModelMapper modelMapper, final EntityManager entityManager) {
        super(modelMapper, entityManager, AddressEntity.class, AddressV2.class);
    }

    @Override
    protected AddressV2 create(final AddressEntity entity) {
        return new AddressV2(entity.getId(), entity.getCountry(), entity.getCity());
    }
}
