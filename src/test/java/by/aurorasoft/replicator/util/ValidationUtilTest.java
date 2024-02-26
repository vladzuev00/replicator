package by.aurorasoft.replicator.util;

import org.junit.Test;

import static by.aurorasoft.replicator.util.ValidationUtil.withinIntervalOrElseThrow;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class ValidationUtilTest {

    @Test
    public void valueShouldBeWithinInterval() {
        final int givenValue = 5;
        final int givenMin = 1;
        final int givenMax = 10;
        final String givenPropertyName = "property";

        withinIntervalOrElseThrow(givenValue, givenMin, givenMax, givenPropertyName);
    }

    @Test
    public void valueShouldNotBeWithinIntervalBecauseOfLessThanMin() {
        final int givenValue = 0;
        final int givenMin = 1;
        final int givenMax = 10;
        final String givenPropertyName = "property";

        boolean exceptionArisen;
        try {
            withinIntervalOrElseThrow(givenValue, givenMin, givenMax, givenPropertyName);
            exceptionArisen = false;
        } catch (final IllegalArgumentException exception) {
            exceptionArisen = true;

            final String actualMessage = exception.getMessage();
            final String expectedMessage = "Not valid 'property'. It should be within [1, 10], but was 0";
            assertEquals(expectedMessage, actualMessage);
        }
        assertTrue(exceptionArisen);
    }

    @Test
    public void valueShouldNotBeWithinIntervalBecauseOfBiggerThanMax() {
        final int givenValue = 11;
        final int givenMin = 1;
        final int givenMax = 10;
        final String givenPropertyName = "property";

        boolean exceptionArisen;
        try {
            withinIntervalOrElseThrow(givenValue, givenMin, givenMax, givenPropertyName);
            exceptionArisen = false;
        } catch (final IllegalArgumentException exception) {
            exceptionArisen = true;

            final String actualMessage = exception.getMessage();
            final String expectedMessage = "Not valid 'property'. It should be within [1, 10], but was 11";
            assertEquals(expectedMessage, actualMessage);
        }
        assertTrue(exceptionArisen);
    }

}
