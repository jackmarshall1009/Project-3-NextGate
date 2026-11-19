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
- **How Did Our Design Absorb the Change?**
    - Classes the feature actually touched
        - `Queue`: added `enqueueFront(T newEntry)`
        - `SecuritySystem`: added a new `retryScreening` method, changed `screenPassenger` to decide between a second attempt and the Rescreen lane, and changed `callNext` so it keeps the "SCREENING_ATTEMPT_2" status
        - `Main`: added a third message for officer option 2, "[name] has been sent back to the front to be re-screened again"
    - What matched our D3 prediction
        - `Queue`, `SecuritySystem`, and `Main` changed, just like we predicted
        - `Queue` only gained one new method and none of its old methods changed, so the deque idea from our scope fit in cleanly
    - What surprised us
        - We predicted `sendToRescreen` would change, but it didn't. Instead we added a separate `retryScreening` method and put the decision in `screenPassenger`. This kept `sendToRescreen` doing one job
        - We predicted `SecurityView` would change (`showConfirmation`), but it didn't need to. `showConfirmation` already prints any message, so the new message went through it with no changes
        - `Passenger` changed even though the feature didn't need it: [TODO: why, e.g. the status now starts as "Waiting", `getFlightnumber` was renamed to `getFlightNumber`, and `equals` now ignores capitalization]

- **Did the Change Cross the Model/View Line?**
    - No. `SecurityView` didn't have to learn anything new about the model. The officer still picks a lane with `readLane` and sees the result through `showConfirmation`
    - One small ripple: `Main` has to know the exact status String "SCREENING_ATTEMPT_2" to pick the right message. If someone renamed that status in `SecuritySystem`, `Main` would quietly print the wrong message without an error. A constant or a method like `isSecondAttempt()` in the model would fix that

- **What Changed From Our D3 Plan?**
    - Feature: same feature as planned (send rescreened passengers back to the front), no switch
    - Signatures
        - New public method `retryScreening(ScreeningLane lane)` instead of putting the logic inside `sendToRescreen`
        - The status is called "SCREENING_ATTEMPT_2" instead of "SECOND_PASS"
    - Logic
        - The decision between a second attempt and the Rescreen lane lives in `screenPassenger`
        - A first failed scan doesn't count toward the rescreen stat. Only a second failed scan does
        - The rescreen chance went from 10% to 50% so the feature shows up more often during testing and the demo
        - Added `preLoadedPassengers` in `Main` so the demo starts with 8 passengers already in line
    - Validation
        - `readText` in the view no longer rejects blank input. Blank names and flight numbers are now caught by `checkIn` in the model, which shows an error instead of crashing
        - `Passenger.equals` now ignores capitalization, so "ANA" and "Ana" on the same flight count as the same person
    - Tests
        - We planned tests for `Queue` rejecting null, but `Queue` doesn't reject null yet, so those tests aren't included
        - We planned a test that both failed scans add to the rescreen count. That doesn't match how the code works now, so the test checks that only the second one counts
        - Added `PassengerTest`, `SecurityViewTest`, and `MainTest`, which weren't in the D3 plan

- **What Doesn't Work Yet?**
    - `Queue` doesn't reject null. If null is ever added, `positionOf` and `remove` crash. The app itself never adds null, so this can't happen through the menus
    - A passenger who is currently being screened can check in again, because the duplicate check only looks at the waiting lines
    - A first failed scan isn't counted in the end of day stats
    - The wait estimate always uses the Standard lane, so priority and rescreen passengers see the same estimate as standard passengers at the same position
    - Whether a passenger fails is random, so during a demo the second attempt might not show up right away
    - [TODO: anything else the team knows about]






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
    - Jack:
        - Null fix in `Queue`
        - Duplicate check-in fix in `SecuritySystem`
    - Tiffany
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
