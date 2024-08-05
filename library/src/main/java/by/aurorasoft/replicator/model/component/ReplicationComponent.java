package by.aurorasoft.replicator.model.component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

@RequiredArgsConstructor
@Getter
public abstract class ReplicationComponent<E, ID> {
    private final String topic;
    private final JpaRepository<E, ID> repository;
}
