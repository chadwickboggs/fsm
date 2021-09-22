package com.tiffanytimbric.fsm;


import java.util.Objects;
import java.util.function.Consumer;


public record Transition(Event event, State toState, Consumer<Event> handler) {

    @Override
    public String toString() {
        return "Transition{" +
            "event=" + event +
            ", toState=" + toState +
            '}';
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
