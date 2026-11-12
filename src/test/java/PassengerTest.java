
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PassengerTest {

    private Passenger passenger1;

    public void setUp() {
        passenger1 = new Passenger("John Doe", "AAA123", 1, true);
    }

    /**
     * Tests getName method
     */
    public void testGetName() {
        assertEquals("John Doe", passenger1.getName());
    }

    /**
     * Tests getFlightNumber method
     */
    public void testGetFlightNumber() {
        assertEquals("AAA123", passenger1.getFlightNumber());
    }

    /**
     * Tests getQueueNumber method
     */
    public void testGetQueueNumber() {
        assertEquals(1, passenger1.getQueueNumber());
    }

    /**
     * Tests getPriority and setPriority methods
     */
    public void testIsPriority() {
        assertTrue(passenger1.isPriority());

        passenger1.setPriority(false);
        assertFalse(passenger1.isPriority());
    }

    /**
     * Tests getStatus and setStatus methods
     */
    public void testGetStatus() {
        // Depends on the initial status in your Passenger constructor
        // and the available status getter/setter.
    }

    /**
     * Tests equals method
     */
    public void testEquals() {
        Passenger passenger2 =
            new Passenger("John Doe", "AAA123", 1, true);

        assertEquals(passenger1, passenger2);
    }

    /**
     * Tests toString method
     */
    @Test
    public void testToString() {
        assertNotNull(passenger1.toString());
    }
}
