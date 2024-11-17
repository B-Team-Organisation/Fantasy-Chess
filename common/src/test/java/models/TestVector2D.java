package models;

import com.bteam.common.models.Vector2D;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TestVector2D {
    Vector2D vec1 = new Vector2D(1, 2);
    Vector2D vec2 = new Vector2D(3, 4);

    @Test
    void testAddition() {
        Vector2D expected = new Vector2D(4, 6);
        Vector2D actual = vec1.add(vec2);
        assertEquals(
                expected,
                actual,
                "Vector should be " + expected + ", but was " + actual
        );
    }

    @Test
    void testSubtraction() {
        Vector2D expected = new Vector2D(2, 2);
        Vector2D actual = vec2.subtract(vec1);
        assertEquals(
                expected,
                actual,
               "Vector should be " + expected + ", but was " + actual
        );
    }
}
