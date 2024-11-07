package models;

import Exceptions.InvalidSubpatternMappingException;
import Exceptions.PatternShapeInvalidException;

import java.util.*;

public class PatternService {

    private final PatternStore patternStore;
    private final PatternModel patternModel;
    private final Map<Vector2D,Character> relativeTargetMappings;

    public PatternService(PatternModel patternModel, PatternStore patternStore) throws PatternShapeInvalidException, InvalidSubpatternMappingException {
        this.patternStore = patternStore;
        this.patternModel = patternModel;
        checkSubpatternTree(patternModel);
        relativeTargetMappings = createMappingsFromPattern(patternModel);
    }

    private void checkSubpatternTree(PatternModel patternModel) throws PatternShapeInvalidException, InvalidSubpatternMappingException {
        String[] lines = patternModel.getPatternString().split("\n");

        if (isPatternShapeInvalid(lines)) {
            throw new PatternShapeInvalidException();
        }
        if (arePatternMappingsInvalid(patternModel)){
            throw new InvalidSubpatternMappingException();
        }
    }

    public Vector2D[] getAreaOfEffect(Vector2D player, Vector2D targetPosition) {
        Vector2D relativePosition = targetPosition.subtract(player);

        if (!relativeTargetMappings.containsKey(relativePosition)) {
            return new Vector2D[0];
        }

        char c = relativeTargetMappings.get(relativePosition);

        Set<Vector2D> targets = new HashSet<>();

        collectTargetsFromSubpattern(patternModel,targetPosition,targets);

        return targets.toArray(new Vector2D[0]);
    }

    private void collectTargetsFromSubpattern(PatternModel patternModel, Vector2D targetPosition, Set<Vector2D> targets) {
        Map<Vector2D,Character> mappings = createMappingsFromPattern(patternModel);
        for (Vector2D relativeSubpatternPosition : mappings.keySet()) {
            char c = mappings.get(relativeSubpatternPosition);
            Vector2D subpatternTarget = targetPosition.add(relativeSubpatternPosition);
            if (patternModel.getSubpatternMappings().containsKey(c)) {
                PatternModel subpatternModel = patternStore.getPatterns().get(patternModel.getSubpatternMappings().get(c));
                collectTargetsFromSubpattern(subpatternModel,subpatternTarget,targets);
            } else {
                targets.add(subpatternTarget);
            }
        }
    }

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

    public Vector2D[] getPossibleTargetPositions(Vector2D player){
        return Arrays.stream(relativeTargetMappings.keySet().toArray(new Vector2D[0])).map(player::add).toArray(Vector2D[]::new);
    }

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
                if (!patternStore.getPatterns().containsKey(subpatternName)){
                    return true;
                }
                return arePatternMappingsInvalid(patternStore.getPatterns().get(subpatternName));
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
        return o instanceof PatternService patternService
                && this.patternModel.equals(patternService.patternModel)
                && this.relativeTargetMappings.equals(patternService.relativeTargetMappings)
                && this.patternStore.equals(patternService.patternStore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patternModel,relativeTargetMappings,patternStore);
    }
}
