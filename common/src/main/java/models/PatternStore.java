package models;


/**
 * Interface for pattern storage.
 * <p>
 * Different implementations for client and server.
 *
 * @author Lukas
 * @version 1.0
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
