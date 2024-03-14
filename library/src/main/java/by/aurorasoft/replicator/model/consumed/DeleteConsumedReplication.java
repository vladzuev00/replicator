package by.aurorasoft.replicator.model.consumed;

import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

import static by.aurorasoft.replicator.util.TransportNameUtil.ENTITY_ID_NAME;
import static by.aurorasoft.replicator.util.TransportNameUtil.UUID_NAME;

@Getter
public final class DeleteConsumedReplication<ID, E extends AbstractEntity<ID>> extends ConsumedReplication<ID, E> {
    private final ID entityId;

    @JsonCreator
    public DeleteConsumedReplication(@JsonProperty(UUID_NAME) final UUID uuid,
                                     @JsonProperty(ENTITY_ID_NAME) final ID entityId) {
        super(uuid);
        this.entityId = entityId;
    }

    @Override
    public void execute(final JpaRepository<E, ID> repository) {
        repository.deleteById(entityId);
    }
}
