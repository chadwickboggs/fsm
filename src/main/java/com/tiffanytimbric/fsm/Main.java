package com.tiffanytimbric.fsm;


import java.util.stream.Collectors;


public class Main {

    public static void main(String[] args) {
        final FiniteStateMachine<String> fsm = createFsm("Test FSM");

        final String fsmJson = fsm.toJson();
        System.out.printf("FSM JSON Definition:%n%s%n", fsmJson);

        System.out.printf("%n%s, %s: Start%n", fsm.getName(), fsm.getDataItem());
        State<String> currentState = fsm.getCurrentState();
        System.out.printf("\tCurrent State: \"%s\", \"%s\"%n", currentState.name(), currentState.dataItem());
        System.out.printf("\tSupported Events: %s%n", currentState.getEvents().stream().map(Event::name).collect(Collectors.toList()));

        sendEventVerbose("Lunch Time", "event_dataArg", fsm);
        sendEventVerbose("Bus Arrives", "event_dataArg", fsm);
        sendEventVerbose("Afternoon Shift Start", "event_dataArg", fsm);
        sendEventVerbose("Postpone Shift Start", "event_dataArg", fsm);
        sendEventVerbose("Afternoon Shift Start", "event_dataArg", fsm);

        System.out.printf("%s: End%n", fsm.getName());

        final FiniteStateMachine<String> fsmFromJson = FiniteStateMachine.fromJson(fsmJson);
        System.out.printf("%nFSM From-JSON JSON Definition:%n%s%n", fsmFromJson.toJson());
    }

    private static <T> void sendEventVerbose(final String eventName, final T event_dataArg, final FiniteStateMachine<T> fsm) {
        System.out.printf("%n\tSending event: \"%s\"%n", eventName);

        fsm.handleEvent(eventName, event_dataArg);

        final State<T> currentState = fsm.getCurrentState();

        System.out.printf("\tCurrent State: \"%s\", \"%s\"%n", currentState.name(), currentState.dataItem());
        System.out.printf("\tSupported Events: %s%n", currentState.getEvents().stream().map(Event::name).collect(Collectors.toList()));
    }

    private static FiniteStateMachine<String> createFsm(final String fsmName) {
        final Event<String> afternoonShiftStartEvent = new Event("Afternoon Shift Start", "event_dataArg");
        final Event<String> postponeShiftStartEvent = new Event("Postpone Shift Start", "event_dataArg");
        final Event<String> lunchTimeEvent = new Event("Lunch Time", "event_dataArg");

        final State<String> endState = new State(
            "Afternoon Shift", "state_dataItem", new Transition<String, String>(
            postponeShiftStartEvent, "At Lunch",
            (fromState, event) -> System.out.printf(
                "\thandleEvent(\"%s\", \"%s\"): Transitioning To State \"%s\".%n",
                event.name(), event.dataArg(), "At Lunch")));

        final State<String> lunchState = new State(
            "At Lunch", "state_dataItem", new Transition<>(
            afternoonShiftStartEvent, endState,
            (fromState, event) -> System.out.printf(
                "\thandleEvent(\"%s\", \"%s\"): Transitioning To State \"%s\".%n",
                event.name(), event.dataArg(), endState.name())));

        final State<String> initialState = new State(
            "Morning Shift", "state_dataItem", new Transition<>(
            lunchTimeEvent, lunchState,
            (fromState, event) -> System.out.printf(
                "\thandleEvent(\"%s\", \"%s\"): Transitioning To State \"%s\".%n",
                event.name(), event.dataArg(), lunchState.name())));

        return new FiniteStateMachine<>(fsmName, "fsm_dataItem", initialState);
    }
}
