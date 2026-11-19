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
   - Figured out how to handle multiple security lanes by using a generic queue and creating three separate queues from it that inherent the logic
   - Solved estimated wait time model by having a 3 minute final static field and multiplying it by the number of customers in each line

- **What Doesn’t Work Yet?** 
   - Need to finish writing JUnit tests for most classes
   - User quality of life fixes can be made still (‘getPosition’ doesn’t tell the user what line they are in)
   - Need to fix PassengerTest so it compiles and runs correctly






# Next Steps
- **What's Left for the Final Build (in order)**
    - Make `enqueue` and `enqueueFront` reject null, then add the two null tests back to `QueueTest`
    - Block check-in for a passenger who is currently being screened, and add a test for it
    - Decide whether a first failed scan should count in the rescreen stat, and update the code and test to match
    - Replace "SCREENING_ATTEMPT_2" and the other status Strings with constants so `Main` and `SecuritySystem` can't get out of sync
    - Make the wait estimate use the passenger's actual lane
    - Run every test in Eclipse and fix anything Web-CAT flags for style
    - Practice the demo

- **Stretch Goals**
    - Committing to: priority security line, re-screening (now with a second attempt), estimated wait time, and end of day stats (all built)
    - Dropping: average wait time in the end of day stats (would need real timestamps) and a GUI (the console view meets our needs)

- **What Our 3-Minute Demo Will Show**
    - Start the program. It begins with 8 preloaded passengers
    - Passenger menu
        - Check in a new passenger
        - Try a bad flight number like "123" to show the error message instead of a crash
        - Check position and estimated wait time
    - Officer menu
        - View all lines to show the Standard, Priority, and Rescreen lanes
        - Call the next passenger in the Standard lane and screen them
        - **New feature:** when a passenger fails, show the "sent back to the front" message, then view all lines to show them at the front of their own lane
        - Call and screen them again. If they fail a second time, show them land in the Rescreen lane
        - Show the end of day stats
    - Back to the start menu and quit
    - Note: failing is random (50%), so we may need to screen a few passengers before the feature shows up

- **Who Does What**
    - Ethan
        - Update all five test classes for any final changes
        - Null and duplicate check-in tests
        - SecurityView changes, if any are needed
    - [TODO: Jack or Tiffany]
        - Null fix in `Queue`
        - Duplicate check-in fix in `SecuritySystem`
    - [TODO: Jack or Tiffany]
        - Status constants, rescreen stat decision, and the lane-based wait estimate
    - Whole team
        - Demo practice and the final README
# Attribution

| File | Who | How |
|---|---|---|
| `Queue.java` | Tiffany, Jack | Co-authors | Tiffany wrote original class, Jack added one method to make compatible with the retry screening change feature
| `Passenger.java` | Tiffany | Tiffany is the author. Jack made minor changes so the test class would run|
| `SecuritySystem.java` | Jack | Author | Added method and made some edits to current methods to implement change feature
| `SecurityView.java` | Ethan | Ethan wrote the menus and input loops. Jack revised it to take the shared `Scanner` and added the methods `Main` needs (`readText`, `readLane`, `showLine`, `showError`, `showStats`) |
| `Main.java` | Jack | Author | Added preLoadedPassengers in D4 to make running an exmaple easier/better
| `QueueTest.java` | Ethan | Author |
| `PassengerTest.java` | Tiffany | Author |
| `SecuritySystemTest.java` | Ethan | Wrote, GenAI-assisted |
| `SecurityViewTest.java` | Ethan | Wrote, GenAI-assisted |
| `MainTest.java` | Ethan | Wrote, GenAI-assisted |


# Gen AI Usage
   - Gen AI was used to help brainstorm and draft some classes and methods. All suggestions were manually reviewed and manually inputed with user changes made when seen fit. We found AI to be particularly useful when writing the controller class, as it was able to decipher minor changes that needed to be made to other team members classes, and suggesting the best way to combine everything.
