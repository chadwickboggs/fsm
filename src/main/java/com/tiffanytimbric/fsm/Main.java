package com.tiffanytimbric.fsm;


import java.util.stream.Collectors;


public class Main {

    public static void main(String[] args) {
        final FiniteStateMachine fsm = createFsm("Test FSM");

        final String fsmJson = fsm.toJson();
        System.out.printf("FSM JSON Definition:%n%s%n", fsmJson);

        System.out.printf("%n%s: Start%n", fsm.getName());
        State currentState = fsm.getCurrentState();
        System.out.printf("\tCurrent State: \"%s\"%n", currentState.name());
        System.out.printf("\tSupported Events: %s%n%n", currentState.getEvents().stream().map(Event::name).collect(Collectors.toList()));

        String eventName = "Lunch Time";
        System.out.printf("\tSending event: \"%s\"%n", eventName);
        fsm.handleEvent(eventName);
        currentState = fsm.getCurrentState();
        System.out.printf("\tCurrent State: \"%s\"%n", currentState.name());
        System.out.printf("\tSupported Events: %s%n", currentState.getEvents().stream().map(Event::name).collect(Collectors.toList()));

        eventName = "Bus Arrives";
        System.out.printf("%n\tSending unknown event: \"%s\"%n", eventName);
        fsm.handleEvent(eventName);
        currentState = fsm.getCurrentState();
        System.out.printf("\tCurrent State: \"%s\"%n", currentState.name());
        System.out.printf("\tSupported Events: %s%n", currentState.getEvents().stream().map(Event::name).collect(Collectors.toList()));

        eventName = "Afternoon Shift Start";
        System.out.printf("%n\tSending event: \"%s\"%n", eventName);
        fsm.handleEvent(eventName);
        currentState = fsm.getCurrentState();
        System.out.printf("\tCurrent State: \"%s\"%n", currentState.name());
        System.out.printf("\tSupported Events: %s%n", currentState.getEvents().stream().map(Event::name).collect(Collectors.toList()));

        System.out.printf("%s: End%n", fsm.getName());

        final FiniteStateMachine fsmFromJson = FiniteStateMachine.fromJson(fsmJson);
        System.out.printf("%nFSM From-JSON JSON Definition:%n%s%n", fsmFromJson.toJson());
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
