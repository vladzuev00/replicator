package by.aurorasoft.replicator.base.v1.entity;

import by.nhorushko.crudgeneric.domain.AbstractEntity;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
@SuppressWarnings("deprecation")
public final class TestEntity implements AbstractEntity {
    private Long id;
}
