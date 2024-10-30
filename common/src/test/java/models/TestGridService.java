package models;

import Exceptions.DestinationAlreadyOccupiedException;
import Exceptions.DestinationInvalidException;
import Exceptions.NoCharacterFoundException;
import entities.CharacterEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class TestGridService {

    private static Vector2D vec1invalid;
    private static Vector2D vec2invalid;
    private static Vector2D vec3invalid;
    private static Vector2D vec4invalid;
    private static Vector2D vec5valid;
    private static Vector2D vec6valid;
    private static Vector2D vec7valid;

    private static CharacterEntity character1;
    private static CharacterEntity character2;


    private GridModel gridModel;
    private GridService gridService;

    @BeforeAll
    static void beforeAll() {
        vec1invalid = new Vector2D(0,-1);
        vec2invalid = new Vector2D(0,5);
        vec3invalid = new Vector2D(5,0);
        vec4invalid = new Vector2D(-4,0);

        vec5valid = new Vector2D(0,0);
        vec6valid = new Vector2D(1,0);
        vec7valid = new Vector2D(2,0);

        character1 = Mockito.mock(CharacterEntity.class);
        character2 = Mockito.mock(CharacterEntity.class);
    }

    @BeforeEach
    void beforeEach() {
        gridModel = new GridModel(5,5);
        gridService = new GridService(gridModel);
    }

    @Test
    void testCheckPositionInvalid(){
        // Check all four boundaries
        assertTrue(gridService.checkPositionInvalid(vec1invalid));
        assertTrue(gridService.checkPositionInvalid(vec2invalid));
        assertTrue(gridService.checkPositionInvalid(vec3invalid));
        assertTrue(gridService.checkPositionInvalid(vec4invalid));
        // Check valid position
        assertFalse(gridService.checkPositionInvalid(vec5valid));
    }

    @Test
    void testGetTileAt(){
        // Check invalid position
        assertThrows(DestinationInvalidException.class, () -> gridService.getTileAt(vec1invalid));
        // Check valid position
        assertDoesNotThrow(() -> assertEquals(gridModel.getTileGrid()[0][0],gridService.getTileAt(new Vector2D(0,0))));
    }

    @Test
    void testSetCharacterTo(){
        // Check valid position
        assertDoesNotThrow(() -> {gridService.setCharacterTo(vec5valid,character1);});
        // Check invalid position
        assertThrows(DestinationInvalidException.class, () -> {gridService.setCharacterTo(vec1invalid,character1);});
        // Check occupied position
        assertThrows(DestinationAlreadyOccupiedException.class, () -> {gridService.setCharacterTo(vec5valid,character2);});
    }

    @Test
    void testGetCharacterFrom(){
        // Check invalid position
        assertThrows(DestinationInvalidException.class,() -> {gridService.getCharacterAt(vec1invalid);});
        // Check empty position
        assertDoesNotThrow(() -> {assertNull(gridService.getCharacterAt(vec5valid));});

        assertDoesNotThrow(() -> {gridService.setCharacterTo(vec5valid,character1);});
        // Check valid position
        assertDoesNotThrow(() -> {gridService.getCharacterAt(vec5valid);});
    }

    @Test
    void testRemoveCharacterFrom(){
        // Check invalid position
        assertThrows(DestinationInvalidException.class, () -> {gridService.removeCharacterFrom(vec1invalid);});
        // Check empty position
        assertThrows(NoCharacterFoundException.class, () -> {gridService.removeCharacterFrom(vec5valid);});

        assertDoesNotThrow(()->{gridService.setCharacterTo(vec5valid,character1);});
        // Check occupied position
        assertDoesNotThrow(()->{gridService.removeCharacterFrom(vec5valid);});
    }

    @Test
    void testMoveCharacter(){
        // Check from invalid to invalid
        assertThrows(DestinationInvalidException.class, () -> {gridService.moveCharacter(vec1invalid,vec2invalid);});
        // Check from valid to invalid
        assertThrows(NoCharacterFoundException.class, () -> {gridService.moveCharacter(vec5valid,vec1invalid);});

        assertDoesNotThrow(() -> {gridService.setCharacterTo(vec5valid,character1);});
        assertDoesNotThrow(() -> {gridService.setCharacterTo(vec6valid,character2);});
        // Check from valid occupied to invalid
        assertThrows(DestinationInvalidException.class, () -> {gridService.moveCharacter(vec5valid,vec1invalid);});
        // Check from valid occupied to valid occupied
        assertThrows(DestinationAlreadyOccupiedException.class,()->{gridService.moveCharacter(vec5valid,vec6valid);});
        // Check from valid occupied to valid empty
        assertDoesNotThrow(() -> {gridService.moveCharacter(vec5valid,vec7valid);});
    }
}
