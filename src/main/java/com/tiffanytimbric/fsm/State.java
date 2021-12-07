package com.tiffanytimbric.fsm;


import java.util.Objects;

import static java.lang.String.format;


public record State(String name, Transition... transitions) implements Jsonable {

    public State fromJson(final String fsmJson) {
        return fromJson(fsmJson, State.class);
    }

    @Override
    public <T> T fromJson(final String fsmJson, final Class<T> clazz) {
        // TODO: Implement.
        throw new RuntimeException("This method has not been implemented.");
    }

    @Override
    public String toJson() {
        return format("{name='%s', transitions=%s}", name, toJson(transitions));
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
