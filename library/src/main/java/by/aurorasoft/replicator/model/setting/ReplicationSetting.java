package by.aurorasoft.replicator.model.setting;

import lombok.Getter;
import org.springframework.data.jpa.repository.JpaRepository;

import static java.util.Objects.requireNonNull;

//TODO: remove
@Getter
public abstract class ReplicationSetting<E, ID> {
    private final String topic;
    private final JpaRepository<E, ID> repository;

    public ReplicationSetting(String topic, JpaRepository<E, ID> repository) {
        this.topic = requireNonNull(topic);
        this.repository = requireNonNull(repository);
    }
}
