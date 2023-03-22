package com.johnburitto.tdddev.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.util.ArrayList;
import java.util.List;

public class JsonUtil {
    private static final ObjectMapper mapper = JsonMapper.builder()
            .findAndAddModules()
            .build();
    public static String toJson(Object object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }

    public static <T> T fromJson(String content, Class<T> valueType) throws JsonProcessingException {
        return mapper.readValue(content, valueType);
    }

    public static <T> List<T> listFromJson(String content, Class<T> valueType) throws JsonProcessingException {
        List<T> dataList = new ArrayList<>();
        var stringList = mapper.readValue(content, List.class);

        for (var obj : stringList) {
            dataList.add(mapper.readValue(mapper.writeValueAsString(obj), valueType));
        }

        return dataList;
    }
}
