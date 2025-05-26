package cm.polytech.poof2;

import cm.polytech.poof2.Exception.EvenementNonTrouveException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EvenementNonTrouveExceptionTest {

    @Test
    void testExceptionMessage() {
        String message = "Event not found";
        EvenementNonTrouveException exception = new EvenementNonTrouveException(message);
        assertEquals(message, exception.getMessage(), "Exception message should match");
    }
}