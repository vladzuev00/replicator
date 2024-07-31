package by.aurorasoft.replicator.testentity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public final class TestEntity {
    private Long id;
    private String name;
    private String language;
}
