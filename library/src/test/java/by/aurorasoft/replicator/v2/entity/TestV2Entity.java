package by.aurorasoft.replicator.v2.entity;

import by.nhorushko.crudgeneric.v2.domain.AbstractEntity;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public final class TestV2Entity implements AbstractEntity<Long> {
    private Long id;
}
