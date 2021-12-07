package com.tiffanytimbric.fsm;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import com.fasterxml.jackson.core.JsonProcessingException;


public interface Jsonable {

    // <T> T fromJson(String fsmJson, Class<T> clazz);
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

    default String toJson(Jsonable... jsonable) {
        final StringBuilder buf = new StringBuilder("[");
        final List<String> jsonables = Arrays.stream(jsonable).map(Jsonable::toJson).toList();
        IntStream.range(0, jsonables.size()).forEachOrdered(i -> {
            buf.append(jsonables.get(i));
            if (i < jsonables.size() - 1) {
                buf.append(", ");
            }
        });
        buf.append("]");

        return buf.toString();
    }

}
