package by.aurorasoft.replicator.model.replication.consumed;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.jpa.repository.JpaRepository;

import static by.aurorasoft.replicator.util.TransportNameUtil.BODY;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class DeleteConsumedReplication<E, ID> extends ConsumedReplication<E, ID> {
    private final ID entityId;

    @JsonCreator
    public DeleteConsumedReplication(@JsonProperty(BODY) ID entityId) {
        this.entityId = entityId;
    }

    @Override
    protected void executeInternal(JpaRepository<E, ID> repository) {
        repository.deleteById(entityId);
    }
}
