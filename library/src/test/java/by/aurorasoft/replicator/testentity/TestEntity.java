package by.aurorasoft.replicator.testentity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
@Builder
public final class TestEntity {
    private Long id;
    private String firstProperty;
    private String secondProperty;
}
