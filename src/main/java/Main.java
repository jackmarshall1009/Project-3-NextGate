import java.util.Scanner;

public class Main {
    private SecuritySystem system;
    private SecurityView view;

    public Main(SecuritySystem system, SecurityView view) {
        this.system = system;
        this.view = view;
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Main app = new Main(new SecuritySystem(), new SecurityView(input));
        app.run();
    }

    // Start Menu

    public void run() {
        boolean running = true;
        while (running) {
            int choice = view.getStartChoice();
            if (choice == 1) {
                officerLoop();
            } else if (choice == 2) {
                passengerLoop();
            } else {
                running = false;
            }
        }
        view.showGoodbye();
    }

    private void officerLoop() {
        boolean inMenu = true;
        while (inMenu) {
            inMenu = handleOfficerCommand(view.getOfficerChoice());
        }
    }

    private void passengerLoop() {
        boolean inMenu = true;
        while (inMenu) {
            inMenu = handlePassengerCommand(view.getPassengerChoice());
        }
    }

    // Officer Commands
    public boolean handleOfficerCommand(int choice) {
        try {
            switch (choice) {
                case 1:
                    Passenger called = system.callNext(view.readLane());
                    view.showConfirmation("Now Screening: " + called);
                    break;
                case 2:
                    Passenger p = system.screenPassenger(view.readLane());
                    if (p.getStatus().equals("CLEARED")) {
                        view.showConfirmation(p.getName() + " has been cleared.");
                    } else {
                        view.showConfirmation(p.getName() + " has been sent to be rescreened.");
                    }
                    break;
                case 3:
                    view.showConfirmation(system.markNoShow(view.readLane()));
                    break;
                case 4:
                    showAllLines();
                    break;
                case 5:
                    view.showLineSize(system.getCurrWaitingCount());
                    break;
                case 6:
                    view.showStats(system.totalScreenedCount(),
                            system.totalRescreenCount(),
                            system.totalCancelCount(),
                            system.totalNoShowCount());
                    break;
                default:
                    return false;
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            view.showError(e.getMessage());
        }
        return true;
    }

    // Passenger Commands

    public boolean handlePassengerCommand(int choice) {
        try {
            switch (choice) {
                case 1:
                    String name = view.readText("Please enter your name");
                    String flight = view.readText("Please enter your flight number");
                    boolean priority = view.readYesNo("Are you priority eligible?");
                    int number = system.checkIn(name, flight, priority);
                    view.showConfirmation("Successfully checked in! Your queue number is "
                            + number + ".");
                    break;
                case 2:
                    int position = system.getPosition(
                            view.readText("Please enter your name"),
                            view.readText("Please enter your flight number"));
                    int wait = 0;
                    if (position != -1) {
                        wait = system.estimateWaitTime(position,
                                SecuritySystem.ScreeningLane.STANDARD);
                    }
                    view.showPosition(position, wait);
                    break;
                case 3:
                    boolean removed = system.cancel(
                            view.readText("Please enter your name"),
                            view.readText("Please enter your flight number"));
                    if (removed) {
                        view.showConfirmation("Your reservation was cancelled.");
                    } else {
                        view.showError("Passenger not found.");
                    }
                    break;
                default:
                    return false;
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            view.showError(e.getMessage());
        }
        return true;
    }

    // Show all lines helper

    private void showAllLines() {
        view.showLine("Standard",
                system.currentPassenger(SecuritySystem.ScreeningLane.STANDARD),
                system.getLine(SecuritySystem.ScreeningLane.STANDARD));
        view.showLine("Priority",
                system.currentPassenger(SecuritySystem.ScreeningLane.PRIORITY),
                system.getLine(SecuritySystem.ScreeningLane.PRIORITY));
        view.showLine("Rescreen",
                system.currentPassenger(SecuritySystem.ScreeningLane.RESCREEN),
                system.getLine(SecuritySystem.ScreeningLane.RESCREEN));
    }
}
