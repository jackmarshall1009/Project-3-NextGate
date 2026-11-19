import java.util.Scanner;

/**
 * Tests for the Main class (the controller) in Next Gate. Each
 * test builds a Main with a real SecuritySystem and a SecurityView
 * that reads fake typed input, runs one command, then checks both
 * what was printed and what changed in the model.
 *
 * @author Ethan
 * @version Nov 19, 2026
 */
public class MainTest extends student.TestCase
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
     * Makes a Main whose view "types" the given input. Each \n is
     * the user pressing Enter.
     *
     * @param input everything the user will type
     * @return a Main connected to this test's system
     */
    private Main makeMain(String input)
    {
        SecurityView view = new SecurityView(new Scanner(input));
        return new Main(system, view);
    }


    // ----------------------------------------------------------
    /**
     * Tests officer option 1: calling the next passenger.
     */
    public void testOfficerCallNext()
    {
        system.checkIn("Ana", "AA100", false);
        Main main = makeMain("1\n");

        assertTrue(main.handleOfficerCommand(1));
        assertTrue(systemOut().getHistory().contains("Now Screening: "));
        assertEquals("Ana", system.currentPassenger(
            SecuritySystem.ScreeningLane.STANDARD).getName());
    }


    // ----------------------------------------------------------
    /**
     * Tests that calling next on an empty lane shows an error
     * instead of crashing.
     */
    public void testOfficerCallNextEmpty()
    {
        Main main = makeMain("1\n");

        assertTrue(main.handleOfficerCommand(1));
        assertTrue(systemOut().getHistory().contains(
            "ERROR: No passengers are currently waiting."));
    }


    // ----------------------------------------------------------
    /**
     * Tests officer option 2 when the passenger is cleared.
     */
    public void testOfficerScreenCleared()
    {
        system.setRescreenChance(0);
        system.checkIn("Ana", "AA100", false);
        system.callNext(SecuritySystem.ScreeningLane.STANDARD);
        Main main = makeMain("1\n");

        assertTrue(main.handleOfficerCommand(2));
        assertTrue(systemOut().getHistory().contains(
            "Ana has been cleared."));
        assertEquals(1, system.totalScreenedCount());
    }


    // ----------------------------------------------------------
    /**
     * Tests officer option 2 on a first failed scan. This is the
     * change feature: the passenger gets a second pass.
     */
    public void testOfficerScreenSecondPass()
    {
        system.setRescreenChance(100);
        system.checkIn("Ana", "AA100", false);
        system.callNext(SecuritySystem.ScreeningLane.STANDARD);
        Main main = makeMain("1\n");

        assertTrue(main.handleOfficerCommand(2));
        assertTrue(systemOut().getHistory().contains(
            "Ana has been sent back to the front to be re-screened again."));
        assertEquals(1, system.getPosition("Ana", "AA100"));
    }


    // ----------------------------------------------------------
    /**
     * Tests officer option 2 on a second failed scan. The
     * passenger should be sent to the rescreen lane.
     */
    public void testOfficerScreenRescreened()
    {
        system.setRescreenChance(100);
        system.checkIn("Ana", "AA100", false);
        system.callNext(SecuritySystem.ScreeningLane.STANDARD);
        system.screenPassenger(SecuritySystem.ScreeningLane.STANDARD);
        system.callNext(SecuritySystem.ScreeningLane.STANDARD);
        Main main = makeMain("1\n");

        assertTrue(main.handleOfficerCommand(2));
        assertTrue(systemOut().getHistory().contains(
            "Ana has been sent to be rescreened."));
        assertEquals(1, system.getLine(
            SecuritySystem.ScreeningLane.RESCREEN).length);
    }


    // ----------------------------------------------------------
    /**
     * Tests that screening when nobody is being screened shows an
     * error instead of crashing.
     */
    public void testOfficerScreenNobody()
    {
        Main main = makeMain("2\n");

        assertTrue(main.handleOfficerCommand(2));
        assertTrue(systemOut().getHistory().contains(
            "ERROR: No passenger is currently being screened."));
    }


    // ----------------------------------------------------------
    /**
     * Tests officer option 3: marking a no-show.
     */
    public void testOfficerNoShow()
    {
        system.checkIn("Ana", "AA100", false);
        system.callNext(SecuritySystem.ScreeningLane.STANDARD);
        Main main = makeMain("1\n");

        assertTrue(main.handleOfficerCommand(3));
        assertTrue(systemOut().getHistory().contains(
            "Ana was marked as a no-show."));
        assertEquals(1, system.totalNoShowCount());
    }


    // ----------------------------------------------------------
    /**
     * Tests that marking a no-show with nobody being screened
     * shows an error.
     */
    public void testOfficerNoShowNobody()
    {
        Main main = makeMain("3\n");

        assertTrue(main.handleOfficerCommand(3));
        assertTrue(systemOut().getHistory().contains(
            "ERROR: Nobody is being screened right now."));
    }


    // ----------------------------------------------------------
    /**
     * Tests officer option 4: showing all three lanes.
     */
    public void testOfficerShowAllLines()
    {
        system.checkIn("Ana", "AA100", false);
        system.checkIn("Ben", "DL200", true);
        Main main = makeMain("");

        assertTrue(main.handleOfficerCommand(4));

        String output = systemOut().getHistory();
        assertTrue(output.contains("--- Standard Lane ---"));
        assertTrue(output.contains("--- Priority Lane ---"));
        assertTrue(output.contains("--- Rescreen Lane ---"));
        assertTrue(output.contains("Ana"));
        assertTrue(output.contains("Ben"));
    }


    // ----------------------------------------------------------
    /**
     * Tests officer options 5 and 6: line size and stats.
     */
    public void testOfficerLineSizeAndStats()
    {
        system.checkIn("Ana", "AA100", false);
        system.checkIn("Ben", "DL200", true);
        Main main = makeMain("");

        assertTrue(main.handleOfficerCommand(5));
        assertTrue(main.handleOfficerCommand(6));

        String output = systemOut().getHistory();
        assertTrue(output.contains("Passengers waiting: 2"));
        assertTrue(output.contains("End of Day Stats"));
    }


    // ----------------------------------------------------------
    /**
     * Tests that officer option 7 leaves the officer menu.
     */
    public void testOfficerBack()
    {
        Main main = makeMain("");

        assertFalse(main.handleOfficerCommand(7));
    }


    // ----------------------------------------------------------
    /**
     * Tests passenger option 1: checking in.
     */
    public void testPassengerCheckIn()
    {
        Main main = makeMain("Ana\naa100\nn\n");

        assertTrue(main.handlePassengerCommand(1));
        assertTrue(systemOut().getHistory().contains(
            "Successfully checked in! Your queue number is 1."));
        assertEquals(1, system.getPosition("Ana", "AA100"));
    }


    // ----------------------------------------------------------
    /**
     * Tests that a bad flight number shows an error and nobody is
     * added.
     */
    public void testPassengerCheckInBadFlight()
    {
        Main main = makeMain("Ana\n123\nn\n");

        assertTrue(main.handlePassengerCommand(1));
        assertTrue(systemOut().getHistory().contains(
            "ERROR: Invalid flight number format."));
        assertEquals(0, system.getCurrWaitingCount());
    }


    // ----------------------------------------------------------
    /**
     * Tests that checking in twice shows an error.
     */
    public void testPassengerCheckInDuplicate()
    {
        system.checkIn("Ana", "AA100", false);
        Main main = makeMain("Ana\nAA100\ny\n");

        assertTrue(main.handlePassengerCommand(1));
        assertTrue(systemOut().getHistory().contains(
            "ERROR: Passenger already checked in"));
        assertEquals(1, system.getCurrWaitingCount());
    }


    // ----------------------------------------------------------
    /**
     * Tests that a blank name shows an error instead of checking
     * in. The view lets blanks through, so the model has to catch
     * them.
     */
    public void testPassengerCheckInBlankName()
    {
        Main main = makeMain("   \nAA100\nn\n");

        assertTrue(main.handlePassengerCommand(1));
        assertTrue(systemOut().getHistory().contains(
            "ERROR: Please provide both name and flight number."));
        assertEquals(0, system.getCurrWaitingCount());
    }


    // ----------------------------------------------------------
    /**
     * Tests passenger option 2 when the passenger is in line.
     */
    public void testPassengerPosition()
    {
        system.checkIn("Ana", "AA100", false);
        system.checkIn("Ben", "DL200", false);
        Main main = makeMain("Ben\nDL200\n");

        assertTrue(main.handlePassengerCommand(2));
        assertTrue(systemOut().getHistory().contains(
            "You are number 2 in line."));
        assertTrue(systemOut().getHistory().contains(
            "Estimated wait time: 6 minutes."));
    }


    // ----------------------------------------------------------
    /**
     * Tests passenger option 2 when the passenger is not in line.
     */
    public void testPassengerPositionNotFound()
    {
        Main main = makeMain("Zed\nAA1\n");

        assertTrue(main.handlePassengerCommand(2));
        assertTrue(systemOut().getHistory().contains(
            "You are not in the line."));
    }


    // ----------------------------------------------------------
    /**
     * Tests passenger option 3: cancelling.
     */
    public void testPassengerCancel()
    {
        system.checkIn("Ana", "AA100", false);
        Main main = makeMain("Ana\nAA100\n");

        assertTrue(main.handlePassengerCommand(3));
        assertTrue(systemOut().getHistory().contains(
            "Your reservation was cancelled."));
        assertEquals(0, system.getCurrWaitingCount());
    }


    // ----------------------------------------------------------
    /**
     * Tests that cancelling someone not in line shows an error.
     */
    public void testPassengerCancelNotFound()
    {
        Main main = makeMain("Zed\nAA1\n");

        assertTrue(main.handlePassengerCommand(3));
        assertTrue(systemOut().getHistory().contains(
            "ERROR: Passenger not found."));
    }


    // ----------------------------------------------------------
    /**
     * Tests that a passenger being screened cannot cancel, and an
     * error is shown instead of a crash.
     */
    public void testPassengerCancelWhileScreening()
    {
        system.checkIn("Ana", "AA100", false);
        system.callNext(SecuritySystem.ScreeningLane.STANDARD);
        Main main = makeMain("Ana\nAA100\n");

        assertTrue(main.handlePassengerCommand(3));
        assertTrue(systemOut().getHistory().contains(
            "ERROR: Current passenger is currently being screened"));
    }


    // ----------------------------------------------------------
    /**
     * Tests that passenger option 4 leaves the passenger menu.
     */
    public void testPassengerBack()
    {
        Main main = makeMain("");

        assertFalse(main.handlePassengerCommand(4));
    }


    // ----------------------------------------------------------
    /**
     * Tests run() when the user exits right away.
     */
    public void testRunExit()
    {
        Main main = makeMain("3\n");

        main.run();

        assertTrue(systemOut().getHistory().contains(
            "Thank you for using Next Gate. Goodbye!"));
    }


    // ----------------------------------------------------------
    /**
     * Tests run() through both menus: a passenger checks in, then
     * an officer calls them, then the user exits.
     */
    public void testRunFullSession()
    {
        Main main = makeMain("2\n1\nAna\nAA100\nn\n4\n"
            + "1\n1\n1\n7\n3\n");

        main.run();

        String output = systemOut().getHistory();
        assertTrue(output.contains("Successfully checked in!"));
        assertTrue(output.contains("Now Screening: "));
        assertTrue(output.contains("Goodbye!"));
        assertEquals("Ana", system.currentPassenger(
            SecuritySystem.ScreeningLane.STANDARD).getName());
    }


    // ----------------------------------------------------------
    /**
     * Tests the main method, which reads from the real keyboard.
     * setSystemIn fakes the keyboard so the user exits right away.
     */
    public void testMainMethod()
    {
        setSystemIn("3\n");

        Main.main(new String[0]);

        assertTrue(systemOut().getHistory().contains("Goodbye!"));
    }

    // ----------------------------------------------------------
    /**
     * Tests that preLoadedPassengers adds the 8 demo passengers:
     * 3 priority and 5 standard.
     */
    public void testPreLoadedPassengers()
    {
        Main.preLoadedPassengers(system);

        assertEquals(8, system.getCurrWaitingCount());
        assertEquals(3, system.getLine(
            SecuritySystem.ScreeningLane.PRIORITY).length);
        assertEquals(5, system.getLine(
            SecuritySystem.ScreeningLane.STANDARD).length);
        assertEquals(1, system.getPosition("Jack", "AA123"));
        assertEquals(5, system.getPosition("Bob", "AA123"));
    }
}
