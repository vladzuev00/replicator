package by.aurorasoft.testapp.crud.v1.mapper;

import by.aurorasoft.testapp.crud.dto.Address;
import by.aurorasoft.testapp.crud.entity.AddressEntity;
import by.nhorushko.crudgeneric.mapper.ImmutableDtoAbstractMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("deprecation")
public final class AddressMapper extends ImmutableDtoAbstractMapper<AddressEntity, Address> {

    public AddressMapper(final ModelMapper modelMapper) {
        super(AddressEntity.class, Address.class, modelMapper);
    }

    @Override
    protected Address mapDto(final AddressEntity entity) {
        return new Address(entity.getId(), entity.getCountry(), entity.getCity());
    }
}
