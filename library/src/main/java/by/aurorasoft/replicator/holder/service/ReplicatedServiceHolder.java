package by.aurorasoft.replicator.holder.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Getter
public final class ReplicatedServiceHolder {
    private final List<Object> services;
}
