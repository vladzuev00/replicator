package by.aurorasoft.replicator.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.UUID;

import static by.aurorasoft.replicator.util.TransportNameUtil.UUID_NAME;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public abstract class Replication {

    @JsonProperty(UUID_NAME)
    private final UUID uuid;
}
