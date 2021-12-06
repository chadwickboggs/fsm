package com.tiffanytimbric.fsm;

import java.util.Arrays;
import java.util.Objects;

import static java.lang.String.format;


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
            throw newUnrecognizedEventIllegalArgumentException(event);
        }

        final Transition[] transition = new Transition[1];
        Arrays.stream(currentState.transitions())
            .filter(aTransition -> aTransition.event().equals(event))
            .findFirst()
            .ifPresentOrElse(aTransition -> {
                aTransition.handler().accept(event);
                transition[0] = aTransition;
            }, () -> {
                throw newUnrecognizedEventIllegalArgumentException(event);
            });

        return currentState = transition[0].toState();
    }

    private IllegalArgumentException newUnrecognizedEventIllegalArgumentException(final Event event) {
        return new IllegalArgumentException(format(
            "Unrecognized event.  Current state contains no handler for the specified event."
                + "  Current State: \"%s\", Specified Event: \"%s\"",
            currentState, event
        ));
    }

    @Override
    public String toString() {
        return format("{name='%s', currentState=%s}", name, currentState);
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
