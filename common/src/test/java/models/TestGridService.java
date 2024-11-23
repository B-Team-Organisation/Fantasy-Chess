package models;


import com.bteam.common.exceptions.DestinationInvalidException;
import com.bteam.common.exceptions.DestinationAlreadyOccupiedException;
import com.bteam.common.exceptions.NoCharacterFoundException;
import com.bteam.common.exceptions.NotAStartPositionException;
import com.bteam.common.entities.CharacterEntity;
import com.bteam.common.models.CharacterDataModel;
import com.bteam.common.models.Vector2D;
import com.bteam.common.models.GridService;
import com.bteam.common.models.GridModel;
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


    }

    @BeforeEach
    void beforeEach() {
        gridModel = new GridModel(5,5);
        gridService = new GridService(gridModel);

        character1 = new CharacterEntity(Mockito.mock(CharacterDataModel.class),0,null,"");
        character2 = new CharacterEntity(Mockito.mock(CharacterDataModel.class),0,null,"");
    }

    @Test
    void testSetStartTiles(){
        // Check invalid index
        assertThrows(DestinationInvalidException.class, () -> {gridService.setStartTiles(new int[]{-1});});
        // Check valid indices
        assertDoesNotThrow(() -> {gridService.setStartTiles(new int[]{0,1,2});});
        // Check worked
        assertTrue(gridModel.getTileGrid()[0][0].getStartTile());
        assertFalse(gridModel.getTileGrid()[3][0].getStartTile());
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
        // Check invalid position (not a starting position)
        assertThrows(NotAStartPositionException.class,()->{gridService.setCharacterTo(vec5valid,character1);});
        assertNotEquals(vec5valid,character1.getPosition());
        // Check valid position
        gridModel.getTileGrid()[vec5valid.getY()][vec5valid.getX()].setStartTile(true);
        assertDoesNotThrow(() -> {gridService.setCharacterTo(vec5valid,character1);});
        assertEquals(vec5valid,character1.getPosition());
        // Check invalid position (out of bounds)
        assertThrows(DestinationInvalidException.class, () -> {gridService.setCharacterTo(vec1invalid,character1);});
        assertNotEquals(vec1invalid,character1.getPosition());
        // Check occupied position
        assertThrows(DestinationAlreadyOccupiedException.class, () -> {gridService.setCharacterTo(vec5valid,character2);});
        assertNotEquals(vec5valid,character2.getPosition());
    }

    @Test
    void testSwapCharacters(){
        assertDoesNotThrow(() -> gridService.setStartTiles(new int[]{0,1,2,3,4}));
        gridModel.getTileGrid()[vec5valid.getY()][vec6valid.getX()].setStartTile(true);
        // Check both tiles out of bounds
        assertThrows(DestinationInvalidException.class, () -> {gridService.swapCharacters(vec1invalid,vec2invalid);});
        // Check first tile invalid
        assertThrows(DestinationInvalidException.class, () -> {gridService.swapCharacters(vec1invalid,vec5valid);});
        // Check first tile empty
        assertThrows(NoCharacterFoundException.class, () -> {gridService.swapCharacters(vec5valid,vec5valid);});

        assertDoesNotThrow(() -> {gridService.setCharacterTo(vec5valid,character1);});
        // Check first tile valid second invalid
        assertThrows(DestinationInvalidException.class, () -> {gridService.swapCharacters(vec5valid,vec1invalid);});
        // Check first tile valid second empty
        assertThrows(NoCharacterFoundException.class, () -> {gridService.swapCharacters(vec5valid,vec6valid);});

        assertDoesNotThrow(() -> {gridService.setCharacterTo(vec6valid,character2);});
        // Check swap successful
        assertDoesNotThrow(() -> {gridService.swapCharacters(vec5valid,vec6valid);});
        assertDoesNotThrow(() -> {assertEquals(character1,gridService.getCharacterAt(vec6valid));});
        assertEquals(character1.getPosition(),vec6valid);
        assertDoesNotThrow(() -> {assertEquals(character2,gridService.getCharacterAt(vec5valid));});
        assertEquals(character2.getPosition(),vec5valid);
    }

    @Test
    void testGetCharacterFrom(){
        // Check invalid position
        assertThrows(DestinationInvalidException.class,() -> {gridService.getCharacterAt(vec1invalid);});
        // Check empty position
        assertDoesNotThrow(() -> {assertNull(gridService.getCharacterAt(vec5valid));});

        gridModel.getTileGrid()[vec5valid.getY()][vec5valid.getX()].setStartTile(true);
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

        gridModel.getTileGrid()[vec5valid.getY()][vec5valid.getX()].setStartTile(true);
        assertDoesNotThrow(()->{gridService.setCharacterTo(vec5valid,character1);});
        // Check occupied position
        assertDoesNotThrow(()->{gridService.removeCharacterFrom(vec5valid);});
        assertNull(character1.getPosition());
    }

    @Test
    void testMoveCharacter(){
        // Check from invalid to invalid
        assertThrows(DestinationInvalidException.class, () -> {gridService.moveCharacter(vec1invalid,vec2invalid);});
        // Check from valid to invalid
        assertThrows(NoCharacterFoundException.class, () -> {gridService.moveCharacter(vec5valid,vec1invalid);});

        gridModel.getTileGrid()[vec5valid.getY()][vec5valid.getX()].setStartTile(true);
        assertDoesNotThrow(() -> {gridService.setCharacterTo(vec5valid,character1);});
        gridModel.getTileGrid()[vec5valid.getY()][vec6valid.getX()].setStartTile(true);
        assertDoesNotThrow(() -> {gridService.setCharacterTo(vec6valid,character2);});
        // Check from valid occupied to invalid
        assertThrows(DestinationInvalidException.class, () -> {gridService.moveCharacter(vec5valid,vec1invalid);});
        assertNotEquals(character1.getPosition(),vec1invalid);
        // Check from valid occupied to valid occupied
        assertThrows(DestinationAlreadyOccupiedException.class,()->{gridService.moveCharacter(vec5valid,vec6valid);});
        assertNotEquals(character1.getPosition(),vec6valid);
        // Check from valid occupied to valid empty
        assertDoesNotThrow(() -> {gridService.moveCharacter(vec5valid,vec7valid);});
        assertEquals(character1.getPosition(),vec7valid);
    }
}
