import java.util.Scanner;

/**
 * Tests for the SecurityView class (the view) in Next Gate. Each
 * test fakes what the user types by giving the view a Scanner that
 * reads from a String, then checks what was printed with
 * systemOut().getHistory(). Most of these are bad-input tests.
 *
 * @author Ethan
 * @version Nov 19, 2026
 */
public class SecurityViewTest extends student.TestCase
{
    /**
     * Makes a view that "types" the given input. Each \n is the
     * user pressing Enter.
     *
     * @param input everything the user will type
     * @return a view that reads from that input
     */
    private SecurityView makeView(String input)
    {
        return new SecurityView(new Scanner(input));
    }


    // ----------------------------------------------------------
    /**
     * Tests that the start menu prints all three options and a
     * valid choice is returned.
     */
    public void testGetStartChoice()
    {
        SecurityView view = makeView("2\n");

        assertEquals(2, view.getStartChoice());

        String output = systemOut().getHistory();
        assertTrue(output.contains("Welcome to Next Gate"));
        assertTrue(output.contains("1. Security Officer"));
        assertTrue(output.contains("2. Passenger"));
        assertTrue(output.contains("3. Quit"));
    }


    // ----------------------------------------------------------
    /**
     * Tests that letters are rejected and the view asks again.
     */
    public void testChoiceRejectsLetters()
    {
        SecurityView view = makeView("abc\n1\n");

        assertEquals(1, view.getStartChoice());
        assertTrue(systemOut().getHistory().contains(
            "That is not a number. Try again."));
    }


    // ----------------------------------------------------------
    /**
     * Tests that a blank line is rejected and the view asks again.
     */
    public void testChoiceRejectsBlank()
    {
        SecurityView view = makeView("\n   \n3\n");

        assertEquals(3, view.getStartChoice());
        assertTrue(systemOut().getHistory().contains(
            "That is not a number. Try again."));
    }


    // ----------------------------------------------------------
    /**
     * Tests that numbers too low or too high are rejected, and the
     * highest valid number (3) is accepted.
     */
    public void testChoiceRejectsOutOfRange()
    {
        SecurityView view = makeView("0\n4\n-1\n3\n");

        assertEquals(3, view.getStartChoice());
        assertTrue(systemOut().getHistory().contains(
            "Please enter a number from 1 to 3."));
    }


    // ----------------------------------------------------------
    /**
     * Tests that a decimal number is rejected.
     */
    public void testChoiceRejectsDecimal()
    {
        SecurityView view = makeView("1.5\n1\n");

        assertEquals(1, view.getStartChoice());
        assertTrue(systemOut().getHistory().contains(
            "That is not a number. Try again."));
    }


    // ----------------------------------------------------------
    /**
     * Tests that spaces around a number are ignored.
     */
    public void testChoiceTrimsSpaces()
    {
        SecurityView view = makeView("  2  \n");

        assertEquals(2, view.getStartChoice());
    }


    // ----------------------------------------------------------
    /**
     * Tests the officer menu: it prints every option, accepts 7,
     * and rejects 8.
     */
    public void testGetOfficerChoice()
    {
        SecurityView view = makeView("8\n7\n");

        assertEquals(7, view.getOfficerChoice());

        String output = systemOut().getHistory();
        assertTrue(output.contains("Security Officer Menu"));
        assertTrue(output.contains("1. Call next passenger"));
        assertTrue(output.contains("2. Screen current passenger"));
        assertTrue(output.contains("6. View end of day statistics"));
        assertTrue(output.contains("7. Back"));
        assertTrue(output.contains("Please enter a number from 1 to 7."));
    }


    // ----------------------------------------------------------
    /**
     * Tests the passenger menu: it prints every option, accepts 4,
     * and rejects 5.
     */
    public void testGetPassengerChoice()
    {
        SecurityView view = makeView("5\n4\n");

        assertEquals(4, view.getPassengerChoice());

        String output = systemOut().getHistory();
        assertTrue(output.contains("Passenger Menu"));
        assertTrue(output.contains("1. Join the line"));
        assertTrue(output.contains("2. Check my position"));
        assertTrue(output.contains("3. Leave the line"));
        assertTrue(output.contains("4. Back"));
        assertTrue(output.contains("Please enter a number from 1 to 4."));
    }


    // ----------------------------------------------------------
    /**
     * Tests that readLane returns the right lane for 1, 2, and 3.
     */
    public void testReadLane()
    {
        SecurityView view = makeView("1\n2\n3\n");

        assertEquals(SecuritySystem.ScreeningLane.STANDARD,
            view.readLane());
        assertEquals(SecuritySystem.ScreeningLane.PRIORITY,
            view.readLane());
        assertEquals(SecuritySystem.ScreeningLane.RESCREEN,
            view.readLane());
        assertTrue(systemOut().getHistory().contains("Which lane?"));
    }


    // ----------------------------------------------------------
    /**
     * Tests that readLane rejects bad input before taking a valid
     * lane.
     */
    public void testReadLaneBadInput()
    {
        SecurityView view = makeView("x\n9\n2\n");

        assertEquals(SecuritySystem.ScreeningLane.PRIORITY,
            view.readLane());

        String output = systemOut().getHistory();
        assertTrue(output.contains("That is not a number. Try again."));
        assertTrue(output.contains("Please enter a number from 1 to 3."));
    }


