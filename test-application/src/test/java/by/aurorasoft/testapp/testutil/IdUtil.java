package by.aurorasoft.testapp.testutil;

import by.nhorushko.crudgeneric.v2.domain.IdEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public final class IdUtil {

    public static <ID> List<ID> mapToIds(final List<? extends IdEntity<ID>> sources) {
        return sources.stream()
                .map(IdEntity::getId)
                .toList();
    }
}
