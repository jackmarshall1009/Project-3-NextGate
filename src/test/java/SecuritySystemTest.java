/**
 * Tests for the SecuritySystem class (the model) in Next Gate.
 * Covers check-in, cancel, position, every officer action, the
 * lane helpers, stats, and every exception the model throws.
 *
 * @author Ethan
 * @version Nov 19, 2026
 */
public class SecuritySystemTest extends student.TestCase
{
    private SecuritySystem system;

    /**
     * Runs before every test. Makes a fresh, empty SecuritySystem.
     */
    public void setUp()
    {
        system = new SecuritySystem();
    }


    // ----------------------------------------------------------
    /**
     * Tests that a new system has empty lines and all stats at 0.
     */
    public void testNewSystem()
    {
        assertEquals(0, system.getCurrWaitingCount());
        assertEquals(0, system.totalScreenedCount());
        assertEquals(0, system.totalRescreenCount());
        assertEquals(0, system.totalCancelCount());
        assertEquals(0, system.totalNoShowCount());
        assertNull(system.currentPassenger(
            SecuritySystem.ScreeningLane.STANDARD));
    }


    // ----------------------------------------------------------
    /**
     * Tests that check-in hands out queue numbers 1, 2, 3 in order.
     */
    public void testCheckInQueueNumbers()
    {
        assertEquals(1, system.checkIn("Ana", "AA100", false));
        assertEquals(2, system.checkIn("Ben", "DL200", true));
        assertEquals(3, system.checkIn("Cal", "UA300", false));
        assertEquals(3, system.getCurrWaitingCount());
    }


    // ----------------------------------------------------------
    /**
     * Tests that standard passengers go to the standard lane and
     * priority passengers go to the priority lane.
     */
    public void testCheckInPicksLane()
    {
        system.checkIn("Ana", "AA100", false);
        system.checkIn("Ben", "DL200", true);

        String[] standard = system.getLine(
            SecuritySystem.ScreeningLane.STANDARD);
        String[] priority = system.getLine(
            SecuritySystem.ScreeningLane.PRIORITY);

        assertEquals(1, standard.length);
        assertEquals(1, priority.length);
        assertTrue(standard[0].contains("Ana"));
        assertTrue(priority[0].contains("Ben"));
    }


    // ----------------------------------------------------------
    /**
     * Tests that check-in removes extra spaces and makes the
     * flight number uppercase.
     */
    public void testCheckInCleansInput()
    {
        system.checkIn("  Ana  ", " aa100 ", false);

        assertEquals(1, system.getPosition("Ana", "AA100"));
    }


    // ----------------------------------------------------------
    /**
     * Tests that a new passenger starts with the WAITING status.
     */
    public void testCheckInStatusWaiting()
    {
        system.checkIn("Ana", "AA100", false);

        String[] line = system.getLine(
            SecuritySystem.ScreeningLane.STANDARD);

        assertTrue(line[0].contains("WAITING"));
    }


    // ----------------------------------------------------------
    /**
     * Tests that a blank or null name is rejected.
     */
    public void testCheckInBlankName()
    {
        Exception exception = null;
        try
        {
            system.checkIn("   ", "AA100", false);
        }
        catch (Exception e)
        {
            exception = e;
        }
        assertTrue(exception instanceof IllegalArgumentException);

        exception = null;
        try
        {
            system.checkIn(null, "AA100", false);
        }
        catch (Exception e)
        {
            exception = e;
        }
        assertTrue(exception instanceof IllegalArgumentException);
        assertEquals(0, system.getCurrWaitingCount());
    }


    // ----------------------------------------------------------
    /**
     * Tests that a blank or null flight number is rejected.
     */
    public void testCheckInBlankFlight()
    {
        Exception exception = null;
        try
        {
            system.checkIn("Ana", "", false);
        }
        catch (Exception e)
        {
            exception = e;
        }
        assertTrue(exception instanceof IllegalArgumentException);

        exception = null;
        try
        {
            system.checkIn("Ana", null, false);
        }
        catch (Exception e)
        {
            exception = e;
        }
        assertTrue(exception instanceof IllegalArgumentException);
    }


