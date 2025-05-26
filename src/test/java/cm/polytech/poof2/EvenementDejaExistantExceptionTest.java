package cm.polytech.poof2;

import cm.polytech.poof2.Exception.EvenementDejaExistantException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EvenementDejaExistantExceptionTest {

    @Test
    void testExceptionMessage() {
        String message = "Event already exists";
        EvenementDejaExistantException exception = new EvenementDejaExistantException(message);
        assertEquals(message, exception.getMessage(), "Exception message should match");
    }
}