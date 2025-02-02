package com.bteam.common.utils;

import com.bteam.common.entities.CharacterEntity;
import com.bteam.common.models.AttackDataModel;
import com.bteam.common.models.GridService;
import com.bteam.common.models.MovementDataModel;
import com.bteam.common.models.Vector2D;
import com.bteam.common.models.TurnResult;
import com.bteam.common.models.ValidationResult;

import java.util.ArrayList;
import java.util.List;

import static com.bteam.common.utils.CommandValidator.validateCommands;
import static com.bteam.common.utils.RelationUtils.getCharacterWithId;

/**
 * Service for turn-based game logic.
 * <p>
 * This service handles the logic for processing movements, attacks, character deaths,
 * and determining the winner for each turn in the game.
 *
 * @author Albano, Jacinto
 * @version 1.0
 */
public class TurnLogic {

    private TurnLogic() {
    }

    /**
     * Applies all the commands (movements and attacks) for a turn and updates the character states.
     *
     * @param moves       List of movement commands received.
     * @param characters  List of all characters in the game.
     * @param attacks     List of attack commands received.
     * @param gridService Grid service for the game board.
     * @param hostID      PlayerID of the hosting player.
     * @return A {@link TurnResult} object containing the updated character states,
     * valid movements, valid attacks, and any movement conflicts.
     */
    public static TurnResult applyCommands(
            List<MovementDataModel> moves,
            List<CharacterEntity> characters,
            List<AttackDataModel> attacks,
            GridService gridService,
            String hostID) {

        ValidationResult validation = validateCommands(characters, moves, attacks, gridService, hostID);

        List<MovementDataModel> validMovements = validation.getValidMoves();
        List<AttackDataModel> validAttacks = validation.getValidAttacks();

        applyMovement(validMovements, characters, gridService);
        applyAttacks(validAttacks, characters, gridService, hostID);

        checkForDeaths(characters, gridService);

        String winner = null;
        if (characters.isEmpty()) winner = "DRAW";
        else winner = checkForWinner(characters);

        return new TurnResult(characters, validation.getMovementConflicts(), validMovements, validAttacks, winner);
    }

    public static void checkForDeaths(List<CharacterEntity> characters, GridService gridService) {
        List<CharacterEntity> deadCharacters = new ArrayList<>();

        for (CharacterEntity character : characters) {
            if (character.getHealth() <= 0) {
                deadCharacters.add(character);
            }
        }

        for (CharacterEntity character : deadCharacters) {
            characters.remove(character);
            try {
                gridService.removeCharacterFrom(character.getPosition());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Applies all valid movements to the characters.
     *
     * @param intendedMovements List of valid movement commands.
     * @param characters        List of all characters in the game.
     * @param gridService       The {@link GridService} of the game.
     */
    public static void applyMovement(List<MovementDataModel> intendedMovements, List<CharacterEntity> characters, GridService gridService) {
        for (MovementDataModel movement : intendedMovements) {
            CharacterEntity character = getCharacterWithId(characters, movement.getCharacterId());
            assert character != null;
            try {
                gridService.moveCharacter(character.getPosition(), movement.getMovementVector());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Applies all valid attacks to the characters.
     *
     * @param intendedAttacks List of valid attack commands.
     * @param characters      List of all characters in the game.
     * @param gridService     The {@link GridService} of the game.
     */
    public static void applyAttacks(List<AttackDataModel> intendedAttacks, List<CharacterEntity> characters,
                                    GridService gridService, String hostId) {

        for (AttackDataModel attackMove : intendedAttacks) {
            String attackerId = attackMove.getAttacker();
            Vector2D attackPosition = attackMove.getAttackPosition();

            CharacterEntity attacker = getCharacterWithId(characters, attackerId);
            assert attacker != null;

            var pattern = attacker.getCharacterBaseModel().getAttackPatterns()[0];
            if (attacker.getPlayerId().equals(hostId)) pattern = pattern.reversePattern();

            Vector2D[] attackArea = pattern.getAreaOfEffect(attacker.getPosition(), attackPosition);

            int damage = attacker.getCharacterBaseModel().getAttackPower();
            for (Vector2D affectedPosition : attackArea) {
                try {
                    CharacterEntity target = gridService.getCharacterAt(affectedPosition);
                    if (target != null) {
                        target.setHealth(target.getHealth() - damage);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Checks if the game has a winner by determining if all remaining characters belong to one player.
     *
     * @param characters List of all characters in the game.
     * @return The player ID of the winner, or {@code null} if no winner exists.
     */
    public static String checkForWinner(List<CharacterEntity> characters) {
        String playerId = null;

        for (CharacterEntity character : characters) {
            if (playerId == null) {
                playerId = character.getPlayerId();
            } else if (!playerId.equals(character.getPlayerId())) {
                return null;
            }
        }

        return playerId;
    }
}