    // ----------------------------------------------------------
    /**
     * Tests that badly formatted flight numbers are rejected:
     * no letters, too many letters, no digits, too many digits.
     */
    public void testCheckInBadFlightFormat()
    {
        String[] badFlights = {"1234", "AAA12", "AA", "AA12345"};

        for (int i = 0; i < badFlights.length; i++)
        {
            Exception exception = null;
            try
            {
                system.checkIn("Ana", badFlights[i], false);
            }
            catch (Exception e)
            {
                exception = e;
            }
            assertTrue(exception instanceof IllegalArgumentException);
        }
        assertEquals(0, system.getCurrWaitingCount());
    }


    // ----------------------------------------------------------
    /**
     * Tests that the shortest and longest valid flight numbers
     * are accepted.
     */
    public void testCheckInGoodFlightEdges()
    {
        assertEquals(1, system.checkIn("Ana", "AA1", false));
        assertEquals(2, system.checkIn("Ben", "AA1234", false));
    }


    // ----------------------------------------------------------
    /**
     * Tests that the same passenger cannot check in twice.
     */
    public void testCheckInDuplicate()
    {
        system.checkIn("Ana", "AA100", false);

        Exception exception = null;
        try
        {
            system.checkIn("Ana", "aa100", true);
        }
        catch (Exception e)
        {
            exception = e;
        }
        assertTrue(exception instanceof IllegalArgumentException);
        assertEquals(1, system.getCurrWaitingCount());
    }


    // ----------------------------------------------------------
    /**
     * Tests that a duplicate is caught even when the name is typed
     * with different capitalization.
     */
    public void testCheckInDuplicateDifferentCase()
    {
        system.checkIn("Ana", "AA100", false);

        Exception exception = null;
        try
        {
            system.checkIn("ANA", "AA100", false);
        }
        catch (Exception e)
        {
            exception = e;
        }
        assertTrue(exception instanceof IllegalArgumentException);
        assertEquals(1, system.getCurrWaitingCount());
    }


    // ----------------------------------------------------------
    /**
     * Tests that the same name on a different flight is allowed.
     */
    public void testCheckInSameNameDifferentFlight()
    {
        system.checkIn("Ana", "AA100", false);

        assertEquals(2, system.checkIn("Ana", "DL200", false));
    }


    // ----------------------------------------------------------
    /**
     * Tests getPosition in the standard and priority lanes, and
     * -1 for someone who is not in any line.
     */
    public void testGetPosition()
    {
        system.checkIn("Ana", "AA100", false);
        system.checkIn("Ben", "DL200", false);
        system.checkIn("Cal", "UA300", true);

        assertEquals(1, system.getPosition("Ana", "AA100"));
        assertEquals(2, system.getPosition("Ben", "DL200"));
        assertEquals(1, system.getPosition("Cal", "UA300"));
        assertEquals(-1, system.getPosition("Zed", "AA1"));
    }


    // ----------------------------------------------------------
    /**
     * Tests that getPosition finds a passenger in the rescreen
     * lane.
     */
    public void testGetPositionRescreen()
    {
        system.checkIn("Ana", "AA100", false);
        system.callNext(SecuritySystem.ScreeningLane.STANDARD);
        system.sendToRescreen(SecuritySystem.ScreeningLane.STANDARD);

        assertEquals(1, system.getPosition("Ana", "AA100"));
    }


    // ----------------------------------------------------------
    /**
     * Tests cancelling a passenger from each lane.
     */
    public void testCancel()
    {
        system.checkIn("Ana", "AA100", false);
        system.checkIn("Ben", "DL200", true);

        assertTrue(system.cancel("Ana", "aa100"));
        assertTrue(system.cancel("Ben", "DL200"));
        assertEquals(0, system.getCurrWaitingCount());
        assertEquals(2, system.totalCancelCount());
    }


