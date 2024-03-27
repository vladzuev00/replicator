package by.aurorasoft.replicator.model.consumed;

import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.jpa.repository.JpaRepository;

import static by.aurorasoft.replicator.util.TransportNameUtil.BODY_NAME;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class SaveConsumedReplication<ID, E extends AbstractEntity<ID>> extends ConsumedReplication<ID, E> {
    private final E entity;

    @JsonCreator
    public SaveConsumedReplication(@JsonProperty(BODY_NAME) final E entity) {
        this.entity = entity;
    }

    @Override
    protected void executeInternal(final JpaRepository<E, ID> repository) {
        repository.save(entity);
    }
}
