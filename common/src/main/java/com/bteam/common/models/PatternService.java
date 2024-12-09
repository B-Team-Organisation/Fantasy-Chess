package com.bteam.common.models;

import com.bteam.common.exceptions.InvalidSubpatternMappingException;
import com.bteam.common.exceptions.PatternShapeInvalidException;

import java.util.*;

/**
 * Service class for patterns
 * <p>
 * Validates the pattern upon creation.
 * Makes it possible to get possible targets and tiles affected by actions of said targets.
 *
 * @author Lukas
 * @version 1.0
 */
public class PatternService {

    private final PatternStore patternStore;
    private final PatternModel patternModel;
    private final Map<Vector2D,Character> relativeTargetMappings;

    /**
     * Constructor for PatternService
     *
     * @param patternModel the services {@link PatternModel}
     * @param patternStore the {@link PatternStore} containing all patterns
     * @throws PatternShapeInvalidException if the shape of the pattern is invalid
     * @throws InvalidSubpatternMappingException if the patterns cant be traversed correctly
     */
    public PatternService(PatternModel patternModel, PatternStore patternStore) throws PatternShapeInvalidException, InvalidSubpatternMappingException {
        this.patternStore = patternStore;
        this.patternModel = patternModel;
        validatePattern(patternModel);
        relativeTargetMappings = createMappingsFromPattern(patternModel);
    }

    /**
     * Validated the given {@link PatternModel} in terms of shape and completeness
     * <p>
     * Extracts the lines of the patternString and validates them.
     * The working horse of this method is the method {@link PatternService#arePatternMappingsInvalid(PatternModel).
     *
     * @param patternModel the {@link PatternModel} to validate
     * @throws PatternShapeInvalidException if the shape of the pattern is invalid
     * @throws InvalidSubpatternMappingException if the patterns cant be traversed correctly
     */
    private void validatePattern(PatternModel patternModel) throws PatternShapeInvalidException, InvalidSubpatternMappingException {
        String[] lines = patternModel.getPatternString().split("\n");

        if (isPatternShapeInvalid(lines)) {
            throw new PatternShapeInvalidException();
        }
        if (arePatternMappingsInvalid(patternModel)){
            throw new InvalidSubpatternMappingException();
        }
    }

    /**
     * Returns the area of effect an action would have.
     * <p>
     * Can be used to get the positions an attack on a certain tile in the pattern would affect.
     *
     * @param player the {@link Vector2D}-position of the player
     * @param targetPosition the {@link Vector2D}-position of the attack
     * @return a {@link Vector2D}[] containing all affected positions
     */
    public Vector2D[] getAreaOfEffect(Vector2D player, Vector2D targetPosition) {
        Vector2D relativePosition = targetPosition.subtract(player);

        if (!relativeTargetMappings.containsKey(relativePosition)) {
            return new Vector2D[0];
        }

        char c = relativeTargetMappings.get(relativePosition);

        Set<Vector2D> targets = new HashSet<>();

        if (patternModel.getSubpatternMappings().containsKey(c)) {
            PatternModel subpatternModel = patternStore.getPatternByName(patternModel.getSubpatternMappings().get(c));
            collectTargetsFromSubpattern(subpatternModel,targetPosition,targets);
        } else {
            targets.add(targetPosition);
        }

        return targets.toArray(new Vector2D[0]);
    }

    /**
     * Adds all attacked positions from the {@link PatternModel} to the {@code targets}.
     * <p>
     * Iterated the mapped positions of the subpattern and either adds them to the targets or recursively executed this
     * method on the mapped subpattern.
     *
     * @param patternModel the {@link PatternModel} it gets the mappings from
     * @param targetPosition the {@link Vector2D} it needs to calculate the absolute positions on the grid
     * @param targets the {@link Set} it adds the determined positions to
     */
    private void collectTargetsFromSubpattern(PatternModel patternModel, Vector2D targetPosition, Set<Vector2D> targets) {
        Map<Vector2D,Character> mappings = createMappingsFromPattern(patternModel);
        for (Vector2D relativeSubpatternPosition : mappings.keySet()) {
            char c = mappings.get(relativeSubpatternPosition);
            Vector2D subpatternTarget = targetPosition.add(relativeSubpatternPosition);
            if (patternModel.getSubpatternMappings().containsKey(c)) {
                PatternModel subpatternModel = patternStore.getPatternByName(patternModel.getSubpatternMappings().get(c));
                collectTargetsFromSubpattern(subpatternModel,subpatternTarget,targets);
            } else {
                targets.add(subpatternTarget);
            }
        }
    }

    /**
     * Parses the {@link PatternModel} string and created a Map of all the non-empty positions mapped to their mapping.
     *
     * @param patternModel the {@link PatternModel} the mappings are created for
     * @return the {@link Map} containing the mappings
     */
    private Map<Vector2D,Character> createMappingsFromPattern(PatternModel patternModel) {
        String[] lines = patternModel.getPatternString().split("\n");

        Map<Vector2D,Character> mappings = new HashMap<>();
        for (int y = 0; y < lines.length; y++) {
            String line = lines[y];
            for (int x = 0; x < lines.length; x++) {
                char tileChar = line.charAt(x);
                if (tileChar != ' '){
                    mappings.put(new Vector2D(x-lines.length/2, y-lines.length/2), tileChar);
                }
            }
        }
        return mappings;
    }

    /**
     * Returns all positions the player can target
     *
     * @param player the {@link Vector2D}-position of the player
     *
     * @return a {@link Vector2D}[] containing the {@link Vector2D}-positions of all the possible positions the player can target with this pattern
     */
    public Vector2D[] getPossibleTargetPositions(Vector2D player){
        return Arrays.stream(relativeTargetMappings.keySet().toArray(new Vector2D[0])).map(player::add).toArray(Vector2D[]::new);
    }

    /**
     * Checks if the pattern shape is valid
     * <p>
     * Checks if the shape is of equal and odd width and height.
     *
     * @param lines the lines of the pattern String
     * @return {@code true} if the shape is invalid, else {@code false}
     */
    private boolean isPatternShapeInvalid(String[] lines){
        if (lines.length % 2 == 0){
            return true;
        }
        int length = lines.length;
        for (String line : lines) {
            if (length != line.length()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the pattern mapping tree is invalid
     * <p>
     * Iterates over the pattern and makes sure that all non-empty chars in the string are either without a mapping or
     * mapped to an existing and valid subpattern.
     *
     * @param patternModel the {@link PatternModel} that is being investigated
     * @return {@code true} if there is an error in the subpattern tree structure, else {@code false}
     */
    private boolean arePatternMappingsInvalid(PatternModel patternModel) {
        String[] lines = patternModel.getPatternString().split("\n");
        for (String line:lines){
            for (char c : line.toCharArray()){
                if (c == ' '){
                    continue;
                }
                if (!patternModel.getSubpatternMappings().containsKey(c)){
                    continue;
                }
                String subpatternName = patternModel.getSubpatternMappings().get(c);

                PatternModel subpatternModel = patternStore.getPatternByName(subpatternName);
                if (subpatternModel == null){
                    return true;
                }
                return arePatternMappingsInvalid(subpatternModel);
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "PatternService [patternModel=" + patternModel + ", relativeTargetMappings=" + relativeTargetMappings + ", patternStore=" + patternStore + "]";
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof PatternService
                && this.patternModel.equals(((PatternService) o).patternModel)
                && this.relativeTargetMappings.equals(((PatternService) o).relativeTargetMappings)
                && this.patternStore.equals(((PatternService) o).patternStore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patternModel,relativeTargetMappings,patternStore);
    }
}
