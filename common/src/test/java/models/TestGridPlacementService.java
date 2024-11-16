package models;

import entities.CharacterEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


import java.util.List;

import static models.GridPlacementService.placeCharacters;
import static org.junit.jupiter.api.Assertions.*;

public class TestGridPlacementService {

    private static CharacterEntity character1;
    private static CharacterEntity character2;
    private static CharacterEntity character3;
    private static CharacterEntity character4;
    private static CharacterEntity character5;
    private static CharacterEntity character6;
    private static CharacterEntity character7;
    private static CharacterEntity character8;


    private GridModel gridModel;
    private GridService gridService;

    @BeforeAll
    static void beforeAll() {
        character1 = Mockito.mock(CharacterEntity.class);
        character2 = Mockito.mock(CharacterEntity.class);
        character3 = Mockito.mock(CharacterEntity.class);
        character4 = Mockito.mock(CharacterEntity.class);
        character5 = Mockito.mock(CharacterEntity.class);
        character6 = Mockito.mock(CharacterEntity.class);
        character7 = Mockito.mock(CharacterEntity.class);
        character8 = Mockito.mock(CharacterEntity.class);
    }

    @BeforeEach
    void beforeEach() {
        gridModel = new GridModel(5, 5);
        gridService = new GridService(gridModel);
    }

    @Test
    void testBottomLeftPlacement() throws DestinationInvalidException {

        assertDoesNotThrow(() -> gridService.setStartTiles(new int[]{3, 4}));
        assertDoesNotThrow(() ->
                placeCharacters(gridService, List.of(character1, character2, character3, character4, character5, character6, character7, character8), new int[]{3, 4})
        );

        assertEquals(character1, gridService.getCharacterAt(new Vector2D(0, 4)));
        assertEquals(character2, gridService.getCharacterAt(new Vector2D(1, 4)));
        assertEquals(character3, gridService.getCharacterAt(new Vector2D(2, 4)));
        assertEquals(character6, gridService.getCharacterAt(new Vector2D(0, 3)));
        assertEquals(character8, gridService.getCharacterAt(new Vector2D(2, 3)));
        assertNotEquals(character7, gridService.getCharacterAt(new Vector2D(3, 3)));
    }


    @Test
    void testErrorsPlacement() {
        int[] startTilesRows = {4, 3};


        assertThrows(DestinationInvalidException.class, () ->
                placeCharacters(gridService, List.of(character1), new int[]{-1})
        );
        assertDoesNotThrow(() -> gridService.setStartTiles(startTilesRows));

        assertDoesNotThrow(() ->
                placeCharacters(gridService, List.of(character1, character2, character3), startTilesRows)
        );
        assertThrows(DestinationAlreadyOccupiedException.class, () ->
                placeCharacters(gridService, List.of(character4), new int[]{4})
        );


    }

    @Test
    void testConcrete() {

        assertDoesNotThrow(() -> gridService.setStartTiles(new int[]{3, 4}));
        assertDoesNotThrow(() -> placeCharacters(gridService, List.of(character1, character2), new int[]{3, 4}));

        GridModel anotherGridModel = new GridModel(5, 5);
        GridService anotherGridService = new GridService(anotherGridModel);

        assertDoesNotThrow(() -> anotherGridService.setStartTiles(new int[]{3, 4}));
        assertDoesNotThrow(() -> placeCharacters(anotherGridService, List.of(character1, character2), new int[]{3, 4}));

        assertEquals(gridModel, anotherGridModel);
    }


    @Test
    void testMoreCharactersThanAvailableSpace() {
        int[] startTilesRows = {2};
        List<CharacterEntity> characters = List.of(character1, character2, character3, character4, character5, character6);
        assertDoesNotThrow(() -> gridService.setStartTiles(startTilesRows));
        assertThrows(FullStartTilesException.class, () ->
                placeCharacters(gridService, characters, startTilesRows)
        );
    }


}
