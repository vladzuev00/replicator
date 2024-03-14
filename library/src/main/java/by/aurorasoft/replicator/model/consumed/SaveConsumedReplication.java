package by.aurorasoft.replicator.model.consumed;

import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

import static by.aurorasoft.replicator.util.TransportNameUtil.BODY_NAME;
import static by.aurorasoft.replicator.util.TransportNameUtil.UUID_NAME;

public final class SaveConsumedReplication<ID, E extends AbstractEntity<ID>> extends ConsumedReplication<ID, E> {
    private final E entity;

    @JsonCreator
    public SaveConsumedReplication(@JsonProperty(UUID_NAME) final UUID uuid, @JsonProperty(BODY_NAME) final E entity) {
        super(uuid);
        this.entity = entity;
    }

    @Override
    public void execute(final JpaRepository<E, ID> repository) {
        repository.save(entity);
    }
}
