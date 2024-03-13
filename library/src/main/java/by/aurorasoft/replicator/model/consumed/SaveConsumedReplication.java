package by.aurorasoft.replicator.model.consumed;

import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import org.springframework.data.jpa.repository.JpaRepository;

import static by.aurorasoft.replicator.util.TransportNameUtil.SAVE_BODY;

@Value
public class SaveConsumedReplication<ID, E extends AbstractEntity<ID>> implements ConsumedReplication<ID, E> {
    E entity;

    @JsonCreator
    public SaveConsumedReplication(@JsonProperty(SAVE_BODY) final E entity) {
        this.entity = entity;
    }

    @Override
    public void execute(final JpaRepository<E, ID> repository) {
        repository.save(entity);
    }
}
