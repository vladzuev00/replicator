package by.aurorasoft.replicator.registry;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

//TODO: remove
@RequiredArgsConstructor
@Getter
public final class ReplicatedRepositoryRegistry {
    private final Set<JpaRepository<?, ?>> repositories;
}
