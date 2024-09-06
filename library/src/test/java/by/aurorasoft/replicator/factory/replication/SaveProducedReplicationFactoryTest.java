package by.aurorasoft.replicator.factory.replication;

import by.aurorasoft.replicator.annotation.service.ReplicatedService.DtoViewConfig;
import by.aurorasoft.replicator.model.replication.produced.SaveProducedReplication;
import by.aurorasoft.replicator.model.view.DtoJsonView;
import by.aurorasoft.replicator.testcrud.TestDto;
import org.junit.jupiter.api.Test;

import static by.aurorasoft.replicator.testutil.DtoViewConfigUtil.createDtoViewConfig;
import static com.monitorjbl.json.Match.match;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class SaveProducedReplicationFactoryTest {
    private final SaveProducedReplicationFactory factory = new SaveProducedReplicationFactory();

    @Test
    public void replicationShouldBeCreated() {
        TestDto givenDto = TestDto.builder().build();
        String givenIncludedField = "firstProperty";
        String givenExcludedField = "secondProperty";
        DtoViewConfig[] givenDtoViewConfigs = {
                createDtoViewConfig(TestDto.class, new String[]{givenIncludedField}, new String[]{givenExcludedField})
        };

        SaveProducedReplication actual = factory.create(givenDto, givenDtoViewConfigs);
        DtoJsonView<TestDto> expectedView = new DtoJsonView<>(givenDto);
        expectedView.onClass(TestDto.class, match().include(givenIncludedField).exclude(givenExcludedField));
        SaveProducedReplication expected = new SaveProducedReplication(expectedView);
        assertEquals(expected, actual);

        Object actualDto = actual.getBody().getDto();
        assertSame(givenDto, actualDto);
    }
}
