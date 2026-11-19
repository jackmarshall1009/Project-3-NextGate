/**
 * Tests for the Passenger class in Next Gate.
 *
 * @author Ethan
 * @version Nov 19, 2026
 */
public class PassengerTest extends student.TestCase
{
    private Passenger ana;

    /**
     * Runs before every test. Makes one standard passenger.
     */
    public void setUp()
    {
        ana = new Passenger("Ana", "AA100", 1, false);
    }


    // ----------------------------------------------------------
    /**
     * Tests that the constructor stores everything and the status
     * starts as "Waiting".
     */
    public void testConstructorAndGetters()
    {
        assertEquals("Ana", ana.getName());
        assertEquals("AA100", ana.getFlightNumber());
        assertEquals(1, ana.getQueueNumber());
        assertFalse(ana.isPriority());
        assertEquals("Waiting", ana.getStatus());
    }


    // ----------------------------------------------------------
    /**
     * Tests setStatus and setPriority.
     */
    public void testSetters()
    {
        ana.setStatus("SCREENING");
        ana.setPriority(true);

        assertEquals("SCREENING", ana.getStatus());
        assertTrue(ana.isPriority());
    }


    // ----------------------------------------------------------
    /**
     * Tests that a passenger equals itself.
     */
    public void testEqualsSameObject()
    {
        assertTrue(ana.equals(ana));
    }


    // ----------------------------------------------------------
    /**
     * Tests that two passengers with the same name and flight are
     * equal, even with a different queue number, priority, or
     * capitalization.
     */
    public void testEqualsSameNameAndFlight()
    {
        Passenger copy = new Passenger("Ana", "AA100", 9, true);
        Passenger lower = new Passenger("ana", "aa100", 2, false);

        assertTrue(ana.equals(copy));
        assertTrue(ana.equals(lower));
    }


    // ----------------------------------------------------------
    /**
     * Tests that a different name or a different flight makes two
     * passengers not equal.
     */
    public void testEqualsDifferent()
    {
        Passenger otherName = new Passenger("Ben", "AA100", 1, false);
        Passenger otherFlight = new Passenger("Ana", "DL200", 1, false);

        assertFalse(ana.equals(otherName));
        assertFalse(ana.equals(otherFlight));
    }


    // ----------------------------------------------------------
    /**
     * Tests that a passenger is not equal to null or to an object
     * that is not a Passenger.
     */
    public void testEqualsNullAndOtherType()
    {
        Object notPassenger = "Ana";

        assertFalse(ana.equals(null));
        assertFalse(ana.equals(notPassenger));
    }


    // ----------------------------------------------------------
    /**
     * Tests that toString includes every field.
     */
    public void testToString()
    {
        String text = ana.toString();

        assertTrue(text.contains("Passenger: Ana"));
        assertTrue(text.contains("Flight: AA100"));
        assertTrue(text.contains("Queue Number: 1"));
        assertTrue(text.contains("Priority: false"));
        assertTrue(text.contains("Screening Status: Waiting"));
    }
}
