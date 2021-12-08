package com.tiffanytimbric.fsm;


import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


@JsonSerialize
@JsonTypeInfo( use = JsonTypeInfo.Id.CLASS )
public record State(String name, Transition... transitions) implements Jsonable {

    public static State fromJson(final String stateJson) {
        return JsonUtil.fromJson(stateJson, State.class);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj.getClass() != getClass()) return false;
        State rhs = (State) obj;
        return new EqualsBuilder()
            .append(this.name, rhs.name)
            .append(this.transitions, rhs.transitions)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(name)
            .append(transitions)
            .toHashCode();
    }
}
