package by.aurorasoft.testapp.it;

import by.aurorasoft.testapp.crud.dto.Person;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class ReplicationAttemptIT {

    @Test
    public void personShouldBeSavedButNotReplicatedBecauseOfThereIsNoReplicatedAddress() {
        final String givenName = "Petr";
        final String givenSurname = "Ivanov";
        final String givenPatronymic = "Petrovich";
        final LocalDate givenBirthDate = LocalDate.of(2000, 3, 19);
        final Long givenAddressId = 263L;
        final Person givenPerson = createPerson(
                givenName,
                givenSurname,
                givenPatronymic,
                givenBirthDate,
                givenAddressId
        );

        final Person actualSavedPerson = personService.save(givenPerson);
        waitReplicating();

        final Long expectedPersonId = 1L;
        final Person expectedSavedPerson = createPerson(
                expectedPersonId,
                givenName,
                givenSurname,
                givenPatronymic,
                givenBirthDate,
                givenAddressId
        );
        assertEquals(expectedSavedPerson, actualSavedPerson);

        assertFalse(isReplicatedPersonExist(expectedPersonId));
    }
}
