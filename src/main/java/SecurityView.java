import java.util.Scanner;

/**
 *  The view for the Next Gate Security System. 
 *  It handles everything the user sees and types. It never changes the 
 *  queue itself
 * 
 *  @author Ethan
 *  @version Nov 11, 2026
 */

public class SecurityView 
{
    private static final int START_OPTIONS = 2;
    private static final int OFFICER_OPTIONS = 6;
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
        System.out.println("Welcome to Next Gate Security System!\n\nPlease "
            + "specify user interface type:\n\n1. Security Officer\n\n2. "
            + "Passenger");
        System.out.println();
        System.out.println("Please specify user type:");
        System.out.println("1. Security Officer");
        System.out.println("2. Passenger");
    }
    
    /**
     * Prints the menu of actions for officer
     */
    public void showOfficerMenu()
    {
        System.out.println();
        System.out.println("Security Officer Menu");
        System.out.println("1. Screen next passenger");
        System.out.println("2. View next passenger");
        System.out.println("3. View full line");
        System.out.println("4. Remove a passenger");
        System.out.println("5. View line size");
        System.out.println("6. Exit");
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
        System.out.println("Passenger Menu");
        System.out.println("1. Join the line");
        System.out.println("2. Check my position");
        System.out.println("3. Leave the line");
        System.out.println("4. Exit");
        
    }
    
    /**
     * Prints the passenger menu
     */
    public int getPassengerChocie()
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
            System.out.println("Enter a choice (1 -" + max + "): ");
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
     * Prints one passenger, or a message if there isn't one
     */
    public void showPassenger(Passenger passenger)
    {
        if (passenger == null)
        {
            System.out.println("There is no passenger to show.");
            
        }
        else
        {
            System.out.println(passenger.toString());
        }
    }
    
    /**
     * Print every passenger in the line from front to back
     * 
     */
    public void showLine(Queue<Passenger> line)
    {
        if (line.isEmpty())
        {
            System.out.println("The line is empty.");
            return;
        }
        
        Object[] passengers = line.toArray();
        
        System.out.println("Current Line");
        for (int i = 0; i < passengers.length; i++)
        {
            System.out.println((i+1) + ". " + passengers[i]);
            System.out.println();
        }
    }
    
    /**
     * Prints where a passenger is in line
     * 
     */
    public void showPosition(int position)
    {
        if (position == -1)
        {
            System.out.println("You are not in the line.");
        }
        else
        {
            System.out.println("You are number " + position + " in line.");
        }
    }
    
    /**
     * Prints how many passengers are waitiing
     * 
     */
    public void showLineSize(int size)
    {
        System.out.println("Passengers waiting: " + size);
    }
    
    /**
     * Prints any message, like a confirmation or error.
     * 
     */
    public void showMessage(String message)
    {
        System.out.println(message);
    }
    
    /**
     * Prints the goodbye message 
     * 
     */
    public void showGoodbye()
    {
        System.out.println("Thnak you for using Next Gate. Goodbye!");
    }
}
