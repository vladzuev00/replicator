package by.aurorasoft.testapp.crud.v1.service;

import by.aurorasoft.testapp.crud.entity.AddressEntity;
import by.aurorasoft.testapp.crud.repository.AddressRepository;
import by.aurorasoft.testapp.crud.v1.dto.Address;
import by.aurorasoft.testapp.crud.v1.mapper.AddressV1Mapper;
import by.nhorushko.crudgeneric.service.CrudGenericService;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("deprecation")
public class AddressV1Service extends CrudGenericService<Address, AddressEntity, AddressRepository, AddressV1Mapper> {

    public AddressV1Service(final AddressRepository repository, final AddressV1Mapper mapper) {
        super(repository, mapper, Address.class, AddressEntity.class);
    }
}
