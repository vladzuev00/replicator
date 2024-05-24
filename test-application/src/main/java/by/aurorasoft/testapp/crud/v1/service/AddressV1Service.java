package by.aurorasoft.testapp.crud.v1.service;

import by.aurorasoft.replicator.annotation.ReplicatedService;
import by.aurorasoft.testapp.crud.entity.AddressEntity;
import by.aurorasoft.testapp.crud.repository.AddressRepository;
import by.aurorasoft.testapp.crud.v1.dto.Address;
import by.aurorasoft.testapp.crud.v1.mapper.AddressV1Mapper;
import by.nhorushko.crudgeneric.service.CrudGenericService;
import org.apache.kafka.common.serialization.LongSerializer;

@SuppressWarnings("deprecation")
@ReplicatedService(
        producerConfig = @ReplicatedService.ProducerConfig(idSerializer = LongSerializer.class),
        topicConfig = @ReplicatedService.TopicConfig(name = "sync-address")
)
public class AddressV1Service extends CrudGenericService<Address, AddressEntity, AddressRepository, AddressV1Mapper> {

    public AddressV1Service(final AddressRepository repository, final AddressV1Mapper mapper) {
        super(repository, mapper, Address.class, AddressEntity.class);
    }
}
