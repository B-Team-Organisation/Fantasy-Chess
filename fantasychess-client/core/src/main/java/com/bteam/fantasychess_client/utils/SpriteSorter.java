package com.bteam.fantasychess_client.utils;

import com.bteam.fantasychess_client.graphics.CharacterSprite;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Sorts the given sprites by their y-Position
 * <p>
 * Makes it so when the sprites are drawn in order,
 * the ones in front are drawn above the ones in the back
 *
 * @version 1.0
 * @author lukas
 */
public class SpriteSorter {
    public static void sortByY(List<CharacterSprite> characterSprites){
        characterSprites.sort(new Comparator<CharacterSprite>() {
            @Override
            public int compare(CharacterSprite s1, CharacterSprite s2) {
                return Float.compare(s2.getY(), s1.getY());
            }
        });
    }
}
