package by.aurorasoft.replicator.model.config.component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

@RequiredArgsConstructor
@Getter
public abstract class ReplicationComponentConfig<E, ID> {
    private final String topic;
    private final JpaRepository<E, ID> repository;
}
