package by.aurorasoft.testapp.crud.v2.mapper;

import by.aurorasoft.testapp.crud.v2.dto.Address;
import by.aurorasoft.testapp.crud.entity.AddressEntity;
import by.nhorushko.crudgeneric.v2.mapper.AbsMapperEntityDto;
import jakarta.persistence.EntityManager;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public final class AddressV2Mapper extends AbsMapperEntityDto<AddressEntity, Address> {

    public AddressV2Mapper(final ModelMapper modelMapper, final EntityManager entityManager) {
        super(modelMapper, entityManager, AddressEntity.class, Address.class);
    }

    @Override
    protected Address create(final AddressEntity entity) {
        return new Address(entity.getId(), entity.getCountry(), entity.getCity());
    }
}
