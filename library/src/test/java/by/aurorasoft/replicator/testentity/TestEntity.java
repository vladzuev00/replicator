package by.aurorasoft.replicator.testentity;

import lombok.*;

//TODO: use everywhere
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
