package com.tiffanytimbric.fsm;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;


public interface Jsonable {

    <T> T fromJson(String fsmJson, Class<T> clazz);
    String toJson();

    default String toJson(Transition... transitions) {
        final StringBuilder buf = new StringBuilder("[");
        final List<String> transitionJsons = Arrays.stream(transitions).map(Transition::toJson).toList();
        IntStream.range(0, transitionJsons.size()).forEachOrdered(i -> {
            buf.append(transitionJsons.get(i));
            if (i < transitionJsons.size() - 1) {
                buf.append(", ");
            }
        });
        buf.append("]");

        return buf.toString();
    }

}
