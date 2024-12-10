package com.bteam.common.stores;

import com.bteam.common.models.CharacterDataModel;
import com.bteam.common.models.PatternModel;
import com.bteam.common.models.PatternService;
import com.bteam.common.models.PatternStore;

import java.util.HashMap;
import java.util.Map;

public class CharacterStore {
    public static final Map<String, CharacterDataModel> characters = new HashMap<>();
    public static final Map<String, PatternService> patterns = new HashMap<>();

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
        } catch (Exception e) {
            System.out.println(e);
        }

        characters.put("badger", new CharacterDataModel("badger", "Tenacious scrapper, burrowing beast",
                15, 20, new PatternService[]{frontAndBackService}, new PatternService[]{dodgeService}
        ));

        characters.put("wolf", new CharacterDataModel("wolf", "Agile predator with sharp instincts",
                20, 15, new PatternService[]{biteService}, new PatternService[]{jumpService}
        ));

        characters.put("boar", new CharacterDataModel("boar", "Ferocious charger, armored tank",
                25, 10, new PatternService[]{tackleService}, new PatternService[]{chargeService}
        ));

        characters.put("stag", new CharacterDataModel("stag", "Noble guardian, antlered sentinel",
                35, 15, new PatternService[]{frontAndBackService}, new PatternService[]{jumpService}
        ));

        characters.put("blossom", new CharacterDataModel("blossom", "Blossom",
                35, 5, new PatternService[]{frontAndBackService}, new PatternService[]{jumpService}
        ));
    }

    public static CharacterDataModel getCharacter(String characterName) {
        return characters.get(characterName);
    }

}
