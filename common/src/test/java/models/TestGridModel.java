package models;

import org.junit.jupiter.api.Test;
import com.bteam.common.models.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestGridModel {

    @Test
    void testConstructor() {
        GridModel gridModel = new GridModel(5,10);
        assertEquals(5, gridModel.getRows());
        assertEquals(10, gridModel.getCols());
        assertEquals(5, gridModel.getTileGrid().length);
        assertEquals(10, gridModel.getTileGrid()[0].length);
        assertThrows(IndexOutOfBoundsException.class,()-> {
            var row = gridModel.getTileGrid()[5];
        });
        assertThrows(IndexOutOfBoundsException.class,()-> {
            var tile = gridModel.getTileGrid()[0][10];
        });
        assertNotNull(gridModel.getTileGrid()[0][0]);
    }

}
