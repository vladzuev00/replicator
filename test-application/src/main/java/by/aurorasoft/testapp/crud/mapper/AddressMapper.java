package by.aurorasoft.testapp.crud.mapper;

import by.aurorasoft.testapp.crud.dto.Address;
import by.aurorasoft.testapp.crud.entity.AddressEntity;
import by.nhorushko.crudgeneric.v2.mapper.AbsMapperEntityDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Component
public final class AddressMapper extends AbsMapperEntityDto<AddressEntity, Address> {

    public AddressMapper(ModelMapper modelMapper, EntityManager entityManager) {
        super(modelMapper, entityManager, AddressEntity.class, Address.class);
    }

    @Override
    protected Address create(AddressEntity from) {
        return null;
    }
}
