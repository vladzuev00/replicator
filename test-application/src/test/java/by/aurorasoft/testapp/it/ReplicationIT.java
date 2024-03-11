package by.aurorasoft.testapp.it;

import by.aurorasoft.testapp.base.AbstractSpringBootTest;
import by.aurorasoft.testapp.crud.dto.Person;
import by.aurorasoft.testapp.crud.dto.ReplicatedPerson;
import by.aurorasoft.testapp.crud.service.PersonService;
import by.aurorasoft.testapp.crud.service.ReplicatedPersonService;
import by.aurorasoft.testapp.model.PersonName;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static java.lang.Thread.currentThread;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

@Transactional(propagation = NOT_SUPPORTED)
public class ReplicationIT extends AbstractSpringBootTest {
    private static final long WAIT_REPLICATING_SECONDS = 5;

    @Autowired
    private PersonService personService;

    @Autowired
    private ReplicatedPersonService replicatedPersonService;

    @Test
    public void personAndReplicatedPersonShouldBeSaved() {
        final String givenName = "Vlad";
        final String givenSurname = "Zuev";
        final String givenPatronymic = "Sergeevich";
        final LocalDate givenBirthDate = LocalDate.of(2000, 2, 18);
        final Person givenPerson = Person.builder()
                .name(givenName)
                .surname(givenSurname)
                .patronymic(givenPatronymic)
                .birthDate(givenBirthDate)
                .build();

        final Person actualSavedPerson = personService.save(givenPerson);
        waitReplicating();

        final Long expectedSavedPersonId = 1L;
        final Person expectedSavedPerson = Person.builder()
                .id(expectedSavedPersonId)
                .name(givenName)
                .surname(givenSurname)
                .patronymic(givenPatronymic)
                .birthDate(givenBirthDate)
                .build();
        assertEquals(expectedSavedPerson, actualSavedPerson);

        final ReplicatedPerson actualReplicatedPerson = replicatedPersonService.getById(expectedSavedPersonId);
        final ReplicatedPerson expectedReplicatedPerson = ReplicatedPerson.builder()
                .id(expectedSavedPersonId)
                .name(givenName)
                .surname(givenSurname)
                .birthDate(givenBirthDate)
                .build();
        assertEquals(expectedReplicatedPerson, actualReplicatedPerson);
    }

    @Test
    @Sql(value = "classpath:sql-scripts/replication/it/delete-person.sql", executionPhase = AFTER_TEST_METHOD)
    public void personsAndReplicatedPersonsShouldBeSaved() {
        final String givenFirstPersonName = "Vlad";
        final String givenFirstPersonSurname = "Zuev";
        final String givenFirstPersonPatronymic = "Sergeevich";
        final LocalDate givenFirstPersonBirthDate = LocalDate.of(2000, 2, 18);

        final String givenSecondPersonName = "Ivan";
        final String givenSecondPersonSurname = "Ivanov";
        final String givenSecondPersonPatronymic = "Ivanovich";
        final LocalDate givenSecondPersonBirthDate = LocalDate.of(2001, 3, 19);

        final List<Person> givenPersons = List.of(
                Person.builder()
                        .name(givenFirstPersonName)
                        .surname(givenFirstPersonSurname)
                        .patronymic(givenFirstPersonPatronymic)
                        .birthDate(givenFirstPersonBirthDate)
                        .build(),
                Person.builder()
                        .name(givenSecondPersonName)
                        .surname(givenSecondPersonSurname)
                        .patronymic(givenSecondPersonPatronymic)
                        .birthDate(givenSecondPersonBirthDate)
                        .build()
        );

        final List<Person> actualSavedPersons = personService.saveAll(givenPersons);
        waitReplicating();

        final Long expectedFirstSavedPersonId = 1L;
        final Long expectedSecondSavedPersonId = 2L;
        final List<Person> expectedSavedPersons = List.of(
                Person.builder()
                        .id(expectedFirstSavedPersonId)
                        .name(givenFirstPersonName)
                        .surname(givenFirstPersonSurname)
                        .patronymic(givenFirstPersonPatronymic)
                        .birthDate(givenFirstPersonBirthDate)
                        .build(),
                Person.builder()
                        .id(expectedSecondSavedPersonId)
                        .name(givenSecondPersonName)
                        .surname(givenSecondPersonSurname)
                        .patronymic(givenSecondPersonPatronymic)
                        .birthDate(givenSecondPersonBirthDate)
                        .build()
        );
        assertEquals(expectedSavedPersons, actualSavedPersons);

        final List<ReplicatedPerson> actualReplicatedPersons = replicatedPersonService.getById(
                List.of(
                        expectedFirstSavedPersonId,
                        expectedSecondSavedPersonId
                )
        );
        final List<ReplicatedPerson> expectedReplicatedPersons = List.of(
                ReplicatedPerson.builder()
                        .id(expectedFirstSavedPersonId)
                        .name(givenFirstPersonName)
                        .surname(givenFirstPersonSurname)
                        .birthDate(givenFirstPersonBirthDate)
                        .build(),
                ReplicatedPerson.builder()
                        .id(expectedSecondSavedPersonId)
                        .name(givenSecondPersonName)
                        .surname(givenSecondPersonSurname)
                        .birthDate(givenSecondPersonBirthDate)
                        .build()
        );
        assertEquals(expectedReplicatedPersons, actualReplicatedPersons);
    }

