package by.aurorasoft.replicator.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import static by.aurorasoft.replicator.util.TransportNameUtil.UUID_NAME;

@RequiredArgsConstructor
@Getter
public abstract class Replication {

    @JsonProperty(UUID_NAME)
    private final UUID uuid;
}
