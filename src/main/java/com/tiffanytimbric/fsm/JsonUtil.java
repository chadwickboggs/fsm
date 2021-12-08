package com.tiffanytimbric.fsm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JsonUtil {

    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static <T> T fromJson(final String stateJson, final Class<T> clazz) {
        try {
            return getObjectMapper().readValue(stateJson, clazz);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static String toJson(final Jsonable jsonable) {
        try {
            return getObjectMapper().writeValueAsString(jsonable);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
