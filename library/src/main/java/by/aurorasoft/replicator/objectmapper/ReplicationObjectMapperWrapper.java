package by.aurorasoft.replicator.objectmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public final class ReplicationObjectMapperWrapper {
    private final ObjectMapper mapper;
}
