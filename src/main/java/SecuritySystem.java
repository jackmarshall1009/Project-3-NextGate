import java.util.Random;

public class SecuritySystem {
    public enum ScreeningLane {STANDARD, PRIORITY, RESCREEN}

    public static final int MINUTES_PER_PASSENGER = 3;
    
    private Queue<Passenger> standardQueue;
    private Queue<Passenger> priorityQueue;
    private Queue<Passenger> rescreenQueue;

    private Passenger currentStandardPassenger;
    private Passenger currentPriorityPassenger;
    private Passenger currentRescreenPassenger;

    private int nextQueueNumber;
    private int screenedCount;
    private int rescreenCount;
    private int cancelCount;
    private int noShowCount;

    private Random random = new Random();
    private int rescreenChance = 10;
    

    public SecuritySystem() {
        standardQueue = new Queue<>();
        priorityQueue = new Queue<>();
        rescreenQueue = new Queue<>();
        nextQueueNumber = 1;
    }

   // Lane helper methods

    private Queue<Passenger> getQueue(ScreeningLane lane) {
        if (lane == ScreeningLane.PRIORITY) {
            return priorityQueue;
        } else if (lane == ScreeningLane.RESCREEN) {
            return rescreenQueue;
        } else {
            return standardQueue;
        }
    }

    public Passenger currentPassenger(ScreeningLane lane) {
        if (lane == ScreeningLane.PRIORITY) {
            return currentPriorityPassenger;
        } else if (lane == ScreeningLane.RESCREEN) {
            return currentRescreenPassenger;
        } else {
            return currentStandardPassenger;
        }
    }

    private void setCurrent(ScreeningLane lane, Passenger p) {
        if (lane == ScreeningLane.PRIORITY) {
            currentPriorityPassenger = p;
        } else if (lane == ScreeningLane.RESCREEN) {
            currentRescreenPassenger = p;
        } else {
            currentStandardPassenger = p;
        }
    }
// Check-in and Cancellation

    public int checkIn(String name, String flightNumber, boolean priority) {
        if (name == null || name.trim().isEmpty()
            || flightNumber == null || flightNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Please provide both name and flight number.");
        }
        name = name.trim();
        flightNumber = flightNumber.trim().toUpperCase();

        if (!flightNumber.matches("[A-Z]{2}\\d{1,4}")) {
            throw new IllegalArgumentException("Invalid flight number format. Ex: AA1234");
        }

        int position = getPosition(name, flightNumber);
        if (position != -1) {
            throw new IllegalArgumentException("Passenger already checked in at position " + position);
        }

        Passenger p = new Passenger(name, flightNumber, nextQueueNumber, priority);
        p.setStatus("WAITING");
        nextQueueNumber++;

        if (priority) {
            priorityQueue.enqueue(p);
        } else {
            standardQueue.enqueue(p);
        }
        return p.getQueueNumber();
    }

    public boolean cancel(String name, String flightNumber) {
        Passenger p = new Passenger(name.trim(), flightNumber.trim().toUpperCase(), 0,false);

        if (p.equals(currentStandardPassenger) || p.equals(currentPriorityPassenger) || p.equals(currentRescreenPassenger)) {
            throw new IllegalStateException("Current passenger is currently being screened and cannot be canceled.");
        }

        if (standardQueue.remove(p) || priorityQueue.remove(p) || rescreenQueue.remove(p)) {
            cancelCount++;
            return true;
        }
        return false;
    }

    public int getPosition(String name, String flightNumber) {
        Passenger p = new Passenger(name.trim(), flightNumber.trim().toUpperCase(), 0, false);

        if (standardQueue.positionOf(p) != -1) {
            return standardQueue.positionOf(p);
        }
        if (priorityQueue.positionOf(p) != -1) {
            return priorityQueue.positionOf(p);
        }
        return rescreenQueue.positionOf(p);
    }

// Officer Actions

    public Passenger callNext(ScreeningLane lane) {
        if (currentPassenger(lane) != null) {
            throw new IllegalStateException("Cannot call next passenger; clear or rescreen the current passenger first.");
        }
        if (getQueue(lane).isEmpty()) {
            throw new IllegalStateException("No passengers are currently waiting.");
        }
        Passenger p = getQueue(lane).dequeue();
        p.setStatus("SCREENING");
        setCurrent(lane, p);
        return p;
    }

    public Passenger clearPassenger(ScreeningLane lane) {
        Passenger p = currentPassenger(lane);
        if (p == null) {
            throw new IllegalStateException("No passenger is currently being screened.");
        }
        p.setStatus("CLEARED");
        screenedCount++;
        setCurrent(lane, null);
        return p;
    }

    public Passenger sendToRescreen(ScreeningLane lane) {
        Passenger p = currentPassenger(lane);
        if (p == null) {
            throw new IllegalStateException("No passenger is currently being screened.");
        }
        p.setStatus("RESCREENING");
        rescreenCount++;
        rescreenQueue.enqueue(p);
        setCurrent(lane, null);
        return p;
    }

    public Passenger screenPassenger(ScreeningLane lane) {
        if (random.nextInt(100) < rescreenChance) {
            return sendToRescreen(lane);
        }
        return clearPassenger(lane);
    }

    public void setRescreenChance(int percent) {
        rescreenChance = percent;
    }

    public String markNoShow(ScreeningLane lane) {
        Passenger p = currentPassenger(lane);
        if (p == null) {
            throw new IllegalStateException(
                "Nobody is being screened right now.");
        }
        p.setStatus("NO_SHOW");
        noShowCount++;
        setCurrent(lane, null);
        return p.getName() + " was marked as a no-show.";
    }
// Stats and Display

    public String[] getLine(ScreeningLane lane) {
        Object[] items = getQueue(lane).toArray();
        String[] line = new String[items.length];
        for (int i = 0; i < items.length; i++) {
            line[i] = items[i].toString();
        }
        return line;
    }

    public int getCurrWaitingCount() {
        return standardQueue.size() + priorityQueue.size() + rescreenQueue.size();
    }

    public int estimateWaitTime(int position, ScreeningLane lane) {
        if (position < 1) {
            throw new IllegalArgumentException("Position must be greater than or equal to 1.");
        }
        return position * MINUTES_PER_PASSENGER;
    }

    public int totalScreenedCount() {
        return screenedCount;
    }

    public int totalRescreenCount() {
        return rescreenCount;
    }

    public int totalCancelCount() {
        return cancelCount;
    }

    public int totalNoShowCount() {
        return noShowCount;
    }

}
