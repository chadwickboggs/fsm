package com.tiffanytimbric.fsm;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import com.fasterxml.jackson.core.JsonProcessingException;


public interface Jsonable {

    default <T> T fromJson(final String stateJson, final Class<T> clazz) {
        try {
            return JsonUtil.getObjectMapper().readValue(stateJson, clazz);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    default String toJson() {
        try {
            return JsonUtil.getObjectMapper().writeValueAsString(this);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
