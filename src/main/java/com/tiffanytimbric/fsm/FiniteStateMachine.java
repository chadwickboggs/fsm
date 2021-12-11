package com.tiffanytimbric.fsm;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;


@JsonSerialize
@JsonTypeInfo( use = JsonTypeInfo.Id.CLASS )
public class FiniteStateMachine implements Jsonable {

    private String name;
    private boolean ignoreUnknownEvents;
    private State initialState;
    private State currentState;

    private FiniteStateMachine() {
    }

    public FiniteStateMachine(final String fsmName, final State initialState) {
        this(fsmName, initialState, true);
    }

    public FiniteStateMachine(final String fsmName, final State initialState, boolean ignoreUnknownEvents) {
        this.name = fsmName;
        this.initialState = initialState;
        this.currentState = initialState;
        this.ignoreUnknownEvents = ignoreUnknownEvents;
    }

    public String getName() {
        return name;
    }

    public State getInitialState() {
        return initialState;
    }

    public State getCurrentState() {
        return currentState;
    }

    public boolean isIgnoreUnknownEvents() {
        return ignoreUnknownEvents;
    }

    public List<Event> getEventsFor(final String stateName) {
        return findState(stateName).map(State::getEvents).orElse(new ArrayList<>());
    }

    public Optional<State> findState(final String stateName) {
        if (initialState.name().equals(stateName)) {
            return Optional.of(initialState);
        }

        return findState(stateName, initialState.transitions());
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

        final Optional<Transition> transitionOpt = currentState.getTransitionFor(event);
        if (transitionOpt.isPresent()) {
            final Transition transition = transitionOpt.get();
            transition.handler().accept(event);

            return currentState = transition.toState();
        }

        if (!ignoreUnknownEvents) {
            throw newUnrecognizedEventIllegalArgumentException(event);
        }

        return currentState;
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

    private Optional<State> findState(final String stateName, final Transition[] transitions) {
        if (transitions == null) {
            return Optional.empty();
        }

        return Arrays.stream(transitions)
            .map(Transition::toState)
            .filter(state -> state.name().equals(stateName))
            .findFirst();
    }

    private IllegalArgumentException newUnrecognizedEventIllegalArgumentException(final Event event) {
        return new IllegalArgumentException(format(
            "Unrecognized event.  Current state contains no handler for the specified event."
                + "  Current State: \"%s\", Specified Event: \"%s\"",
            currentState, event
        ));
    }
}
