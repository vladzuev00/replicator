package by.aurorasoft.testapp.crud.mapper;

import by.aurorasoft.testapp.crud.dto.Address;
import by.aurorasoft.testapp.crud.entity.AddressEntity;
import by.nhorushko.crudgeneric.v2.mapper.AbsMapperEntityDto;
import jakarta.persistence.EntityManager;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public final class AddressMapper extends AbsMapperEntityDto<AddressEntity, Address> {

    public AddressMapper(final ModelMapper modelMapper, final EntityManager entityManager) {
        super(modelMapper, entityManager, AddressEntity.class, Address.class);
    }

    @Override
    protected Address create(final AddressEntity entity) {
        return new Address(entity.getId(), entity.getCountry(), entity.getCity());
    }
}
