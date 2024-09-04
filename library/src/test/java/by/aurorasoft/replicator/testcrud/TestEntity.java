package by.aurorasoft.replicator.testcrud;

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
