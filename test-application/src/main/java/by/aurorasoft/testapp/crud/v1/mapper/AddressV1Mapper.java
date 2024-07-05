package by.aurorasoft.testapp.crud.v1.mapper;

import by.aurorasoft.testapp.crud.entity.AddressEntity;
import by.aurorasoft.testapp.crud.v1.dto.AddressV1;
import by.nhorushko.crudgeneric.mapper.ImmutableDtoAbstractMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("deprecation")
public final class AddressV1Mapper extends ImmutableDtoAbstractMapper<AddressEntity, AddressV1> {

    public AddressV1Mapper(ModelMapper modelMapper) {
        super(AddressEntity.class, AddressV1.class, modelMapper);
    }

    @Override
    protected AddressV1 mapDto(AddressEntity entity) {
        return new AddressV1(entity.getId(), entity.getCountry(), entity.getCity());
    }
}
