package com.bteam.common.utils;

import com.bteam.common.dto.JsonDTO;

import java.util.List;
import java.util.Map;

public class JsonWriter {
    StringBuilder builder;

    public JsonWriter() {
        builder = new StringBuilder();
        builder.append("{");
    }

    public <T> JsonWriter writeList(String key, List<T> objects) {
        builder.append("\"").append(key).append("\":[");
        objects.forEach(this::writeValueAnd);
        if (!objects.isEmpty()) {
            builder.deleteCharAt(builder.length() - 1);
        }
        builder.append("]");
        return this;
    }

    public <T> JsonWriter writeKeyValue(String key, T object) {
        writeKey(key);
        writeValue(object);
        return this;
    }

    private <T> JsonWriter writeValue(T object) {
        if (object instanceof JsonDTO) {
            JsonDTO jsonDTO = (JsonDTO) object;
            builder.append(jsonDTO.toJson());
        } else if (object instanceof String) {
            builder.append("\"").append(object).append("\"");
        } else {
            builder.append(object);
        }
        return this;
    }

    private JsonWriter writeKey(String key) {
        builder.append("\"").append(key).append("\":");
        return this;
    }

    private <T> JsonWriter writeValueAnd(T object) {
        writeValue(object);
        and();
        return this;
    }

    public <T, U> JsonWriter writeMap(String key, Map<T, U> map) {
        writeKey(key);
        builder.append("{");
        map.entrySet().stream()
            .forEach(entry -> writeKeyValue(entry.getKey().toString(), entry.getValue()).and());
        if (!map.isEmpty()) builder.deleteCharAt(builder.length() - 1);
        builder.append("}");
        return this;
    }

    public JsonWriter and() {
        builder.append(",");
        return this;
    }

    @Override
    public String toString() {
        return builder.toString() + "}";
    }
}
