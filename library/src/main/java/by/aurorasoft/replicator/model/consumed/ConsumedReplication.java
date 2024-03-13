package by.aurorasoft.replicator.model.consumed;

import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@JsonTypeInfo(use = NAME, property = "type")
@JsonSubTypes(
        {
                @Type(value = SaveConsumedReplication.class, name = "save"),
                @Type(value = DeleteConsumedReplication.class, name = "delete")
        }
)
public interface ConsumedReplication<ID, E extends AbstractEntity<ID>> {
    void execute(final JpaRepository<E, ID> repository);
}
