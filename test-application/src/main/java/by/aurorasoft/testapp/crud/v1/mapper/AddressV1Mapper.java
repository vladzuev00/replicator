package by.aurorasoft.testapp.crud.v1.mapper;

import by.aurorasoft.testapp.crud.entity.AddressEntity;
import by.aurorasoft.testapp.crud.v1.dto.Address;
import by.nhorushko.crudgeneric.mapper.ImmutableDtoAbstractMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("deprecation")
public final class AddressV1Mapper extends ImmutableDtoAbstractMapper<AddressEntity, Address> {

    public AddressV1Mapper(final ModelMapper modelMapper) {
        super(AddressEntity.class, Address.class, modelMapper);
    }

    @Override
    protected Address mapDto(final AddressEntity entity) {
        return new Address(entity.getId(), entity.getCountry(), entity.getCity());
    }
}
