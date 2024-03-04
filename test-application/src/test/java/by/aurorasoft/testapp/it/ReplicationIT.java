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
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

public class ReplicationIT extends AbstractSpringBootTest {

    @Autowired
    private PersonService personService;

    @Autowired
    private ReplicatedPersonService replicatedPersonService;

//    @Autowired
//    private KafkaPersonReplicationConsumer replicationConsumer;

    @Test
    @Transactional(propagation = NOT_SUPPORTED)
    @Sql(value = "classpath:sql-scripts/replication/it/delete-person.sql", executionPhase = AFTER_TEST_METHOD)
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

//        replicationConsumer.setExpectedReplicationCount(1);
        final Person actualSavedPerson = personService.save(givenPerson);

        final Long expectedSavedPersonId = 1L;
        final Person expectedSavedPerson = Person.builder()
                .id(expectedSavedPersonId)
                .name(givenName)
                .surname(givenSurname)
                .patronymic(givenPatronymic)
                .birthDate(givenBirthDate)
                .build();
        assertEquals(expectedSavedPerson, actualSavedPerson);

//        assertTrue(replicationConsumer.isSuccessConsuming());

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

//        replicationConsumer.setExpectedReplicationCount(2);
        final List<Person> actualSavedPersons = personService.saveAll(givenPersons);

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

//        assertTrue(replicationConsumer.isSuccessConsuming());

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
    @Transactional(propagation = NOT_SUPPORTED)
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

//        replicationConsumer.setExpectedReplicationCount(1);
        final Person actualUpdatedPerson = personService.update(givenNewPerson);
        assertEquals(givenNewPerson, actualUpdatedPerson);

//        assertTrue(replicationConsumer.isSuccessConsuming());

        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        final ReplicatedPerson actualUpdatedReplication = replicatedPersonService.getById(givenId);
        final ReplicatedPerson expectedUpdatedReplication = ReplicatedPerson.builder()
                .id(givenId)
                .name(givenNewName)
                .surname(givenNewSurname)
                .birthDate(givenNewBirthDate)
                .build();
        assertEquals(expectedUpdatedReplication, actualUpdatedReplication);
    }

    @Test
    @Transactional(propagation = NOT_SUPPORTED)
    @Sql("classpath:sql-scripts/replication/it/insert-person.sql")
    @Sql(value = "classpath:sql-scripts/replication/it/delete-person.sql", executionPhase = AFTER_TEST_METHOD)
    public void personAndReplicatedPersonShouldBeUpdatedPartially() {
        final Long givenId = 255L;

        final String givenNewName = "Ivan";
        final String givenNewSurname = "Ivanov";
        final String givenNewPatronymic = "Ivanovich";
        final PersonName givenPartial = new PersonName(givenNewName, givenNewSurname, givenNewPatronymic);

//        replicationConsumer.setExpectedReplicationCount(1);
        final Person actualUpdatedPerson = personService.updatePartial(givenId, givenPartial);
        final LocalDate expectedBirthDate = LocalDate.of(2000, 2, 18);
        final Person expectedUpdatedPerson = Person.builder()
                .id(givenId)
                .name(givenNewName)
                .surname(givenNewSurname)
                .patronymic(givenNewPatronymic)
                .birthDate(expectedBirthDate)
                .build();
        assertEquals(expectedUpdatedPerson, actualUpdatedPerson);

//        assertTrue(replicationConsumer.isSuccessConsuming());

        final ReplicatedPerson actualUpdatedReplication = replicatedPersonService.getById(givenId);
        final ReplicatedPerson expectedUpdatedReplication = ReplicatedPerson.builder()
                .id(givenId)
                .name(givenNewName)
                .surname(givenNewSurname)
                .birthDate(expectedBirthDate)
                .build();
        assertEquals(expectedUpdatedReplication, actualUpdatedReplication);
    }

    @Test
    @Transactional(propagation = NOT_SUPPORTED)
    @Sql("classpath:sql-scripts/replication/it/insert-person.sql")
    @Sql(value = "classpath:sql-scripts/replication/it/delete-person.sql", executionPhase = AFTER_TEST_METHOD)
    public void personAndReplicatedPersonShouldBeDeleted() {
        final Long givenId = 255L;

//        replicationConsumer.setExpectedReplicationCount(1);
        personService.delete(givenId);

//        assertTrue(replicationConsumer.isSuccessConsuming());

        final boolean successDeleting = !personService.isExist(givenId) && !replicatedPersonService.isExist(givenId);
        assertTrue(successDeleting);
    }
}
