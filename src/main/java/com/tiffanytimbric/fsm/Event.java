package com.tiffanytimbric.fsm;

import java.util.Objects;

import static java.lang.String.format;


public record Event(String name) implements Jsonable {

    public Event fromJson(final String fsmJson) {
        return fromJson(fsmJson, Event.class);
    }

    @Override
    public <T> T fromJson(final String fsmJson, final Class<T> clazz) {
        // TODO: Implement.
        throw new RuntimeException("This method has not been implemented.");
    }

    @Override
    public String toJson() {
        return format("{name=%s}", name);
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
