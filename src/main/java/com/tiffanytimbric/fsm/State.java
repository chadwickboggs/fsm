package com.tiffanytimbric.fsm;


import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.Objects;


@JsonAutoDetect
public record State(String name, Transition... transitions) implements Jsonable {

    public static State fromJson(final String stateJson) {
        return JsonUtil.fromJson(stateJson, State.class);
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
