package models;


/**
 * Interface for pattern storage.
 * <p>
 * Different implementations for client and server.
 */
public interface PatternStore {

    /**
     * Returns the {@link PatternModel} of the requested Pattern.
     *
     * @param patternName the name of the pattern
     * @return the {@link PatternModel}
     */
    PatternModel getPatternByName(String patternName);
}
