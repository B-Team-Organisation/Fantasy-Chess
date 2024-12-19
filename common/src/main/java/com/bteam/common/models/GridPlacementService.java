package com.bteam.common.models;

import com.bteam.common.entities.CharacterEntity;
import com.bteam.common.exceptions.DestinationAlreadyOccupiedException;
import com.bteam.common.exceptions.DestinationInvalidException;
import com.bteam.common.exceptions.FullStartTilesException;
import com.bteam.common.exceptions.NotAStartPositionException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for the initial placement
 * <p>
 * The class is responsible for the placement of the given Characters
 * into the {@link GridService }.
 *
 * @author Albano, Lukas
 * @version 1.0
 */
public class GridPlacementService {

    private GridPlacementService() {
    }

    /**
     * Places characters in the allowed starting rows at the bottom of the grid.
     *
     * @param gridService    GridService that manages the grid
     * @param characters     list of CharacterEntity objects to place in the grid
     * @param startTilesRows array with the exact row numbers allowed for placement
     * @throws DestinationInvalidException         if a specified position is out of bounds
     * @throws DestinationAlreadyOccupiedException if a specified position is already occupied
     * @throws NotAStartPositionException          if the destination Tile is not a valid starting position
     * @throws FullStartTilesException             if there are more characters than available start tiles
     */
    public static void placeCharacters(GridService gridService, List<CharacterEntity> characters, int[] startTilesRows)
        throws DestinationInvalidException, DestinationAlreadyOccupiedException, NotAStartPositionException, FullStartTilesException {

        List<Integer> sortedRows = Arrays.stream(startTilesRows)
            .boxed()
            .sorted((a, b) -> Integer.compare(b, a))
            .collect(Collectors.toList());

        int gridCols = gridService.getGridModel().getCols();

        int availableSpaces = gridCols * sortedRows.size();

        if (characters.size() > availableSpaces) {
            throw new FullStartTilesException();
        }

        int characterIndex = 0;
        for (int row : sortedRows) {
            for (int col = 0; col < gridCols && characterIndex < characters.size(); col++) {
                Vector2D position = new Vector2D(col, row);

                if (gridService.getTileAt(position).isStartTile()) {
                    if (gridService.getTileAt(position).getCharacter() != null) {
                        throw new DestinationAlreadyOccupiedException(position);
                    }

                    CharacterEntity character = characters.get(characterIndex);
                    gridService.setCharacterTo(position, character);

                    characterIndex++;
                }

                if (characterIndex >= characters.size()) {
                    return;
                }
            }
        }
    }
}

