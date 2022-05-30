package com.tiffanytimbric.fsm;

import javax.annotation.Nonnull;


public interface Jsonable {

    @Nonnull
    default String toJson() {
        return JsonUtil.toJson( this );
    }

}
