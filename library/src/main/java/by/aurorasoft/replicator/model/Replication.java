package by.aurorasoft.replicator.model;

import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import by.nhorushko.crudgeneric.v2.service.AbsServiceCRUD;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@JsonTypeInfo(use= NAME)
@JsonSubTypes({@Type(SaveReplication.class), @Type(UpdateReplication.class), @Type(DeleteReplication.class),})
public interface Replication<ID, DTO extends AbstractDto<ID>> {
    ID getEntityId();

    void execute(final AbsServiceCRUD<ID, ?, DTO, ?> service);
}
