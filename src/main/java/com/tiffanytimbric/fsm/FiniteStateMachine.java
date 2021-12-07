package com.tiffanytimbric.fsm;

import java.util.Arrays;
import java.util.Objects;

import static java.lang.String.format;


public class FiniteStateMachine implements Jsonable {

    private final String name;
    private final boolean ignoreUnknownEvents;
    private State currentState;

    public FiniteStateMachine(final String fsmName, final State initialState) {
        this(fsmName, initialState, true);
    }

    public FiniteStateMachine(final String fsmName, final State initialState, boolean ignoreUnknownEvents) {
        this.name = fsmName;
        this.currentState = initialState;
        this.ignoreUnknownEvents = ignoreUnknownEvents;
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
            if (!ignoreUnknownEvents) {
                throw newUnrecognizedEventIllegalArgumentException(event);
            }

            return currentState;
        }

        final Transition[] transition = new Transition[1];
        Arrays.stream(currentState.transitions())
            .filter(aTransition -> aTransition.event().equals(event))
            .findFirst()
            .ifPresentOrElse(aTransition -> {
                aTransition.handler().accept(event);
                transition[0] = aTransition;
            }, () -> {
                if (!ignoreUnknownEvents) {
                    throw newUnrecognizedEventIllegalArgumentException(event);
                }
            });
        if (transition[0] == null) {
            if (!ignoreUnknownEvents) {
                throw newUnrecognizedEventIllegalArgumentException(event);
            }

            return currentState;
        }

        return currentState = transition[0].toState();
    }

    private IllegalArgumentException newUnrecognizedEventIllegalArgumentException(final Event event) {
        return new IllegalArgumentException(format(
            "Unrecognized event.  Current state contains no handler for the specified event."
                + "  Current State: \"%s\", Specified Event: \"%s\"",
            currentState, event
        ));
    }

    public FiniteStateMachine fromJson(final String fsmJson) {
        return fromJson(fsmJson, FiniteStateMachine.class);
    }

    @Override
    public <T> T fromJson(final String fsmJson, final Class<T> clazz) {
        // TODO: Implement.
        throw new RuntimeException("This method has not been implemented.");
    }

    @Override
    public String toJson() {
        return format("{name='%s', ignoreUnknownEvents=%b, currentState=%s}", name, ignoreUnknownEvents, currentState.toJson());
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
