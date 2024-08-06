package by.aurorasoft.replicator.testentity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
@Builder
public class TestEntity {
    private Long id;
    private String firstProperty;
    private String secondProperty;
}
