package by.aurorasoft.testapp.crud.v2.service;

import by.aurorasoft.replicator.annotation.ReplicatedService;
import by.aurorasoft.replicator.annotation.ReplicatedService.ProducerConfig;
import by.aurorasoft.replicator.annotation.ReplicatedService.TopicConfig;
import by.aurorasoft.testapp.crud.v2.dto.AddressV2;
import by.aurorasoft.testapp.crud.entity.AddressEntity;
import by.aurorasoft.testapp.crud.v2.mapper.AddressV2Mapper;
import by.aurorasoft.testapp.crud.repository.AddressRepository;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import org.apache.kafka.common.serialization.LongSerializer;

@ReplicatedService(
        producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
        topicConfig = @TopicConfig(name = "sync-address")
)
public class AddressV2Service extends AbsServiceCRUD<Long, AddressEntity, AddressV2, AddressRepository> {

    public AddressV2Service(final AddressV2Mapper mapper, final AddressRepository repository) {
        super(mapper, repository);
    }
}
