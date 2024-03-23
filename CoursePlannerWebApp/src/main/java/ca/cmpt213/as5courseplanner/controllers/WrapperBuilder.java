package ca.cmpt213.as5courseplanner.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WrapperBuilder {
    public static <S, T> T fromJson(S source, Class<T> type) {
        try {
            Class<WrapperBuilder> x = WrapperBuilder.class;

            ObjectMapper objectMapper = new ObjectMapper();

            T copy = objectMapper
                    .readValue(objectMapper.writeValueAsString(source), type);

            return copy;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("ERROR: Unable to convert object to type " + type + " from " + source, e);
        }
    }

    public static <S, T> Iterable<T> listFromJson(Iterable<S> sourceList, Class<T> type) {
        List<T> list = new ArrayList<>();
        for (S source : sourceList) {
            list.add(fromJson(source, type));
        }
        return list;
    }
}