    // ----------------------------------------------------------
    /**
     * Tests cancelling a passenger who is waiting in the rescreen
     * lane.
     */
    public void testCancelFromRescreen()
    {
        system.checkIn("Ana", "AA100", false);
        system.callNext(SecuritySystem.ScreeningLane.STANDARD);
        system.sendToRescreen(SecuritySystem.ScreeningLane.STANDARD);

        assertTrue(system.cancel("Ana", "AA100"));
        assertEquals(0, system.getCurrWaitingCount());
    }


    // ----------------------------------------------------------
    /**
     * Tests that cancelling someone not in line returns false
     * and does not change the cancel count.
     */
    public void testCancelNotFound()
    {
        assertFalse(system.cancel("Zed", "AA1"));
        assertEquals(0, system.totalCancelCount());
    }


    // ----------------------------------------------------------
    /**
     * Tests that a passenger being screened cannot cancel.
     */
    public void testCancelWhileScreening()
    {
        system.checkIn("Ana", "AA100", false);
        system.callNext(SecuritySystem.ScreeningLane.STANDARD);

        Exception exception = null;
        try
        {
            system.cancel("Ana", "AA100");
        }
        catch (Exception e)
        {
            exception = e;
        }
        assertTrue(exception instanceof IllegalStateException);
        assertEquals(0, system.totalCancelCount());
    }


    // ----------------------------------------------------------
    /**
     * Tests that callNext takes the front passenger, marks them
     * SCREENING, and makes them the current passenger.
     */
    public void testCallNext()
    {
        system.checkIn("Ana", "AA100", false);
        system.checkIn("Ben", "DL200", false);

        Passenger called = system.callNext(
            SecuritySystem.ScreeningLane.STANDARD);

        assertEquals("Ana", called.getName());
        assertEquals("SCREENING", called.getStatus());
        assertEquals(called, system.currentPassenger(
            SecuritySystem.ScreeningLane.STANDARD));
        assertEquals(1, system.getCurrWaitingCount());
    }


    // ----------------------------------------------------------
    /**
     * Tests callNext in the priority and rescreen lanes.
     */
    public void testCallNextOtherLanes()
    {
        system.checkIn("Ana", "AA100", true);
        system.checkIn("Ben", "DL200", false);
        system.callNext(SecuritySystem.ScreeningLane.STANDARD);
        system.sendToRescreen(SecuritySystem.ScreeningLane.STANDARD);

        Passenger priority = system.callNext(
            SecuritySystem.ScreeningLane.PRIORITY);
        Passenger rescreen = system.callNext(
            SecuritySystem.ScreeningLane.RESCREEN);

        assertEquals("Ana", priority.getName());
        assertEquals("Ben", rescreen.getName());
        assertEquals(priority, system.currentPassenger(
            SecuritySystem.ScreeningLane.PRIORITY));
        assertEquals(rescreen, system.currentPassenger(
            SecuritySystem.ScreeningLane.RESCREEN));
    }


    // ----------------------------------------------------------
    /**
     * Tests that callNext on an empty lane throws an exception.
     */
    public void testCallNextEmpty()
    {
        Exception exception = null;
        try
        {
            system.callNext(SecuritySystem.ScreeningLane.STANDARD);
        }
        catch (Exception e)
        {
            exception = e;
        }
        assertTrue(exception instanceof IllegalStateException);
    }


