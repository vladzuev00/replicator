package by.aurorasoft.replicator.testcrud;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class TestDto {
    Long id;
    String firstProperty;
    String secondProperty;
}
