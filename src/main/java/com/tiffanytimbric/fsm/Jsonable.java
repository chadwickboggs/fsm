package com.tiffanytimbric.fsm;

public interface Jsonable {

    default String toJson() {
        return JsonUtil.toJson(this);
    }

}
