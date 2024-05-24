package by.aurorasoft.testapp.crud.v2.service;

import by.aurorasoft.replicator.annotation.ReplicatedService;
import by.aurorasoft.replicator.annotation.ReplicatedService.ProducerConfig;
import by.aurorasoft.replicator.annotation.ReplicatedService.TopicConfig;
import by.aurorasoft.testapp.crud.dto.Address;
import by.aurorasoft.testapp.crud.v2.entity.AddressEntity;
import by.aurorasoft.testapp.crud.v2.mapper.AddressMapper;
import by.aurorasoft.testapp.crud.v2.repository.AddressRepository;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import org.apache.kafka.common.serialization.LongSerializer;

@ReplicatedService(
        producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
        topicConfig = @TopicConfig(name = "sync-address")
)
public class AddressService extends AbsServiceCRUD<Long, AddressEntity, Address, AddressRepository> {

    public AddressService(final AddressMapper mapper, final AddressRepository repository) {
        super(mapper, repository);
    }
}
