package com.tiffanytimbric.fsm;


import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.Objects;
import java.util.function.Consumer;

import static java.lang.String.format;


@JsonAutoDetect
public record Transition(Event event, State toState, Consumer<Event> handler) implements Jsonable {

    public Transition fromJson(final String fsmJson) {
        return fromJson(fsmJson, Transition.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transition that = (Transition) o;
        return event.equals(that.event) && toState.equals(that.toState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(event, toState);
    }
}
