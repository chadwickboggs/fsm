package com.tiffanytimbric.fsm;

import com.fasterxml.jackson.databind.ObjectMapper;


public class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
