package com.tiffanytimbric.fsm;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;


@JsonSerialize
@JsonTypeInfo( use = JsonTypeInfo.Id.CLASS )
public record State<T>(@Nonnull String name, @Nullable T dataItem, @Nullable Transition... transitions) implements Jsonable {

    @Nonnull
    public static State fromJson(@Nonnull final String stateJson) {
        return JsonUtil.fromJson(stateJson, State.class);
    }

    @JsonIgnore
    @Nullable
    public List<Event> getEvents() {
        if (transitions == null) {
            return new ArrayList<>();
        }

        return Arrays.stream(transitions).map(Transition::event).collect(Collectors.toList());
    }

    @Nonnull
    public Optional<Transition> getTransitionFor(@Nonnull final String eventName) {
        return getTransitionFor(new Event(eventName, null));
    }

    @Nonnull
    public Optional<Transition> getTransitionFor(@Nonnull final Event event) {
        if (transitions == null) {
            return Optional.empty();
        }

        return Arrays.stream(transitions)
            .filter(transition -> event.equals(transition.event()))
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