    // ----------------------------------------------------------
    /**
     * Tests that callNext throws if someone is already being
     * screened in that lane, and the line is not changed.
     */
    public void testCallNextWhileScreening()
    {
        system.checkIn("Ana", "AA100", false);
        system.checkIn("Ben", "DL200", false);
        system.callNext(SecuritySystem.ScreeningLane.STANDARD);

        Exception exception = null;
        try
        {
            system.callNext(SecuritySystem.ScreeningLane.STANDARD);
        }
        catch (Exception e)
        {
            exception = e;
        }
        assertTrue(exception instanceof IllegalStateException);
        assertEquals(1, system.getPosition("Ben", "DL200"));
    }


    // ----------------------------------------------------------
    /**
     * Tests that clearing a passenger marks them CLEARED, adds to
     * the screened count, and frees up the lane.
     */
    public void testClearPassenger()
    {
        system.checkIn("Ana", "AA100", false);
        system.callNext(SecuritySystem.ScreeningLane.STANDARD);

        Passenger cleared = system.clearPassenger(
            SecuritySystem.ScreeningLane.STANDARD);

        assertEquals("CLEARED", cleared.getStatus());
        assertEquals(1, system.totalScreenedCount());
        assertNull(system.currentPassenger(
            SecuritySystem.ScreeningLane.STANDARD));
    }


    // ----------------------------------------------------------
    /**
     * Tests that clearing when nobody is being screened throws.
     */
    public void testClearPassengerNobody()
    {
        Exception exception = null;
        try
        {
            system.clearPassenger(SecuritySystem.ScreeningLane.STANDARD);
        }
        catch (Exception e)
        {
            exception = e;
        }
        assertTrue(exception instanceof IllegalStateException);
        assertEquals(0, system.totalScreenedCount());
    }


    // ----------------------------------------------------------
    /**
     * Tests that sending to rescreen marks the passenger
     * RESCREENING, puts them in the rescreen lane, and frees up
     * their old lane.
     */
    public void testSendToRescreen()
    {
        system.checkIn("Ana", "AA100", false);
        system.callNext(SecuritySystem.ScreeningLane.STANDARD);

        Passenger sent = system.sendToRescreen(
            SecuritySystem.ScreeningLane.STANDARD);

        assertEquals("RESCREENING", sent.getStatus());
        assertEquals(1, system.totalRescreenCount());
        assertNull(system.currentPassenger(
            SecuritySystem.ScreeningLane.STANDARD));
        assertEquals(1, system.getLine(
            SecuritySystem.ScreeningLane.RESCREEN).length);
    }


    // ----------------------------------------------------------
    /**
     * Tests that sending to rescreen when nobody is being
     * screened throws.
     */
    public void testSendToRescreenNobody()
    {
        Exception exception = null;
        try
        {
            system.sendToRescreen(SecuritySystem.ScreeningLane.PRIORITY);
        }
        catch (Exception e)
        {
            exception = e;
        }
        assertTrue(exception instanceof IllegalStateException);
        assertEquals(0, system.totalRescreenCount());
    }


    // ----------------------------------------------------------
    /**
     * Tests screenPassenger with a 0% rescreen chance. The
     * passenger should always be cleared.
     */
    public void testScreenPassengerClears()
    {
        system.setRescreenChance(0);
        system.checkIn("Ana", "AA100", false);
        system.callNext(SecuritySystem.ScreeningLane.STANDARD);

        Passenger p = system.screenPassenger(
            SecuritySystem.ScreeningLane.STANDARD);

        assertEquals("CLEARED", p.getStatus());
        assertEquals(1, system.totalScreenedCount());
        assertEquals(0, system.totalRescreenCount());
    }


    // ----------------------------------------------------------
    /**
     * Tests screenPassenger with a 100% rescreen chance on a first
     * try. The passenger should get a second attempt at the front
     * of their own lane, not go to the rescreen lane.
     */
    public void testScreenPassengerRescreens()
    {
        system.setRescreenChance(100);
        system.checkIn("Ana", "AA100", false);
        system.checkIn("Ben", "DL200", false);
        system.callNext(SecuritySystem.ScreeningLane.STANDARD);

        Passenger p = system.screenPassenger(
            SecuritySystem.ScreeningLane.STANDARD);

        assertEquals("SCREENING_ATTEMPT_2", p.getStatus());
        assertEquals(1, system.getPosition("Ana", "AA100"));
        assertEquals(2, system.getPosition("Ben", "DL200"));
        assertEquals(0, system.getLine(
            SecuritySystem.ScreeningLane.RESCREEN).length);
        assertEquals(0, system.totalScreenedCount());
        assertEquals(0, system.totalRescreenCount());
    }


