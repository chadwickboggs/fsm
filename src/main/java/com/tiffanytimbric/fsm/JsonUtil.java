package com.tiffanytimbric.fsm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.Nonnull;


public class JsonUtil {

    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
    }

    @Nonnull
    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    @Nonnull
    public static <T> T fromJson(
        @Nonnull final String stateJson, @Nonnull final Class<T> clazz
    ) {
        try {
            return getObjectMapper().readValue( stateJson, clazz );
        }
        catch ( JsonProcessingException e ) {
            throw new RuntimeException( e.getMessage(), e );
        }
    }

    @Nonnull
    public static String toJson( @Nonnull final Jsonable jsonable ) {
        try {
            return getObjectMapper().writeValueAsString( jsonable );
        }
        catch ( JsonProcessingException e ) {
            throw new RuntimeException( e.getMessage(), e );
        }
    }
}
