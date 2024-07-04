package by.aurorasoft.replicator.registry.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
@Getter
public final class ReplicatedServiceRegistry {
    private final Set<Object> services;
}
