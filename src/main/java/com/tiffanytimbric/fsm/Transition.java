package com.tiffanytimbric.fsm;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.function.BiConsumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;


@JsonSerialize
@JsonTypeInfo( use = JsonTypeInfo.Id.CLASS )
public record Transition<T, J>(
    @Nonnull Event<T> event, @Nonnull State<J> toState, @Nullable String toStateName,
    @JsonIgnore @Nullable BiConsumer<State<J>, Event<T>> handler
) implements Jsonable {

    public Transition(
        @Nonnull Event<T> event, @Nonnull State<J> toState, @Nullable BiConsumer<State<J>, Event<T>> handler
    ) {
        this( event, toState, null, handler );
    }

    public Transition(
        @Nonnull Event<T> event, @Nonnull String toStateName, @Nullable BiConsumer<State<J>, Event<T>> handler
    ) {
        this( event, null, toStateName, handler );
    }

    @Nonnull
    public static Transition fromJson( @Nonnull final String transitionJson ) {
        return JsonUtil.fromJson( transitionJson, Transition.class );
    }

    @Override
    public boolean equals( Object obj ) {
        if ( obj == null ) return false;
        if ( obj == this ) return true;
        if ( obj.getClass() != getClass() ) return false;
        Transition rhs = (Transition) obj;
        return new EqualsBuilder()
            .append( this.event, rhs.event )
            .append( this.toState, rhs.toState )
            .append( this.toStateName, rhs.toStateName )
            .append( this.handler, rhs.handler )
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append( event )
            .append( toState )
            .append( toStateName )
            .append( handler )
            .toHashCode();
    }
}
