package com.tiffanytimbric.fsm;


public class Main {

    public static void main(String[] args) {
        final FiniteStateMachine fsm = createFsm("Test FSM");
        System.out.println("FSM Definition:" + fsm);

        System.out.printf("%n%s: Start%n", fsm.getName());

        System.out.printf("\tCurrent State: \"%s\"%n", fsm.getCurrentState().name());

        fsm.handleEvent("Lunch Time");
        System.out.printf("\tCurrent State: \"%s\"%n", fsm.getCurrentState().name());

        final String eventName = "Bus Arrives";
        System.out.printf("%nSending unknown event: \"%s\"%n", eventName);
        fsm.handleEvent(eventName);
        System.out.printf("\tCurrent State: \"%s\"%n", fsm.getCurrentState().name());
        System.out.println();

        fsm.handleEvent("Afternoon Shift Start");
        System.out.printf("\tCurrent State: \"%s\"%n", fsm.getCurrentState().name());

        System.out.printf("%s: End%n", fsm.getName());
    }

    private static FiniteStateMachine createFsm(final String fsmName) {
        final Event afternoonShiftStartEvent = new Event("Afternoon Shift Start");
        final Event lunchTimeEvent = new Event("Lunch Time");

        final State endState = new State("Afternoon Shift");
        final State lunchState = new State(
            "At Lunch", new Transition(
            afternoonShiftStartEvent, endState,
            event -> System.out.printf("\tEvent: \"%s\" --transition_state-> \"%s\"%n", event.name(), endState.name())));
        final State initialState = new State(
            "Morning Shift", new Transition(
            lunchTimeEvent, lunchState,
            event -> System.out.printf("\tEvent: \"%s\" --transition_state-> \"%s\"%n", event.name(), lunchState.name())));

        return new FiniteStateMachine(fsmName, initialState);
    }
}
