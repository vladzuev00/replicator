package by.aurorasoft.replicator.mapperwrapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public final class ReplicationObjectMapperWrapper {
    private final ObjectMapper mapper;
}
