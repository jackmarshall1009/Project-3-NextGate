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
    -enqueueFront(T newEntry)

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
