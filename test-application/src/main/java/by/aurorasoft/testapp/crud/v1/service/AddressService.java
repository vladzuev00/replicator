package by.aurorasoft.testapp.crud.v1.service;

import by.aurorasoft.testapp.crud.dto.Address;
import by.aurorasoft.testapp.crud.entity.AddressEntity;
import by.aurorasoft.testapp.crud.repository.AddressRepository;
import by.aurorasoft.testapp.crud.v1.mapper.AddressMapper;
import by.nhorushko.crudgeneric.service.ImmutableGenericService;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("deprecation")
public class AddressService extends ImmutableGenericService<Address, AddressEntity, AddressRepository, AddressMapper> {

    public AddressService(final AddressRepository repository, final AddressMapper mapper) {
        super(repository, mapper, Address.class, AddressEntity.class);
    }
}
