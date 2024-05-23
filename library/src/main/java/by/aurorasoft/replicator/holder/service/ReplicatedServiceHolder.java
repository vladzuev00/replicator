package by.aurorasoft.replicator.holder.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public final class ReplicatedServiceHolder {
    private final List<Object> services;
}
