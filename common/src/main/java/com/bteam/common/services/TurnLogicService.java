package com.bteam.common.services;

import java.util.ArrayList;
import java.util.List;

import com.bteam.common.entities.CharacterEntity;
import com.bteam.common.models.*;

import static com.bteam.common.services.CommandValidator.validateCommands;

/**
 * Service for turn-based game logic.
 * <p>
 * This service handles the logic for processing movements, attacks, character deaths,
 * and determining the winner for each turn in the game.
 *
 * @author Albano, Jacinto
 * @version 1.0
 */
public class TurnLogicService {

    private TurnLogicService() {}

    /**
     * Applies all the commands (movements and attacks) for a turn and updates the character states.
     *
     * @param moves      List of movement commands received.
     * @param characters List of all characters in the game.
     * @param attacks    List of attack commands received.
     * @param grid       Grid model representing the game board.
     * @return A {@link TurnResult} object containing the updated character states,
     *         valid movements, valid attacks, and any movement conflicts.
     */
    public static TurnResult applyCommands(
        List<MovementDataModel> moves,
        List<CharacterEntity> characters,
        List<AttackDataModel> attacks,
        GridModel grid) {

        TurnResult result = validateCommands(characters, moves, attacks, new GridService(grid));

        List<MovementDataModel> validMovements = result.getValidMoves();
        List<AttackDataModel> validAttacks = result.getValidAttacks();

        List<CharacterEntity> charactersAfterMovement = applyMovement(validMovements, characters);
        List<CharacterEntity> charactersAfterAttacks = applyAttacks(validAttacks, charactersAfterMovement);

        result.setUpdatedCharacters(charactersAfterAttacks);
        return result;
    }

    /**
     * Applies all valid movements to the characters.
     *
     * @param intendedMovements List of valid movement commands.
     * @param characters        List of all characters in the game.
     * @return The updated list of characters after movements are applied.
     */
    private static List<CharacterEntity> applyMovement(List<MovementDataModel> intendedMovements, List<CharacterEntity> characters) {
        List<CharacterEntity> charactersAfterMovement = new ArrayList<>(characters);

        for (MovementDataModel movement : intendedMovements) {
            String characterToMove = movement.getCharacterId();

            for (CharacterEntity character : charactersAfterMovement) {
                if (character.getId().equals(characterToMove)) {
                    character.setPosition(movement.getMovementVector());
                    break;
                }
            }
        }

        return charactersAfterMovement;
    }

    /**
     * Applies all valid attacks to the characters.
     *
     * @param intendedAttacks List of valid attack commands.
     * @param characters      List of all characters in the game.
     * @return The updated list of characters after attacks are applied.
     */
    private static List<CharacterEntity> applyAttacks(List<AttackDataModel> intendedAttacks, List<CharacterEntity> characters) {
        List<CharacterEntity> charactersAfterAttacks = new ArrayList<>(characters);

        for (AttackDataModel attackMove : intendedAttacks) {
            String attackerId = attackMove.getAttacker();
            Vector2D attackPoint = attackMove.getAttackPosition();

            CharacterEntity attacker = findCharacterById(attackerId, charactersAfterAttacks);
            if (attacker == null) {
                continue;
            }

            Vector2D[] attackArea = attacker.getCharacterBaseModel()
                    .getAttackPatterns()[0]
                    .getAreaOfEffect(attacker.getPosition(), attackPoint);

            for (Vector2D affectedPosition : attackArea) {
                for (CharacterEntity target : new ArrayList<>(charactersAfterAttacks)) {
                    if (target.getPosition().equals(affectedPosition)) {

                        int newHealth = target.getHealth() - attacker.getCharacterBaseModel().getAttackPower();
                        target.setHealth(newHealth);

                        if (newHealth <= 0) {
                            charactersAfterAttacks.remove(target);
                        }
                    }
                }
            }
        }

        return charactersAfterAttacks;
    }

    /**
     * Finds a character by its unique ID from a list of characters.
     *
     * @param id         The ID of the character to find.
     * @param characters The list of characters to search.
     * @return The matching {@link CharacterEntity}, or {@code null} if not found.
     */
    private static CharacterEntity findCharacterById(String id, List<CharacterEntity> characters) {
        for (CharacterEntity character : characters) {
            if (character.getId().equals(id)) {
                return character;
            }
        }
        return null;
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
