package cm.polytech.poof2;

import cm.polytech.poof2.Exception.CapaciteMaxAtteinteException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CapaciteMaxAtteinteExceptionTest {

    @Test
    void testExceptionMessage() {
        String message = "Maximum capacity reached";
        CapaciteMaxAtteinteException exception = new CapaciteMaxAtteinteException(message);
        assertEquals(message, exception.getMessage(), "Exception message should match");
    }
}