    @Test
    @Sql("classpath:sql-scripts/replication/it/insert-person.sql")
    @Sql(value = "classpath:sql-scripts/replication/it/delete-person.sql", executionPhase = AFTER_TEST_METHOD)
    public void personAndReplicatedPersonShouldBeUpdated() {
        final Long givenId = 255L;
        final String givenNewName = "Ivan";
        final String givenNewSurname = "Ivanov";
        final String givenNewPatronymic = "Ivanovich";
        final LocalDate givenNewBirthDate = LocalDate.of(2000, 2, 19);
        final Person givenNewPerson = Person.builder()
                .id(givenId)
                .name(givenNewName)
                .surname(givenNewSurname)
                .patronymic(givenNewPatronymic)
                .birthDate(givenNewBirthDate)
                .build();

        final Person actualUpdatedPerson = personService.update(givenNewPerson);
        waitReplicating();

        assertEquals(givenNewPerson, actualUpdatedPerson);

        final ReplicatedPerson actualUpdatedReplicatedPerson = replicatedPersonService.getById(givenId);
        final ReplicatedPerson expectedUpdatedReplicatedPerson = ReplicatedPerson.builder()
                .id(givenId)
                .name(givenNewName)
                .surname(givenNewSurname)
                .birthDate(givenNewBirthDate)
                .build();
        assertEquals(expectedUpdatedReplicatedPerson, actualUpdatedReplicatedPerson);
    }

    @Test
    @Sql("classpath:sql-scripts/replication/it/insert-person.sql")
    @Sql(value = "classpath:sql-scripts/replication/it/delete-person.sql", executionPhase = AFTER_TEST_METHOD)
    public void personAndReplicatedPersonShouldBeUpdatedPartially() {
        final Long givenId = 255L;

        final String givenNewName = "Ivan";
        final String givenNewSurname = "Ivanov";
        final String givenNewPatronymic = "Ivanovich";
        final PersonName givenPartial = new PersonName(givenNewName, givenNewSurname, givenNewPatronymic);

        final Person actualUpdatedPerson = personService.updatePartial(givenId, givenPartial);
        waitReplicating();

        final LocalDate expectedBirthDate = LocalDate.of(2000, 2, 18);
        final Person expectedUpdatedPerson = Person.builder()
                .id(givenId)
                .name(givenNewName)
                .surname(givenNewSurname)
                .patronymic(givenNewPatronymic)
                .birthDate(expectedBirthDate)
                .build();
        assertEquals(expectedUpdatedPerson, actualUpdatedPerson);

        final ReplicatedPerson actualUpdatedReplicatedPerson = replicatedPersonService.getById(givenId);
        final ReplicatedPerson expectedUpdatedReplicatedPerson = ReplicatedPerson.builder()
                .id(givenId)
                .name(givenNewName)
                .surname(givenNewSurname)
                .birthDate(expectedBirthDate)
                .build();
        assertEquals(expectedUpdatedReplicatedPerson, actualUpdatedReplicatedPerson);
    }

    @Test
    @Sql("classpath:sql-scripts/replication/it/insert-person.sql")
    @Sql(value = "classpath:sql-scripts/replication/it/delete-person.sql", executionPhase = AFTER_TEST_METHOD)
    public void personAndReplicatedPersonShouldBeDeleted() {
        final Long givenId = 255L;

        personService.delete(givenId);
        waitReplicating();

        final boolean successDeleting = !personService.isExist(givenId) && !replicatedPersonService.isExist(givenId);
        assertTrue(successDeleting);
    }

    private static void waitReplicating() {
        try {
            SECONDS.sleep(WAIT_REPLICATING_SECONDS);
        } catch (final InterruptedException cause) {
            currentThread().interrupt();
        }
    }
}
