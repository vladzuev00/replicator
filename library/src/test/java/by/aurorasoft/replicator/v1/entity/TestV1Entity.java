package by.aurorasoft.replicator.v1.entity;

import by.nhorushko.crudgeneric.domain.AbstractEntity;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
@SuppressWarnings("deprecation")
public final class TestV1Entity implements AbstractEntity {
    private Long id;
}
