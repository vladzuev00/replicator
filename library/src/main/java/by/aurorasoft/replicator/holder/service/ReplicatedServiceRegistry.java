package by.aurorasoft.replicator.holder.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
@Getter
public final class ReplicatedServiceRegistry {
    private final Set<Object> services;
}
