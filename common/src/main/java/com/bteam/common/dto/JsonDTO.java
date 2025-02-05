package com.bteam.common.dto;

/**
 * Identifies a class as Serializable to JSON, however, the
 * serialization needs to be implemented inside the class itself
 *
 * @author Marc
 */
public interface JsonDTO {
    /**
     * Implement this method as JSON serialization and return the class as a JSON string
     * @return Class serialized as JSON in a String
     */
    String toJson();
}
