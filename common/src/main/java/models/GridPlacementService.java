package models;

import Exceptions.*;
import entities.CharacterEntity;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GridPlacementService {

    /**
     * Places characters in the allowed starting rows at the bottom of the grid.
     *
     * @param gridService GridService that manages the grid
     * @param characters  list of CharacterEntity objects to place in the grid
     * @param startTilesRows array with the exact row numbers allowed for placement
     *
     */
    public void placeCharacters(GridService gridService, List<CharacterEntity> characters, int[] startTilesRows)
            throws DestinationInvalidException, DestinationAlreadyOccupiedException, NotAStartPositionException, WrongStartTilesException, FullStartTilesException, NoCharacterGivenException {

        if (gridService == null) {
            throw new NullPointerException("GridService cannot be null.");
        }

        for (CharacterEntity character : characters) {
            if (character == null) {
                throw new NoCharacterGivenException();
            }
        }

        if (startTilesRows == null || startTilesRows.length == 0) {
            throw new WrongStartTilesException(startTilesRows);
        }

        List<Integer> sortedRows = Arrays.stream(startTilesRows)
                .boxed()
                .sorted((a, b) -> Integer.compare(b, a))
                .toList();
        int gridCols = gridService.getGridModel().getCols();



        int availableSpaces = gridCols *sortedRows.size();
        System.out.println("ch"+characters.size()+" as"+availableSpaces);
        if (characters.size() > availableSpaces) {
            throw new FullStartTilesException();
        }

       int characterIndex = 0;
        for (int row : sortedRows) {
            for (int col = 0; col < gridCols && characterIndex < characters.size(); col++) {
                Vector2D position = new Vector2D(col, row);


                if (gridService.getTileAt(position).getStartTile()) {
                    if (gridService.getTileAt(position).getCharacter() != null) {
                        throw new DestinationAlreadyOccupiedException(position);
                    }

                    CharacterEntity character = characters.get(characterIndex);
                    gridService.setCharacterTo(position, character);
                    character.setPosition(position);
                    System.out.println("Character placed at: " + position);

                    characterIndex++;
                }
            }
        }
    }
}

