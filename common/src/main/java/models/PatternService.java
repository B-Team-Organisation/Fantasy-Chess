package models;

import Exceptions.PatternShapeInvalidException;

import java.util.ArrayList;

public class PatternService {


    protected final char[][] pattern;

    public PatternService(PatternModel patternModel) throws PatternShapeInvalidException {
        String[] lines = patternModel.getPatternString().split("\n");

        if (isPatternShapeInvalid(lines)) {
            throw new PatternShapeInvalidException();
        }

        for (int y = 0; y < lines.length; y++) {
            String line = lines[y];
            for (int x = 0; x < lines.length; x++) {
                char patternTile = line.charAt(x);
                if (patternTile == ' '){
                    continue;
                }

            }
        }

        char[][] pattern = new char[lines.length][lines[0].length()];

        this.pattern = pattern;
    }

    private boolean isPatternShapeInvalid(String[] lines){
        return lines.length % 2 == 0 || lines[0].length() % 2 == 0;
    }

    public Vector2D[] getPossibleTargetPositions(Vector2D player){
        ArrayList<Vector2D> possibleTiles = new ArrayList<>();

        return possibleTiles.toArray(new Vector2D[0]);
    }

    public char[][] getPattern(){
        return pattern;
    }

}
