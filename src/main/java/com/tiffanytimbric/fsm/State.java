package com.tiffanytimbric.fsm;


import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@JsonSerialize
@JsonTypeInfo( use = JsonTypeInfo.Id.CLASS )
public record State<T>(String name, T dataItem, Transition... transitions) implements Jsonable {

    public static State fromJson(final String stateJson) {
        return JsonUtil.fromJson(stateJson, State.class);
    }

    public List<Event> getEvents() {
        if (transitions == null) {
            return new ArrayList<>();
        }

        return Arrays.stream(transitions).map(Transition::event).collect(Collectors.toList());
    }

    public Optional<Transition> getTransitionFor(final String eventName) {
        return getTransitionFor(new Event(eventName, null));
    }

    public Optional<Transition> getTransitionFor(final Event event) {
        if (transitions == null) {
            return Optional.empty();
        }

        return Arrays.stream(transitions)
            .filter(transition -> transition.event().equals(event))
            .findFirst();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj.getClass() != getClass()) return false;
        State rhs = (State) obj;
        return new EqualsBuilder()
            .append(this.name, rhs.name)
            .append(this.dataItem, rhs.dataItem)
            .append(this.transitions, rhs.transitions)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(name)
            .append(dataItem)
            .append(transitions)
            .toHashCode();
    }
}