    // ----------------------------------------------------------
    /**
     * Tests failing the second attempt. The passenger should now
     * go to the rescreen lane and the rescreen count goes up.
     */
    public void testScreenPassengerFailsTwice()
    {
        system.setRescreenChance(100);
        system.checkIn("Ana", "AA100", false);
        system.callNext(SecuritySystem.ScreeningLane.STANDARD);
        system.screenPassenger(SecuritySystem.ScreeningLane.STANDARD);
        system.callNext(SecuritySystem.ScreeningLane.STANDARD);

        Passenger p = system.screenPassenger(
            SecuritySystem.ScreeningLane.STANDARD);

        assertEquals("RESCREENING", p.getStatus());
        assertEquals(1, system.totalRescreenCount());
        assertEquals(1, system.getLine(
            SecuritySystem.ScreeningLane.RESCREEN).length);
        assertEquals(0, system.getLine(
            SecuritySystem.ScreeningLane.STANDARD).length);
    }


    // ----------------------------------------------------------
    /**
     * Tests that a passenger on their second attempt can be
     * cleared normally.
     */
    public void testSecondAttemptCleared()
    {
        system.setRescreenChance(100);
        system.checkIn("Ana", "AA100", false);
        system.callNext(SecuritySystem.ScreeningLane.STANDARD);
        system.screenPassenger(SecuritySystem.ScreeningLane.STANDARD);
        system.callNext(SecuritySystem.ScreeningLane.STANDARD);
        system.setRescreenChance(0);

        Passenger p = system.screenPassenger(
            SecuritySystem.ScreeningLane.STANDARD);

        assertEquals("CLEARED", p.getStatus());
        assertEquals(1, system.totalScreenedCount());
        assertEquals(0, system.getCurrWaitingCount());
    }


    // ----------------------------------------------------------
    /**
     * Tests that failing in the rescreen lane sends the passenger
     * to the back of the rescreen lane, with no second attempt.
     */
    public void testScreenPassengerFailsInRescreenLane()
    {
        system.checkIn("Ana", "AA100", false);
        system.checkIn("Ben", "DL200", false);
        system.callNext(SecuritySystem.ScreeningLane.STANDARD);
        system.sendToRescreen(SecuritySystem.ScreeningLane.STANDARD);
        system.callNext(SecuritySystem.ScreeningLane.STANDARD);
        system.sendToRescreen(SecuritySystem.ScreeningLane.STANDARD);
        system.callNext(SecuritySystem.ScreeningLane.RESCREEN);
        system.setRescreenChance(100);

        Passenger p = system.screenPassenger(
            SecuritySystem.ScreeningLane.RESCREEN);

        assertEquals("Ana", p.getName());
        assertEquals("RESCREENING", p.getStatus());
        assertEquals(1, system.getPosition("Ben", "DL200"));
        assertEquals(2, system.getPosition("Ana", "AA100"));
    }


    // ----------------------------------------------------------
    /**
     * Tests retryScreening directly. The passenger goes to the
     * front of their own lane, the lane is freed, and the rescreen
     * count does not change.
     */
    public void testRetryScreening()
    {
        system.checkIn("Ana", "AA100", false);
        system.checkIn("Ben", "DL200", false);
        system.callNext(SecuritySystem.ScreeningLane.STANDARD);

        Passenger p = system.retryScreening(
            SecuritySystem.ScreeningLane.STANDARD);

        assertEquals("SCREENING_ATTEMPT_2", p.getStatus());
        assertNull(system.currentPassenger(
            SecuritySystem.ScreeningLane.STANDARD));
        assertEquals(1, system.getPosition("Ana", "AA100"));
        assertEquals(2, system.getPosition("Ben", "DL200"));
        assertEquals(0, system.totalRescreenCount());
    }


