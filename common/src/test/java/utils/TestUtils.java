package utils;

import com.bteam.common.entities.CharacterEntity;
import com.bteam.common.models.Vector2D;

import java.util.ArrayList;
import java.util.List;


public class TestUtils {

    public static List<CharacterEntity> deepCopyCharacterList(List<CharacterEntity> originalList) {
        List<CharacterEntity> deepCopy = new ArrayList<>();
        for (CharacterEntity entity : originalList) {

            CharacterEntity copiedEntity = new CharacterEntity(
                    entity.getCharacterBaseModel(),
                    entity.getHealth(),
                    new Vector2D(entity.getPosition().getX(), entity.getPosition().getY()),
                    entity.getPlayerId()
            );
            deepCopy.add(copiedEntity);
        }
        return deepCopy;
    }
}
