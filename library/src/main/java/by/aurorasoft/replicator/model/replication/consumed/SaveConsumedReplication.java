package by.aurorasoft.replicator.model.replication.consumed;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.jpa.repository.JpaRepository;

import static by.aurorasoft.replicator.util.ReplicationTransportNameUtil.BODY;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class SaveConsumedReplication<E, ID> extends ConsumedReplication<E, ID> {
    private final E entity;

    @JsonCreator
    public SaveConsumedReplication(@JsonProperty(BODY) E entity) {
        this.entity = entity;
    }

    @Override
    protected void executeInternal(JpaRepository<E, ID> repository) {
        repository.save(entity);
    }
}