    // ----------------------------------------------------------
    /**
     * Tests that readText prints the prompt and removes extra
     * spaces.
     */
    public void testReadText()
    {
        SecurityView view = makeView("  Ana  \n");

        assertEquals("Ana", view.readText("Please enter your name"));
        assertTrue(systemOut().getHistory().contains(
            "Please enter your name: "));
    }


    // ----------------------------------------------------------
    /**
     * Tests that readText returns an empty String for a blank
     * line. The view does not reject blanks itself; the model's
     * checkIn rejects them instead.
     */
    public void testReadTextBlank()
    {
        SecurityView view = makeView("   \n");

        assertEquals("", view.readText("Name"));
    }


    // ----------------------------------------------------------
    /**
     * Tests that readYesNo accepts y, yes, n, and no in any
     * capitalization.
     */
    public void testReadYesNo()
    {
        SecurityView view = makeView("y\nYES\nn\nNo\n");

        assertTrue(view.readYesNo("Priority?"));
        assertTrue(view.readYesNo("Priority?"));
        assertFalse(view.readYesNo("Priority?"));
        assertFalse(view.readYesNo("Priority?"));
        assertTrue(systemOut().getHistory().contains("Priority? (y/n): "));
    }


    // ----------------------------------------------------------
    /**
     * Tests that readYesNo rejects anything that is not yes or no.
     */
    public void testReadYesNoBadInput()
    {
        SecurityView view = makeView("maybe\n\n1\ny\n");

        assertTrue(view.readYesNo("Priority?"));
        assertTrue(systemOut().getHistory().contains(
            "Please type y or n."));
    }


    // ----------------------------------------------------------
    /**
     * Tests showPassenger with a passenger and with null.
     */
    public void testShowPassenger()
    {
        SecurityView view = makeView("");
        Passenger ana = new Passenger("Ana", "AA100", 1, false);

        view.showPassenger(ana);
        view.showPassenger(null);

        String output = systemOut().getHistory();
        assertTrue(output.contains("Passenger: Ana"));
        assertTrue(output.contains(
            "No passenger is currently being screened."));
    }


    // ----------------------------------------------------------
    /**
     * Tests showLine for an empty lane with nobody being screened.
     */
    public void testShowLineEmpty()
    {
        SecurityView view = makeView("");

        view.showLine("Standard", null, new String[0]);

        String output = systemOut().getHistory();
        assertTrue(output.contains("--- Standard Lane ---"));
        assertTrue(output.contains(
            "Now screening: No passenger is currently being screened."));
        assertTrue(output.contains("The line is empty."));
    }


    // ----------------------------------------------------------
    /**
     * Tests showLine with someone being screened and two people
     * waiting.
     */
    public void testShowLineWithPassengers()
    {
        SecurityView view = makeView("");
        Passenger ana = new Passenger("Ana", "AA100", 1, false);
        String[] waiting = {"Ben's info", "Cal's info"};

        view.showLine("Priority", ana, waiting);

        String output = systemOut().getHistory();
        assertTrue(output.contains("--- Priority Lane ---"));
        assertTrue(output.contains("Now screening: Passenger: Ana"));
        assertTrue(output.contains("1. Ben's info"));
        assertTrue(output.contains("2. Cal's info"));
        assertFalse(output.contains("The line is empty."));
    }


    // ----------------------------------------------------------
    /**
     * Tests showPosition when the passenger is not in line.
     */
    public void testShowPositionNotFound()
    {
        SecurityView view = makeView("");

        view.showPosition(-1, 0);

        String output = systemOut().getHistory();
        assertTrue(output.contains("You are not in the line."));
        assertFalse(output.contains("Estimated wait time"));
    }


    // ----------------------------------------------------------
    /**
     * Tests showPosition when the passenger is in line.
     */
    public void testShowPositionFound()
    {
        SecurityView view = makeView("");

        view.showPosition(2, 6);

        String output = systemOut().getHistory();
        assertTrue(output.contains("You are number 2 in line."));
        assertTrue(output.contains("Estimated wait time: 6 minutes."));
    }


    // ----------------------------------------------------------
    /**
     * Tests showLineSize and showStats.
     */
    public void testShowLineSizeAndStats()
    {
        SecurityView view = makeView("");

        view.showLineSize(5);
        view.showStats(1, 2, 3, 4);

        String output = systemOut().getHistory();
        assertTrue(output.contains("Passengers waiting: 5"));
        assertTrue(output.contains("End of Day Stats"));
        assertTrue(output.contains("Screened:   1"));
        assertTrue(output.contains("Rescreened: 2"));
        assertTrue(output.contains("Cancelled:  3"));
        assertTrue(output.contains("No-shows:   4"));
    }


    // ----------------------------------------------------------
    /**
     * Tests showConfirmation, showError, and showGoodbye.
     */
    public void testMessages()
    {
        SecurityView view = makeView("");

        view.showConfirmation("Ana has been cleared.");
        view.showError("No passengers are currently waiting.");
        view.showGoodbye();

        String output = systemOut().getHistory();
        assertTrue(output.contains("Ana has been cleared."));
        assertTrue(output.contains(
            "ERROR: No passengers are currently waiting."));
        assertTrue(output.contains(
            "Thank you for using Next Gate. Goodbye!"));
    }
}
