package com.tiffanytimbric.fsm;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.function.BiConsumer;


@JsonSerialize
@JsonTypeInfo( use = JsonTypeInfo.Id.CLASS )
public record Transition<T, J>(
    Event<T> event, State<J> toState, String toStateName, @JsonIgnore BiConsumer<State<J>, Event<T>> handler) implements Jsonable {

    public static Transition fromJson(final String transitionJson) {
        return JsonUtil.fromJson(transitionJson, Transition.class);
    }

    public Transition(Event<T> event, State<J> toState, BiConsumer<State<J>, Event<T>> handler) {
        this(event, toState, null, handler);
    }

    public Transition(Event<T> event, String toStateName, BiConsumer<State<J>, Event<T>> handler) {
        this(event, null, toStateName, handler);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj.getClass() != getClass()) return false;
        Transition rhs = (Transition) obj;
        return new EqualsBuilder()
            .append(this.event, rhs.event)
            .append(this.toState, rhs.toState)
            .append(this.toStateName, rhs.toStateName)
            .append(this.handler, rhs.handler)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(event)
            .append(toState)
            .append(toStateName)
            .append(handler)
            .toHashCode();
    }
}
