# Project-3-NextGate
Airport security queue system that allows customers to check in ahead of time and see estimated wait time, similar to how a DMV queue is handled.


# Run the full program (thin view + controller)
- **VS Code:** open `src/main/java/Main.java` and click **Run** above `main`.
- **Command line** (from the project root):
  ```
  javac -d bin -cp lib/student.jar src/main/java/*.java
  java -cp bin Main
  ```
- Pick **1** for the Security Officer menu or **2** for the Passenger menu. **Back** returns to the start menu, and **3** on the start menu quits.

# Run the model without the view
- The model (`Queue`, `Passenger`, `SecuritySystem`) never prints or reads input, so it can be run directly from the JUnit tests.
- **VS Code:** open the **Testing** panel (beaker icon) and run `QueueTest`.
- `QueueTest` exercises the queue logic on its own (enqueue, dequeue, peek, remove from the middle, position lookup, and toArray) using both Strings and Passengers.

# Reflection
- **Changes from the Spec**
    - Two menu system for choosing if you are a 'Security Officer' or 'Passenger'
        - Originally was going to be options in one menu but this flowed better we thought
    - Scanner is created in Main and passed into SecurityView which reads the menus and choices
        - Originally the scanner was going to only exist in main but we thought this way will make for easier testing and future modulation
    - Dropped the scanner parameter in the constructor in Main
        - As stated above, is now passed into SecurityView instead
    - Created two handle command methods, one for officer and one for passenger
        - Originally was just going to be a single method but needed to split it when we introduced the two menu system
    - Added a rng of 10% to determine which passengers are sent to rescreening
        - Originally was going to be a manual choice by the officer but we thought this would better simulate screenings
    - In general, added additional helper methods to multiple classes to help make the code more modular
        - Not in spec
   
- **Does the Model Stand Alone?**
        - Yes, the SecuritySystem, Passenger, and Queue classes never print, read input, or reference the view or controller classes. All of the rule checks and logic is handled in the model, and throws exceptions when needed.

- **Unknowns from the Scope**
   - Figured out how to handle multiple security lanes by using a generic queue and creating three separate queues from it that inherit the logic
   - Solved estimated wait time model by having a 3 minute final static field and multiplying it by the number of customers in each line

- **What Doesn’t Work Yet?** 
   - Need to finish writing JUnit tests for most classes
   - User quality of life fixes can be made still (‘getPosition’ doesn’t tell the user what line they are in)
   - Need to fix PassengerTest so it compiles and runs correctly






# Next Steps
- **What’s Left to Finish the MVP**
    - Write SecuritySystemTest
    - Write SecurityViewTest
    - Write MainTest

- **Change Feature**
    - Feature: Send rescreened passengers back to the front for a second pass
    - Right now, a passenger who sets off the scanner goes to the back of the rescreen lane. With this, the first time it happens they go back to the front of their own lane for a quick second pass instead of starting over. If they fail the second pass too, they go to the Rescreen lane like before
    - This came from our scope, which planned a dequeue so travelers who set off the scanner would be sent back to the front instead of the back
    - It is non-trivial because it adds new model logic and the officer reaches it through the controller and view when screening a passenger

- **Classes we predict will change**
    - Queue
        - enqueueFront(T newEntry)
    - SecuritySystem
        - sendToRescreen
        - callNext
    - Main
        - Add a third message “ _____ has been sent back to the front for a second pass”
    - SecurityView
        - showConfirmation

- **New tests**
    - `enqueueFront` on an empty queue, then `peek` and `dequeue` return that item
    - `enqueueFront` on a queue with items puts the new item at position 1 and everything else moves back one
    - `enqueueFront` then `enqueue` still adds to the back correctly
    - With the rescreen chance set to 100, a passenger who fails once is at position 1 of their own lane with status "SECOND_PASS"
    - The same passenger failing a second time ends up in the Rescreen lane instead
    - With the rescreen chance set to 0, a second-pass passenger is cleared normally

- **Stretch Goals**
    - Doing: Priority security line, re-screening, estimated wait time, and end-of-day stats (all built in the prototype)
    - Dropping: Average wait time in the end-of-day stats (would need real timestamps) and a GUI (the console view meets our needs)

- **New Tests Needed**
    - Tests for the fixes listed in What Doesn't Work Yet

- **Who Does What**
    - `SecuritySystemTest`: Ethan
    - `SecurityViewTest` and bad-input cases: Ethan & Tiffany
    - Null and duplicate check-in fixes: Tiffany
    - Change feature (model): Jack & Tiffany
    - Change feature (controller and view): Jack

# Attribution

| File | Who | How |
|---|---|---|
| `Queue.java` | Tiffany | Wrote |
| `Passenger.java` | Tiffany, Jack | Tiffany wrote. Jack renamed `getFlightnumber` to `getFlightNumber` so the tests compile |
| `SecuritySystem.java` | Jack | Wrote, GenAI-assisted |
| `SecurityView.java` | Ethan, Jack | Ethan wrote the menus and input loops. Jack revised it to take the shared `Scanner` and added the methods `Main` needs (`readText`, `readLane`, `showLine`, `showError`, `showStats`), GenAI-assisted |
| `Main.java` | Jack | Wrote, GenAI-assisted |
| `QueueTest.java` | Ethan | Wrote |
| `PassengerTest.java` | Tiffany | Wrote (in progress) |
| `SecuritySystemTest.java` | Ethan | Not yet written |
| `SecurityViewTest.java` | Ethan | Not yet written |
| `MainTest.java` | Ethan | Not yet written |
