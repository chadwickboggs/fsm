package com.tiffanytimbric.fsm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static java.lang.String.format;


@JsonSerialize
@JsonTypeInfo( use = JsonTypeInfo.Id.CLASS )
public class FiniteStateMachine<T> implements Jsonable {

    private String name;
    private T dataItem;
    private boolean ignoreUnknownEvents;
    private State<T> initialState;
    @JsonIgnore
    private State<T> currentState;

    private FiniteStateMachine() {
    }

    public FiniteStateMachine(
        @Nonnull final String fsmName, @Nullable final T dataItem, @Nonnull final State<T> initialState
    ) {
        this(fsmName, dataItem, initialState, true);
    }

    public FiniteStateMachine(
        @Nonnull final String fsmName, @Nullable final T dataItem, @Nonnull final State<T> initialState,
        boolean ignoreUnknownEvents
    ) {
        this.name = fsmName;
        this.dataItem = dataItem;
        this.initialState = initialState;
        this.currentState = initialState;
        this.ignoreUnknownEvents = ignoreUnknownEvents;
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Nullable
    public T getDataItem() {
        return dataItem;
    }

    @Nonnull
    public State<T> getInitialState() {
        return initialState;
    }

    @Nonnull
    public State<T> getCurrentState() {
        return currentState;
    }

    public boolean isIgnoreUnknownEvents() {
        return ignoreUnknownEvents;
    }

    @Nonnull
    public List<Event> getEventsFor(@Nullable final String stateName) {
        return findState(stateName).map(State::getEvents).orElse(new ArrayList<>());
    }

    @Nonnull
    public Optional<State<T>> findState(@Nullable final String stateName) {
        if (initialState.name().equals(stateName)) {
            return Optional.of(initialState);
        }

        return findState(stateName, initialState.transitions());
    }

    @Nonnull
    public State<T> handleEvent(@Nonnull final String eventName) {
        return handleEvent(new Event(eventName, null));
    }

    @Nonnull
    public State<T> handleEvent(
        @Nonnull final String eventName,
        @Nullable final T dataArg
    ) {
        return handleEvent(new Event(eventName, dataArg));
    }

    @Nonnull
    public State<T> handleEvent(@Nonnull final Event<T> event) {
        if (currentState.transitions() == null) {
            if (!ignoreUnknownEvents) {
                throw newUnrecognizedEventIllegalArgumentException(event);
            }

            return currentState;
        }

        final Optional<Transition> transitionOpt = currentState.getTransitionFor(event);
        if (transitionOpt.isPresent()) {
            final Transition transition = transitionOpt.get();
            final BiConsumer handler = transition.handler();
            if (handler != null) {
                handler.accept(currentState, event);
            }

            return currentState = transition.toState() != null ? transition.toState() : findState(transition.toStateName()).get();
        }

        if (!ignoreUnknownEvents) {
            throw newUnrecognizedEventIllegalArgumentException(event);
        }

        return currentState;
    }

    @Nonnull
    public static FiniteStateMachine fromJson(@Nonnull final String fsmJson) {
        final FiniteStateMachine fsm = JsonUtil.fromJson(fsmJson, FiniteStateMachine.class);
        fsm.currentState = fsm.initialState;

        return fsm;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj.getClass() != getClass()) return false;
        FiniteStateMachine rhs = (FiniteStateMachine) obj;
        return new EqualsBuilder()
            .append(this.name, rhs.name)
            .append(this.dataItem, rhs.dataItem)
            .append(this.ignoreUnknownEvents, rhs.ignoreUnknownEvents)
            .append(this.currentState, rhs.currentState)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(name)
            .append(dataItem)
            .append(ignoreUnknownEvents)
            .append(currentState)
            .toHashCode();
    }

    @Nonnull
    private <T, J> Optional<State<J>> findState(
        @Nullable final String stateName, @Nonnull final Transition<T, J>[] transitions
    ) {
        if (transitions == null) {
            return Optional.empty();
        }

        return Arrays.stream(transitions)
            .map(Transition::toState)
            .filter(state -> state.name().equals(stateName))
            .findFirst();
    }

    @Nonnull
    private IllegalArgumentException newUnrecognizedEventIllegalArgumentException(@Nonnull final Event event) {
        return new IllegalArgumentException(format(
            "Unrecognized event.  Current state contains no handler for the specified event."
                + "  Current State: \"%s\", Specified Event: \"%s\"",
            currentState, event
        ));
    }
}
