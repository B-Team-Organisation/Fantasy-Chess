package com.bteam.common.utils;

import com.bteam.common.dto.JsonDTO;

import java.util.List;
import java.util.Map;

/**
 * A utility class for constructing JSON strings in a structured manner.
 *
 * @author Marc
 */
public class JsonWriter {
    StringBuilder builder;

    /**
     * Initializes a new instance of {@code JsonWriter} with an opening curly brace.
     */
    public JsonWriter() {
        builder = new StringBuilder();
        builder.append("{");
    }

    /**
     * Writes a JSON array with the given key and list of objects.
     *
     * @param key     The key for the JSON array.
     * @param objects The list of objects to be included in the array.
     * @param <T>     The type of objects in the list.
     * @return The current {@code JsonWriter} instance.
     */
    public <T> JsonWriter writeList(String key, List<T> objects) {
        builder.append("\"").append(key).append("\":[");
        objects.forEach(this::writeValueAnd);
        if (!objects.isEmpty()) {
            builder.deleteCharAt(builder.length() - 1);
        }
        builder.append("]");
        return this;
    }

    /**
     * Writes a key-value pair to the JSON output.
     *
     * @param key    The key for the JSON entry.
     * @param object The value associated with the key.
     * @param <T>    The type of the value.
     * @return The current {@code JsonWriter} instance.
     */
    public <T> JsonWriter writeKeyValue(String key, T object) {
        writeKey(key);
        writeValue(object);
        return this;
    }

    /**
     * Writes a value to the JSON output.
     * If the object is an instance of {@code JsonDTO}, its {@code toJson} method is used.
     * If the object is a {@code String}, it is enclosed in double quotes.
     * Otherwise, the object is appended directly.
     *
     * @param object The value to write.
     * @param <T>    The type of the value.
     * @return The current {@code JsonWriter} instance.
     */
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

    /**
     * Writes a JSON key.
     *
     * @param key The key to write.
     * @return The current {@code JsonWriter} instance.
     */
    private JsonWriter writeKey(String key) {
        builder.append("\"").append(key).append("\":");
        return this;
    }

    /**
     * Writes a value followed by a comma (for separating JSON entries).
     *
     * @param object The value to write.
     * @param <T>    The type of the value.
     * @return The current {@code JsonWriter} instance.
     */
    private <T> JsonWriter writeValueAnd(T object) {
        writeValue(object);
        and();
        return this;
    }

    /**
     * Writes a JSON object from a map, where each entry represents a key-value pair.
     *
     * @param key The key for the JSON object.
     * @param map The map containing key-value pairs.
     * @param <T> The type of keys in the map.
     * @param <U> The type of values in the map.
     * @return The current {@code JsonWriter} instance.
     */
    public <T, U> JsonWriter writeMap(String key, Map<T, U> map) {
        writeKey(key);
        builder.append("{");
        map.entrySet().stream()
                .forEach(entry -> writeKeyValue(entry.getKey().toString(), entry.getValue()).and());
        if (!map.isEmpty()) builder.deleteCharAt(builder.length() - 1);
        builder.append("}");
        return this;
    }

    /**
     * Appends a comma to separate JSON entries.
     *
     * @return The current {@code JsonWriter} instance.
     */
    public JsonWriter and() {
        builder.append(",");
        return this;
    }

    /**
     * Returns the generated JSON string, ensuring it is properly closed with a closing curly brace.
     *
     * @return The constructed JSON string.
     */
    @Override
    public String toString() {
        return builder.toString() + "}";
    }
}
