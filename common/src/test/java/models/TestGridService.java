package models;

import Exceptions.DestinationInvalidException;
import Exceptions.NoCharacterToMoveException;
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
    private static CharacterEntity character3;


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
        character3 = Mockito.mock(CharacterEntity.class);
    }

    @BeforeEach
    void beforeEach() {
        gridModel = new GridModel(5,5);
        gridService = new GridService(gridModel);
    }

    @Test
    void testCheckPositionInvalid(){
        assertTrue(gridService.checkPositionInvalid(vec1invalid));
        assertTrue(gridService.checkPositionInvalid(vec2invalid));
        assertTrue(gridService.checkPositionInvalid(vec3invalid));
        assertTrue(gridService.checkPositionInvalid(vec4invalid));
        assertFalse(gridService.checkPositionInvalid(vec5valid));
    }

    @Test
    void testGetTileAt(){
        assertThrows(DestinationInvalidException.class, () -> gridService.getTileAt(vec1invalid));
        assertDoesNotThrow(() -> assertEquals(gridModel.getTileGrid()[0][0],gridService.getTileAt(new Vector2D(0,0))));
    }

    @Test
    void testSetCharacterTo(){
        assertDoesNotThrow(() -> {gridService.setCharacterTo(vec5valid,character1);});
        assertThrows(DestinationInvalidException.class, () -> {gridService.setCharacterTo(vec1invalid,character1);});
        assertThrows(DestinationInvalidException.class, () -> {gridService.setCharacterTo(vec5valid,character2);});
    }

    @Test
    void testGetCharacterFrom(){
        assertThrows(DestinationInvalidException.class,() -> {gridService.getCharacterAt(vec1invalid);});
        assertDoesNotThrow(() -> {assertNull(gridService.getCharacterAt(vec5valid));});
        assertDoesNotThrow(() -> {gridService.setCharacterTo(vec5valid,character1);});
        assertDoesNotThrow(() -> {gridService.getCharacterAt(vec5valid);});
    }

    @Test
    void testRemoveCharacterFrom(){
        assertThrows(DestinationInvalidException.class, () -> {gridService.removeCharacterFrom(vec1invalid);});
        assertThrows(DestinationInvalidException.class, () -> {gridService.removeCharacterFrom(vec5valid);});
        assertDoesNotThrow(()->{gridService.setCharacterTo(vec5valid,character1);});
        assertDoesNotThrow(()->{gridService.setCharacterTo(vec6valid,character2);});
        assertDoesNotThrow(()->{gridService.setCharacterTo(vec7valid,character3);});
        assertDoesNotThrow(()->{gridService.removeCharacterFrom(vec5valid);});
    }

    @Test
    void testMoveCharacter(){
        assertThrows(DestinationInvalidException.class, () -> {gridService.moveCharacter(vec1invalid,vec2invalid);});
        assertThrows(NoCharacterToMoveException.class, () -> {gridService.moveCharacter(vec5valid,vec1invalid);});
        assertDoesNotThrow(() -> {gridService.setCharacterTo(vec5valid,character1);});
        assertDoesNotThrow(() -> {gridService.setCharacterTo(vec6valid,character2);});
        assertThrows(DestinationInvalidException.class, () -> {gridService.moveCharacter(vec5valid,vec1invalid);});
        assertThrows(DestinationInvalidException.class,()->{gridService.moveCharacter(vec5valid,vec6valid);});

        assertDoesNotThrow(() -> {gridService.moveCharacter(vec5valid,vec7valid);});
        assertThrows(DestinationInvalidException.class, () -> {gridService.moveCharacter(vec6valid,vec7valid);});
    }
}
