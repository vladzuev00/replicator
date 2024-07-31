package by.aurorasoft.testapp.crud.v2.service;

import by.aurorasoft.replicator.annotation.ReplicatedRepository;
import by.aurorasoft.replicator.annotation.ReplicatedRepository.Producer;
import by.aurorasoft.replicator.annotation.ReplicatedRepository.Topic;
import by.aurorasoft.testapp.crud.v2.dto.AddressV2;
import by.aurorasoft.testapp.crud.entity.AddressEntity;
import by.aurorasoft.testapp.crud.v2.mapper.AddressV2Mapper;
import by.aurorasoft.testapp.crud.repository.AddressRepository;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import org.apache.kafka.common.serialization.LongSerializer;

@ReplicatedRepository(
        producer = @Producer(idSerializer = LongSerializer.class),
        topicConfig = @Topic(name = "sync-address")
)
public class AddressV2Service extends AbsServiceCRUD<Long, AddressEntity, AddressV2, AddressRepository> {

    public AddressV2Service(AddressV2Mapper mapper, AddressRepository repository) {
        super(mapper, repository);
    }
}
