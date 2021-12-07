package com.tiffanytimbric.fsm;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.Objects;


@JsonAutoDetect
public record Event(String name) implements Jsonable {

    public static Event fromJson(final String eventJson) {
        return JsonUtil.fromJson(eventJson, Event.class);
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
