package com.tiffanytimbric.fsm;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.Objects;

import static java.lang.String.format;


@JsonAutoDetect
public record Event(String name) implements Jsonable {

    public Event fromJson(final String fsmJson) {
        return fromJson(fsmJson, Event.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return name.equals(event.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
