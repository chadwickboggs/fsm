package com.tiffanytimbric.fsm;


import java.util.Arrays;
import java.util.Objects;


public record State(String name, Transition... transitions) {

    @Override
    public String toString() {
        return "State{" +
            "name='" + name + '\'' +
            ", transitions=" + Arrays.toString(transitions) +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return name.equals(state.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
