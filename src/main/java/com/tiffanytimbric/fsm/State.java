package com.tiffanytimbric.fsm;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Objects;

import static java.lang.String.format;


@JsonAutoDetect
public record State(String name, Transition... transitions) implements Jsonable {

    public State fromJson(final String fsmJson) {
        return fromJson(fsmJson, State.class);
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
