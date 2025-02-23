package com.bteam.fantasychess_client.utils;

import com.bteam.common.entities.CharacterEntity;
import com.bteam.common.models.*;
import com.bteam.fantasychess_client.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import static com.bteam.fantasychess_client.Main.getWebSocketService;

/**
 * Stores lists of mocks to render in the scene
 *
 * @author jacinto lukas
 */
public class GameMockStore {
    private static List<CharacterEntity> characters = new ArrayList<>();

    static {
        PatternStore mockPatternStore = new PatternStore() {
            private final Map<String, PatternModel> patterns = new HashMap<>() {{
                put(
                    "bite", new PatternModel("xxx\nx x\n   ", new HashMap<>(), "bite")
                );
                put(
                    "frontAndBack",
                    new PatternModel("xxx\n   \nxxx", new HashMap<>(), "frontAndBack")
                );
                put(
                    "tackle", new PatternModel("xxx\n   \n   ", new HashMap<>(), "tackle")
                );
                put(
                    "dodge", new PatternModel("xxx\nx x\nxxx", new HashMap<>(), "dodge")
                );
                put(
                    "jump", new PatternModel("xxxxx\nx   x\nx   x\nx   x\nxxxxx", new HashMap<>(), "jump")
                );
                put(
                    "charge", new PatternModel("xxxxx\n xxx \n     \n     \n     ", new HashMap<>(), "charge")
                );
            }};

            @Override
            public PatternModel getPatternByName(String patternName) {
                return patterns.get(patternName);
            }
        };

        Map<String, PatternModel> patternModels = new HashMap<>();

        PatternService biteService = null;
        PatternService frontAndBackService = null;
        PatternService tackleService = null;
        PatternService dodgeService = null;
        PatternService jumpService = null;
        PatternService chargeService = null;

        try {
            biteService = new PatternService(
                mockPatternStore.getPatternByName("bite"),
                mockPatternStore
            );

            frontAndBackService = new PatternService(
                mockPatternStore.getPatternByName("frontAndBack"),
                mockPatternStore
            );

            tackleService = new PatternService(
                mockPatternStore.getPatternByName("tackle"),
                mockPatternStore
            );

            dodgeService = new PatternService(
                mockPatternStore.getPatternByName("dodge"),
                mockPatternStore
            );

            jumpService = new PatternService(
                mockPatternStore.getPatternByName("jump"),
                mockPatternStore
            );

            chargeService = new PatternService(
                mockPatternStore.getPatternByName("charge"),
                mockPatternStore
            );
        } catch (Exception e){
            Main.getLogger().log(Level.SEVERE,e.getMessage());
        }

        Map<String, CharacterDataModel> mockBaseModels = new HashMap<>();

        mockBaseModels.put("badger", new CharacterDataModel("badger","Tenacious scrapper, burrowing beast",
            15, 20, new PatternService[]{frontAndBackService}, new PatternService[]{dodgeService},
            "long range shots", "1 move in each direction"
        ));

        mockBaseModels.put("wolf", new CharacterDataModel("wolf", "Agile predator with sharp instincts",
            20, 15, new PatternService[]{biteService}, new PatternService[]{jumpService},
            "long range shots", "1 move in each direction"
        ));

        mockBaseModels.put("boar", new CharacterDataModel("boar", "Ferocious charger, armored tank",
            25, 10, new PatternService[]{tackleService}, new PatternService[]{chargeService},
            "long range shots", "1 move in each direction"
        ));

        mockBaseModels.put("stag", new CharacterDataModel("stag","Noble guardian, antlered sentinel",
            35, 15, new PatternService[]{frontAndBackService},new PatternService[]{jumpService},
            "long range shots", "1 move in each direction"
        ));

        mockBaseModels.put("blossom", new CharacterDataModel("blossom","Blossom",
            35, 5, new PatternService[]{frontAndBackService},new PatternService[]{jumpService},
            "long range shots", "1 move in each direction"
        ));

        characters.add(new CharacterEntity(mockBaseModels.get("badger"), "0", mockBaseModels.get("badger").getHealth(), new Vector2D(-1,-1), getWebSocketService().getUserid()));
        characters.add(new CharacterEntity(mockBaseModels.get("wolf"), "1", mockBaseModels.get("wolf").getHealth()-5, new Vector2D(-1,-1), getWebSocketService().getUserid()));
        characters.add(new CharacterEntity(mockBaseModels.get("boar"), "2", mockBaseModels.get("boar").getHealth()-10, new Vector2D(-1,-1), getWebSocketService().getUserid()));
        characters.add(new CharacterEntity(mockBaseModels.get("stag"), "3", mockBaseModels.get("stag").getHealth()-25, new Vector2D(-1,-1), getWebSocketService().getUserid()));
        characters.add(new CharacterEntity(mockBaseModels.get("blossom"), "4", mockBaseModels.get("blossom").getHealth()-34, new Vector2D(-1,-1), getWebSocketService().getUserid()));
    }

    /**
     * Gives you a list of mock character entities to use for debugging purposes
     *
     * @return a list of {@link CharacterEntity}
     */
    public static List<CharacterEntity> getCharacterMocks() {
        return characters;
    }
}
