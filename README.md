# Akka FSM using Akka Typed illustration
- Illustrates akka typed FSM via a Car example.
- *NOTE* you need SBT installed, 
- Also need - scala 2.12.9 version installed.
- `CarFSM` flips from -
    - `preEntry` state
    - `running` state
    - `parked` state
 - Uses a rather trivialized Event, Data hierarchy to illustrate
 concepts of FSM flipping states using Events and data in general.
 - Note as documentation states, the `State` is maintained via `Behavior[EventLike]`.
 - An event in conjunction with data may cause State to flip or remain the same with some adjustments in data
 via a recursive call.
 
 ## Testing
 - run `sbt test` to run all tests.
 - you may configure `logback-test.xml` to have outputs more adjusted.
 - for Intellij users you may run scala test as is.
 
 ## Miscellaneous
 - scalafmt for formatting
 - please PR for any improvements to this example, this is still WIP
 and I may push more of test scenarios to show a final version.  