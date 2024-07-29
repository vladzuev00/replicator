package by.aurorasoft.replicator.objectmapper;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class ReplicationObjectMapper extends ObjectMapper {

    public ReplicationObjectMapper(ObjectMapper source) {
        super(source);
    }
}
