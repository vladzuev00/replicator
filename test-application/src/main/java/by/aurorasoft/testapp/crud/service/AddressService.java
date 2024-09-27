package by.aurorasoft.testapp.crud.service;

import by.aurorasoft.replicator.annotation.operation.ReplicatedDeleteById;
import by.aurorasoft.replicator.annotation.operation.ReplicatedSave;
import by.aurorasoft.replicator.annotation.operation.ReplicatedSaveAll;
import by.aurorasoft.replicator.annotation.service.ReplicatedService;
import by.aurorasoft.replicator.annotation.service.ReplicatedService.DtoViewConfig;
import by.aurorasoft.replicator.annotation.service.ReplicatedService.ProducerConfig;
import by.aurorasoft.replicator.annotation.service.ReplicatedService.TopicConfig;
import by.aurorasoft.testapp.crud.dto.Address;
import by.aurorasoft.testapp.crud.entity.AddressEntity;
import by.aurorasoft.testapp.crud.mapper.AddressMapper;
import by.aurorasoft.testapp.crud.repository.AddressRepository;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import org.apache.kafka.common.serialization.LongSerializer;

import java.util.Collection;
import java.util.List;

import static by.aurorasoft.testapp.crud.dto.Address.Fields.country;

@ReplicatedService(
        producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
        topicConfig = @TopicConfig(name = "address-sync"),
        dtoViewConfigs = @DtoViewConfig(type = Address.class, includedFields = country)
)
public class AddressService extends AbsServiceCRUD<Long, AddressEntity, Address, AddressRepository> {

    public AddressService(AddressMapper mapper, AddressRepository repository) {
        super(mapper, repository);
    }

    @Override
    @ReplicatedSave
    public Address save(Address address) {
        return super.save(address);
    }

    @Override
    @ReplicatedSaveAll
    public List<Address> saveAll(Collection<Address> addresses) {
        return super.saveAll(addresses);
    }

    @Override
    @ReplicatedDeleteById
    public void delete(Long id) {
        super.delete(id);
    }
}
