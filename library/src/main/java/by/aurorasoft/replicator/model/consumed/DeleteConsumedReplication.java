package by.aurorasoft.replicator.model.consumed;

import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import org.springframework.data.jpa.repository.JpaRepository;

import static by.aurorasoft.replicator.util.TransportNameUtil.DELETE_ENTITY_ID;

@Value
public class DeleteConsumedReplication<ID, E extends AbstractEntity<ID>> implements ConsumedReplication<ID, E> {
    ID entityId;

    @JsonCreator
    public DeleteConsumedReplication(@JsonProperty(DELETE_ENTITY_ID) final ID entityId) {
        this.entityId = entityId;
    }

    @Override
    public void execute(final JpaRepository<E, ID> repository) {
        repository.deleteById(entityId);
    }
}
