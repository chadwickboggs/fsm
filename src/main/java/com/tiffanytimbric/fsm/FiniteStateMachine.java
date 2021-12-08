package com.tiffanytimbric.fsm;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Arrays;

import static java.lang.String.format;


@JsonSerialize
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

    public static FiniteStateMachine fromJson(final String fsmJson) {
        return JsonUtil.fromJson(fsmJson, FiniteStateMachine.class);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj.getClass() != getClass()) return false;
        FiniteStateMachine rhs = (FiniteStateMachine) obj;
        return new EqualsBuilder()
            .append(this.name, rhs.name)
            .append(this.ignoreUnknownEvents, rhs.ignoreUnknownEvents)
            .append(this.currentState, rhs.currentState)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(name)
            .append(ignoreUnknownEvents)
            .append(currentState)
            .toHashCode();
    }
}
