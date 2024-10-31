package models;

public class PatternService {

    private final char[][] pattern;
    private final Vector2D playerIndexes;

    public PatternService(PatternModel patternModel){
        String[] lines = patternModel.getPatternString().split("\n");

        char[][] pattern = new char[lines.length][lines.length];

        for (int i = 0; i < lines.length; i++){
            pattern[i] = lines[i].toCharArray();
        }

        this.pattern = pattern;
    }

    public Vector2D[] getPossibleTiles(Vector2D player){

    }

    public Vector2D[] getAreaOfAttack(Vector2D player, Vector2D target){
        Vector2D targetRelativToPlayer = target.subtract(player);

    }

}
