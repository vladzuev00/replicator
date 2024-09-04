package by.aurorasoft.replicator.testcrud;

import by.aurorasoft.replicator.annotation.operation.*;
import by.aurorasoft.replicator.annotation.service.ReplicatedService;
import by.aurorasoft.replicator.annotation.service.ReplicatedService.ProducerConfig;
import by.aurorasoft.replicator.annotation.service.ReplicatedService.TopicConfig;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

@ReplicatedService(
        producerConfig = @ProducerConfig(idSerializer = LongSerializer.class),
        topicConfig = @TopicConfig(name = "sync-dto")
)
@RequiredArgsConstructor
public class TestService {
    private final TestRepository repository;

    @ReplicatedSave
    public TestDto save(TestDto dto) {
        return dto;
    }

    @ReplicatedSaveAll
    public List<TestDto> saveAll(Iterable<TestDto> dtos) {
        List<TestDto> result = new ArrayList<>();
        dtos.forEach(result::add);
        return result;
    }

    @ReplicatedDeleteById
    public void deleteById(@SuppressWarnings("unused") Long id) {

    }

    @ReplicatedDelete
    public void delete(@SuppressWarnings("unused") TestDto dto) {

    }

    @ReplicatedDeleteByIds
    public void deleteByIds(@SuppressWarnings("unused") Iterable<Long> ids) {

    }

    @ReplicatedDeleteIterable
    public void delete(@SuppressWarnings("unused") Iterable<TestDto> dtos) {

    }

    @ReplicatedDeleteAll
    public void deleteAll() {
        
    }
}
