package com.tiffanytimbric.fsm;

import java.util.Objects;

import static java.lang.String.format;


public record Event(String name) {

    @Override
    public String toString() {
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
