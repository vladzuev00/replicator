package by.aurorasoft.replicator.base.v2.entity;

import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public final class TestEntity implements AbstractEntity<Long> {
    private Long id;
}