    // ----------------------------------------------------------
    /**
     * Tests that retryScreening also works in the priority lane.
     */
    public void testRetryScreeningPriority()
    {
        system.checkIn("Ana", "AA100", true);
        system.callNext(SecuritySystem.ScreeningLane.PRIORITY);

        system.retryScreening(SecuritySystem.ScreeningLane.PRIORITY);

        String[] priority = system.getLine(
            SecuritySystem.ScreeningLane.PRIORITY);

        assertEquals(1, priority.length);
        assertTrue(priority[0].contains("SCREENING_ATTEMPT_2"));
        assertEquals(0, system.getLine(
            SecuritySystem.ScreeningLane.STANDARD).length);
    }


    // ----------------------------------------------------------
    /**
     * Tests that retryScreening throws when nobody is being
     * screened.
     */
    public void testRetryScreeningNobody()
    {
        Exception exception = null;
        try
        {
            system.retryScreening(SecuritySystem.ScreeningLane.STANDARD);
        }
        catch (Exception e)
        {
            exception = e;
        }
        assertTrue(exception instanceof IllegalStateException);
    }


    // ----------------------------------------------------------
    /**
     * Tests that callNext keeps the SCREENING_ATTEMPT_2 status
     * instead of changing it back to SCREENING.
     */
    public void testCallNextKeepsSecondAttempt()
    {
        system.checkIn("Ana", "AA100", false);
        system.callNext(SecuritySystem.ScreeningLane.STANDARD);
        system.retryScreening(SecuritySystem.ScreeningLane.STANDARD);

        Passenger again = system.callNext(
            SecuritySystem.ScreeningLane.STANDARD);

        assertEquals("Ana", again.getName());
        assertEquals("SCREENING_ATTEMPT_2", again.getStatus());
    }


    // ----------------------------------------------------------
    /**
     * Tests that a passenger waiting for their second attempt can
     * still cancel.
     */
    public void testCancelDuringSecondAttempt()
    {
        system.checkIn("Ana", "AA100", false);
        system.callNext(SecuritySystem.ScreeningLane.STANDARD);
        system.retryScreening(SecuritySystem.ScreeningLane.STANDARD);

        assertTrue(system.cancel("Ana", "AA100"));
        assertEquals(1, system.totalCancelCount());
        assertEquals(0, system.getCurrWaitingCount());
    }


    // ----------------------------------------------------------
    /**
     * Tests that a passenger waiting for their second attempt
     * cannot check in again.
     */
    public void testCheckInDuringSecondAttempt()
    {
        system.checkIn("Ana", "AA100", false);
        system.callNext(SecuritySystem.ScreeningLane.STANDARD);
        system.retryScreening(SecuritySystem.ScreeningLane.STANDARD);

        Exception exception = null;
        try
        {
            system.checkIn("Ana", "AA100", false);
        }
        catch (Exception e)
        {
            exception = e;
        }
        assertTrue(exception instanceof IllegalArgumentException);
        assertEquals(1, system.getCurrWaitingCount());
    }


    // ----------------------------------------------------------
    /**
     * Tests that screenPassenger throws when nobody is being
     * screened, for both the clear path and the rescreen path.
     */
    public void testScreenPassengerNobody()
    {
        system.setRescreenChance(0);
        Exception exception = null;
        try
        {
            system.screenPassenger(SecuritySystem.ScreeningLane.STANDARD);
        }
        catch (Exception e)
        {
            exception = e;
        }
        assertTrue(exception instanceof IllegalStateException);

        system.setRescreenChance(100);
        exception = null;
        try
        {
            system.screenPassenger(SecuritySystem.ScreeningLane.STANDARD);
        }
        catch (Exception e)
        {
            exception = e;
        }
        assertTrue(exception instanceof IllegalStateException);
    }


