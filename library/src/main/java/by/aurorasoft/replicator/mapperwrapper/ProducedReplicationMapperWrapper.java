package by.aurorasoft.replicator.mapperwrapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public final class ProducedReplicationMapperWrapper {
    private final ObjectMapper mapper;
}
