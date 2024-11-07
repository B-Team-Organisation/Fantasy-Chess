package models;


import java.util.Map;

/**
 * Interface for pattern storage.
 * <p>
 * Different implementations for client and server.
 */
public interface PatternStore {
    Map<String, PatternModel> getPatterns();
}
