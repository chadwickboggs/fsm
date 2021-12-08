package com.tiffanytimbric.fsm;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.function.Consumer;


@JsonSerialize
public record Transition(Event event, State toState, Consumer<Event> handler) implements Jsonable {

    public static Transition fromJson(final String transitionJson) {
        return JsonUtil.fromJson(transitionJson, Transition.class);
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
            .append(this.handler, rhs.handler)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(event)
            .append(toState)
            .append(handler)
            .toHashCode();
    }
}