    // ----------------------------------------------------------
    /**
     * Tests that marking a no-show sets the status, adds to the
     * no-show count, frees up the lane, and returns a message.
     */
    public void testMarkNoShow()
    {
        system.checkIn("Ana", "AA100", false);
        Passenger p = system.callNext(
            SecuritySystem.ScreeningLane.STANDARD);

        String message = system.markNoShow(
            SecuritySystem.ScreeningLane.STANDARD);

        assertEquals("Ana was marked as a no-show.", message);
        assertEquals("NO_SHOW", p.getStatus());
        assertEquals(1, system.totalNoShowCount());
        assertNull(system.currentPassenger(
            SecuritySystem.ScreeningLane.STANDARD));
    }


    // ----------------------------------------------------------
    /**
     * Tests that marking a no-show when nobody is being screened
     * throws.
     */
    public void testMarkNoShowNobody()
    {
        Exception exception = null;
        try
        {
            system.markNoShow(SecuritySystem.ScreeningLane.RESCREEN);
        }
        catch (Exception e)
        {
            exception = e;
        }
        assertTrue(exception instanceof IllegalStateException);
        assertEquals(0, system.totalNoShowCount());
    }


    // ----------------------------------------------------------
    /**
     * Tests that getLine returns an empty array for an empty lane
     * and lists passengers front to back.
     */
    public void testGetLine()
    {
        assertEquals(0, system.getLine(
            SecuritySystem.ScreeningLane.STANDARD).length);

        system.checkIn("Ana", "AA100", false);
        system.checkIn("Ben", "DL200", false);

        String[] line = system.getLine(
            SecuritySystem.ScreeningLane.STANDARD);

        assertEquals(2, line.length);
        assertTrue(line[0].contains("Ana"));
        assertTrue(line[1].contains("Ben"));
    }


    // ----------------------------------------------------------
    /**
     * Tests that the waiting count adds up all three lanes but
     * does not count passengers being screened.
     */
    public void testGetCurrWaitingCount()
    {
        system.checkIn("Ana", "AA100", false);
        system.checkIn("Ben", "DL200", true);
        system.checkIn("Cal", "UA300", false);
        system.callNext(SecuritySystem.ScreeningLane.STANDARD);
        system.sendToRescreen(SecuritySystem.ScreeningLane.STANDARD);

        assertEquals(3, system.getCurrWaitingCount());

        system.callNext(SecuritySystem.ScreeningLane.PRIORITY);

        assertEquals(2, system.getCurrWaitingCount());
    }


    // ----------------------------------------------------------
    /**
     * Tests the wait estimate for valid positions.
     */
    public void testEstimateWaitTime()
    {
        assertEquals(3, system.estimateWaitTime(1,
            SecuritySystem.ScreeningLane.STANDARD));
        assertEquals(12, system.estimateWaitTime(4,
            SecuritySystem.ScreeningLane.PRIORITY));
    }


    // ----------------------------------------------------------
    /**
     * Tests that the wait estimate rejects positions below 1.
     */
    public void testEstimateWaitTimeBadPosition()
    {
        Exception exception = null;
        try
        {
            system.estimateWaitTime(0,
                SecuritySystem.ScreeningLane.STANDARD);
        }
        catch (Exception e)
        {
            exception = e;
        }
        assertTrue(exception instanceof IllegalArgumentException);

        exception = null;
        try
        {
            system.estimateWaitTime(-1,
                SecuritySystem.ScreeningLane.STANDARD);
        }
        catch (Exception e)
        {
            exception = e;
        }
        assertTrue(exception instanceof IllegalArgumentException);
    }
}
