package by.aurorasoft.replicator.model.setting;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

@RequiredArgsConstructor
@Getter
public abstract class ReplicationSetting<E, ID> {
    private final String topic;
    private final JpaRepository<E, ID> repository;
}
