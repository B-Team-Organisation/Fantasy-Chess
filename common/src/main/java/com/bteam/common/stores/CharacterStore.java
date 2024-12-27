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
                put(
                        "plus" , new PatternModel(" x \nxxx\n x ", new HashMap<>(), "plus")
                );
                put(
                        "bomboAttack", new PatternModel(" +++ \n++ ++\n+   +\n++ ++\n +++ ", new HashMap<>(){{put('+',"plus");}}, "bomboAttack" )
                );
                put(
                        "fitzhoothAttack", new PatternModel("+++++\n +++ \n\n\n+    +", new HashMap<>(), "fitzhoothAttack" )
                );
                put(
                        "prometheusSub", new PatternModel(" xxx\nxx xx\n xxx ", new HashMap<>(), "prometheusSub")
                );
                put(
                        "prometheusAttack", new PatternModel("+", new HashMap<>(){{put('+',"prometheusSub");}}, "prometheusSub")
                );
                put(
                        "flashSub", new PatternModel("  +\n + \n+ +\n + \n+  ", new HashMap<>(), "flashSub")
                );
                put (
                        "stablinSub", new PatternModel("x x x\n\n  x  \n\n xxx ", new HashMap<>(), "stablinSub")
                );
                put(
                        "stablinAttack", new PatternModel("\n\n+++", new HashMap<>(){{put('+',"stablinSub");}}, "stablinSub")
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
        PatternService bomboAttackService = null;
        PatternService fitzoothAttackService = null;
        PatternService prometheusAttackService = null;
        PatternService flashSubService = null;
        PatternService stablinSubService = null;

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
            bomboAttackService = new PatternService(
                    mockPatternStore.getPatternByName( "bomboAttack"),
                    mockPatternStore
            );
            fitzoothAttackService = new PatternService(
                    mockPatternStore.getPatternByName("fitzhoothAttack"),
                    mockPatternStore
            );
            prometheusAttackService = new PatternService(
                    mockPatternStore.getPatternByName("prometheusAttack"),
                    mockPatternStore
            );
            flashSubService = new PatternService(
                    mockPatternStore.getPatternByName("flashSub"),
                    mockPatternStore
            );
            stablinSubService = new PatternService(
                    mockPatternStore.getPatternByName("stablin"),
                    mockPatternStore
            );

        } catch (Exception e) {
            System.out.println(e);
        }

        /*characters.put("badger", new CharacterDataModel("badger", "Tenacious scrapper, burrowing beast",
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
*/
        characters.put("blossom", new CharacterDataModel("blossom", "fights with the power of love",
                35, 5, new PatternService[]{frontAndBackService}, new PatternService[]{jumpService}
        ));
        characters.put("bombo", new CharacterDataModel("bombo", "Likes to experiment with explosives",
                28, 8, new PatternService[]{bomboAttackService}, new PatternService[]{dodgeService}
        ));
        characters.put("fitzooth", new CharacterDataModel("fitzhooth", " Robin Hood Fitzhooth simbolizes precision and agility",
                20, 17, new PatternService[]{fitzoothAttackService}, new PatternService[]{chargeService}
        ));
        characters.put("prometheus", new CharacterDataModel("prometheus", " He risked divine wrath to gift humanity fire",
                20, 20, new PatternService[]{prometheusAttackService}, new PatternService[]{dodgeService}
        ));
        characters.put("flash", new CharacterDataModel("flash", " Can hit you from everywhere",
                17, 6, new PatternService[]{flashSubService}, new PatternService[]{chargeService}
        ));
        characters.put("stablin", new CharacterDataModel("stablin", " Likes to stab others in their back",
                21, 25, new PatternService[]{stablinSubService}, new PatternService[]{flashSubService}
        ));
    }

    public static CharacterDataModel getCharacter(String characterName) {
        return characters.get(characterName);
    }

}
