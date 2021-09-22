package com.tiffanytimbric.fsm;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;


public class FiniteStateMachine {

    private final String name;
    private State currentState;

    public FiniteStateMachine(String fsmName, final State initialState) {
        this.name = fsmName;
        this.currentState = initialState;
    }

    public String getName() {
        return name;
    }

    public State getCurrentState() {
        return currentState;
    }

    public State handleEvent(final String eventName) {
        return handleEvent(new Event(eventName));
    }

    public State handleEvent(final Event event) {
        if (currentState.transitions() == null) {
            return throwUnrecognizedEventIllegalArgumentException(event);
        }

        final Optional<Transition> transitionOpt = Arrays.stream(currentState.transitions())
            .filter(transition -> transition.event().equals(event))
            .findFirst();
        if (transitionOpt.isEmpty()) {
            throwUnrecognizedEventIllegalArgumentException(event);
        }

        final Transition transition = transitionOpt.get();
        transition.handler().accept(event);

        return currentState = transition.toState();
    }

    private State throwUnrecognizedEventIllegalArgumentException(final Event event) {
        throw new IllegalArgumentException(String.format(
            "Unrecognized event.  Current state contains no handler for the specified event."
                + "  Current State: \"%s\", Specified Event: \"%s\"",
            currentState, event
        ));
    }

    @Override
    public String toString() {
        return "FiniteStateMachine{" +
            "name='" + name + '\'' +
            ", currentState=" + currentState +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FiniteStateMachine that = (FiniteStateMachine) o;
        return name.equals(that.name) && currentState.equals(that.currentState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, currentState);
    }
}
