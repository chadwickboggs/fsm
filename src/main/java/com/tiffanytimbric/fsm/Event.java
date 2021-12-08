package com.tiffanytimbric.fsm;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


@JsonSerialize
public record Event(String name) implements Jsonable {

    public static Event fromJson(final String eventJson) {
        return JsonUtil.fromJson(eventJson, Event.class);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj.getClass() != getClass()) return false;
        Event rhs = (Event) obj;
        return new EqualsBuilder()
            .append(this.name, rhs.name)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(name)
            .toHashCode();
    }

}
