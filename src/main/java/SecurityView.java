import java.util.Scanner;

/**
 *  The view for the Next Gate Security System. 
 *  It handles everything the user sees and types. It never changes the 
 *  queue itself
 * 
 *  @author Ethan
 *  @version 2
 */

public class SecurityView 
{
    private static final int START_OPTIONS = 3;
    private static final int OFFICER_OPTIONS = 7;
    private static final int PASSENGER_OPTIONS = 4;
    
    private Scanner scanner;
    
    /**
     * Creating a new SecurityView
     */
    public SecurityView(Scanner scanner)
    {
        this.scanner = scanner;
    }
    
    
    /**
     * Prints the welcome screen and choices
     */
    public void showStartMenu()
    {
        System.out.println();
        System.out.println("Welcome to Next Gate Security System!");
        System.out.println("Please specify user type:");
        System.out.println("1. Security Officer");
        System.out.println("2. Passenger");
        System.out.println("3. Quit");
    }

    /**
     * Shows the start menu and returns the user's choice.
     */
    public int getStartChoice() 
    {
        showStartMenu();
        return readChoice(START_OPTIONS);
    }
    
    /**
     * Prints the menu of actions for officer
     */
    public void showOfficerMenu()
    {
        System.out.println();
        System.out.println("Security Officer Menu");
        System.out.println("1. Call next passenger");
        System.out.println("2. Screen current passenger");
        System.out.println("3. Mark no-show");
        System.out.println("4. View all lines");
        System.out.println("5. View line size");
        System.out.println("6. View end of day statistics");
        System.out.println("7. Back");
    }
    
    /**
     * Prints the officer menu
     */
    public int getOfficerChoice()
    {
        showOfficerMenu();
        return readChoice(OFFICER_OPTIONS);
    }
    
    /**
     * Prints the passenger actions
     */
    public void showPassengerMenu()
    {
        System.out.println();
        System.out.println("Passenger Menu");
        System.out.println("1. Join the line");
        System.out.println("2. Check my position");
        System.out.println("3. Leave the line");
        System.out.println("4. Back");
    }
    
    /**
     * Prints the passenger menu
     */
    public int getPassengerChoice()
    {
        showPassengerMenu();
        return readChoice(PASSENGER_OPTIONS);
    }
    
    /**
     * Keeps asking until the user types a whole number from 1 to max.
     * Letters, blanks, and out of range numbers are rejected.  
     */
    private int readChoice(int max)
    {
        int choice = -1;
        boolean valid = false;
        
        while (!valid)
        {
            System.out.print("Enter a choice (1-" + max + "): ");
            String input = scanner.nextLine().trim();
            
            try
            {
                choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= max)
                {
                    valid = true;
                }
                else
                {
                    System.out.println("Please enter a number from 1 to " + max
                         + ".");
                }
            }
            catch (NumberFormatException e)
            {
                System.out.println("That is not a number. Try again.");
            }
        
        }
        return choice;
    }
    
    /**
     * Asks a yes or no question
     */
    public boolean readYesNo(String prompt)
    {
        while (true)
        {
            System.out.print(prompt + " (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();
            
            if (input.equals("y") || input.equals("yes"))
            {
                return true;
            }
            else if (input.equals("n") || input.equals("no"))
            {
                return false;
            }
            System.out.println("Please type y or n.");
        }
    }

    /**
     * Prints a prompt and returns what the user typed.
     */
    public String readText(String prompt)
    {
        System.out.print(prompt + ": ");
        return scanner.nextLine().trim();
    }

    /**
     * Asks which screening lane to use
     */
    public SecuritySystem.ScreeningLane readLane()  
    {
        System.out.println("Which lane?");
        System.out.println("1. Standard");
        System.out.println("2. Priority");
        System.out.println("3. Rescreen");
        int choice = readChoice(3);

        if (choice == 2)
        {
            return SecuritySystem.ScreeningLane.PRIORITY;
        }
        else if (choice == 3)
        {
            return SecuritySystem.ScreeningLane.RESCREEN;
        }
        return SecuritySystem.ScreeningLane.STANDARD;
    }
    
    /**
     * Prints one passenger, or a message if there isn't one
     */
    public void showPassenger(Passenger passenger)
    {
        if (passenger == null)
        {
            System.out.println("No passenger is currently being screened.");
            
        }
        else
        {
            System.out.println(passenger.toString());
        }
    }
    
    /**
     * Prints one lane: who is being screened and who is waiting
     */
    public void showLine(String laneName, Passenger current, String[] line)
    {
        System.out.println();
        System.out.println("--- " + laneName + " Lane ---");
        System.out.print("Now screening: ");
        showPassenger(current);

        if (line.length == 0)
        {
            System.out.println("The line is empty.");
            return;
        }
        for (int i = 0; i < line.length; i++)
        {
            System.out.println((i + 1) + ". " + line[i]);
        }
    }
    
    /**
     * Prints where a passenger is in line and shows estimated wait time
     * 
     */
    public void showPosition(int position, int waitMinutes)
    {
        if (position == -1)
        {
            System.out.println("You are not in the line.");
        }
        else
        {
            System.out.println("You are number " + position + " in line.");
            System.out.println("Estimated wait time: " + waitMinutes + " minutes.");
        }
    }
    
    /**
     * Prints how many passengers are waiting
     * 
     */
    public void showLineSize(int size)
    {
        System.out.println("Passengers waiting: " + size);
    }

    /**
     * Prints an error
     */
    public void showError(String message)
    {
        System.out.println("ERROR: " + message);
    }

    /**
     * Prints the end of day summary
     */
    public void showStats(int screened, int rescreened, int cancelled, int noShows)
    {
        System.out.println();
        System.out.println("End of Day Stats");
        System.out.println("Screened:   " + screened);
        System.out.println("Rescreened: " + rescreened);
        System.out.println("Cancelled:  " + cancelled);
        System.out.println("No-shows:   " + noShows);
    }
    
    /**
     * Prints any confirmation messages
     * 
     */
    public void showConfirmation(String message)
    {
        System.out.println(message);
    }
    
    /**
     * Prints the goodbye message 
     * 
     */
    public void showGoodbye()
    {
        System.out.println("Thank you for using Next Gate. Goodbye!");
    }
}
