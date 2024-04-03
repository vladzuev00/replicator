package by.aurorasoft.testapp.base.containerinitializer;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.stream.Stream;

import static org.testcontainers.utility.DockerImageName.parse;

public final class DBContainerInitializer extends ContainerInitializer {
    private static final String PROPERTY_KEY_DATASOURCE_URL = "spring.datasource.url";
    private static final String PROPERTY_KEY_USERNAME = "spring.datasource.username";
    private static final String PROPERTY_KEY_PASSWORD = "spring.datasource.password";

    private static final String FULL_IMAGE_NAME = "mdillon/postgis:latest";
    private static final String OTHER_IMAGE_NAME = "postgres";
    private static final DockerImageName DOCKER_IMAGE_NAME = parse(FULL_IMAGE_NAME)
            .asCompatibleSubstituteFor(OTHER_IMAGE_NAME);

    private static final String DATA_BASE_NAME = "integration-tests-db";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "sa";

    @SuppressWarnings("resource")
    private static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>(DOCKER_IMAGE_NAME)
            .withDatabaseName(DATA_BASE_NAME)
            .withUsername(USERNAME)
            .withPassword(PASSWORD);

    static {
        POSTGRE_SQL_CONTAINER.start();
    }

    @Override
    protected Stream<TestProperty> getProperties() {
        return Stream.of(
                new TestProperty(PROPERTY_KEY_DATASOURCE_URL, POSTGRE_SQL_CONTAINER.getJdbcUrl()),
                new TestProperty(PROPERTY_KEY_USERNAME, POSTGRE_SQL_CONTAINER.getUsername()),
                new TestProperty(PROPERTY_KEY_PASSWORD, POSTGRE_SQL_CONTAINER.getPassword())
        );
    }
}
