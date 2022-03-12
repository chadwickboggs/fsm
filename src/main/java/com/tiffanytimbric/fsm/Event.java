package com.tiffanytimbric.fsm;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


@JsonSerialize
@JsonTypeInfo( use = JsonTypeInfo.Id.CLASS )
public record Event<T>(@Nonnull String name, @Nullable T dataArg) implements Jsonable {

    @Nonnull
    public static Event fromJson(@Nonnull final String eventJson) {
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
