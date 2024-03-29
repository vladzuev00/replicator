package by.aurorasoft.replicator.model.consumed;

import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.jpa.repository.JpaRepository;

import static by.aurorasoft.replicator.util.TransportNameUtil.ENTITY_ID_NAME;

@Getter
@EqualsAndHashCode
@ToString
public final class DeleteConsumedReplication<ID, E extends AbstractEntity<ID>> implements ConsumedReplication<ID, E> {
    private final ID entityId;

    @JsonCreator
    public DeleteConsumedReplication(@JsonProperty(ENTITY_ID_NAME) final ID entityId) {
        this.entityId = entityId;
    }

    @Override
    public void execute(final JpaRepository<E, ID> repository) {
        repository.deleteById(entityId);
    }
}
