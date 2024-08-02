package by.aurorasoft.testapp.crud.repository;

import by.aurorasoft.replicator.annotation.ReplicatedRepository;
import by.aurorasoft.replicator.annotation.ReplicatedRepository.Producer;
import by.aurorasoft.replicator.annotation.ReplicatedRepository.Topic;
import by.aurorasoft.testapp.crud.entity.AddressEntity;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.data.jpa.repository.JpaRepository;

@ReplicatedRepository(producer = @Producer(idSerializer = LongSerializer.class), topic = @Topic(name = "address-sync"))
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {

}
