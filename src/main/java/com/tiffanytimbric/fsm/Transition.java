package com.tiffanytimbric.fsm;


import java.util.Objects;
import java.util.function.Consumer;

import static java.lang.String.format;


public record Transition(Event event, State toState, Consumer<Event> handler) implements Jsonable {

    public Transition fromJson(final String fsmJson) {
        return fromJson(fsmJson, Transition.class);
    }

    @Override
    public <T> T fromJson(final String fsmJson, final Class<T> clazz) {
        // TODO: Implement.
        throw new RuntimeException("This method has not been implemented.");
    }

    @Override
    public String toJson() {
        return format("{event=%s, toState=%s}", event.toJson(), toState.toJson());
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
