package by.aurorasoft.testapp.crud.v1.service;

import by.aurorasoft.replicator.annotation.ReplicatedService;
import by.aurorasoft.replicator.annotation.ReplicatedService.ProducerConfig;
import by.aurorasoft.replicator.annotation.ReplicatedService.TopicConfig;
import by.aurorasoft.testapp.crud.entity.AddressEntity;
import by.aurorasoft.testapp.crud.repository.AddressRepository;
import by.aurorasoft.testapp.crud.v1.dto.AddressV1;
import by.aurorasoft.testapp.crud.v1.mapper.AddressV1Mapper;
import by.nhorushko.crudgeneric.service.CrudGenericService;
import org.apache.kafka.common.serialization.LongSerializer;

@SuppressWarnings("deprecation")
@ReplicatedService(
        producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
        topicConfig = @TopicConfig(name = "sync-address")
)
public class AddressV1Service extends CrudGenericService<AddressV1, AddressEntity, AddressRepository, AddressV1Mapper> {

    public AddressV1Service(AddressRepository repository, AddressV1Mapper mapper) {
        super(repository, mapper, AddressV1.class, AddressEntity.class);
    }
}
