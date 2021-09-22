package com.tiffanytimbric.fsm;


public class Main {

    public static void main(String[] args) {
        final FiniteStateMachine fsm = createFsm("Test FSM");
        System.out.printf("%s: Start%n", fsm.getName());

        System.out.printf("\tCurrent State: \"%s\"%n", fsm.getCurrentState().name());

        fsm.handleEvent("Lunch Time");
        System.out.printf("\tCurrent State: \"%s\"%n", fsm.getCurrentState().name());

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
            event -> System.out.printf("\t%s -> %s%n", event, endState.name())));
        final State initialState = new State(
            "Morning Shift", new Transition(
            lunchTimeEvent, lunchState,
            event -> System.out.printf("\t%s -> %s%n", event, lunchState.name())));

        return new FiniteStateMachine(fsmName, initialState);
    }
}
