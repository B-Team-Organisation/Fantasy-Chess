package com.bteam.fantasychess_client.services;

import com.bteam.common.models.PatternModel;
import com.bteam.common.models.PatternStore;

import java.util.HashMap;
import java.util.List;

/**
 *  Stores the patterns send to the client by the server.
 *  <p>
 *  Every match, the server sends all necessary patterns to the client.
 *  The patterns can be retrieved by their name.
 *
 * @author lukas
 */
public class ClientPatternStore implements PatternStore {

    private final HashMap<String, PatternModel> patternStore;

    /**
     * Basic constructor for {@link ClientPatternStore}
     */
    public ClientPatternStore() {
        this.patternStore = new HashMap<>();
    }

    /**
     * Adds the given {@link PatternModel}s to the store
     * <p>
     * Makes the {@link PatternModel}s retrievable by their names.
     *
     * @param patterns a {@link List} of {@link PatternModel}s to add
     */
    public void addPatterns(List<PatternModel> patterns){
        for (PatternModel pattern : patterns) {
            addPattern(pattern);
        }
    }

    /**
     * Adds the given {@link PatternModel} to the store
     * <p>
     * Makes the {@link PatternModel} retrievable by its name.
     *
     * @param pattern the {@link PatternModel} to add
     */
    public void addPattern(PatternModel pattern) {
        patternStore.put(pattern.getPatternName(),pattern);
    }

    /**
     * Returns the pattern specified by the name
     *
     * @param patternName the name of the pattern
     * @return the {@link PatternModel} of the pattern
     */
    @Override
    public PatternModel getPatternByName(String patternName) {
        return patternStore.get(patternName);
    }
}
