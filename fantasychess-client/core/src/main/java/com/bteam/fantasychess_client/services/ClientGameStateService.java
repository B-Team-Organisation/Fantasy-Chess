package com.bteam.fantasychess_client.services;

import com.bteam.common.entities.CharacterEntity;
import com.bteam.common.models.GridModel;
import com.bteam.common.models.GridService;
import com.bteam.common.utils.TurnLogic;
import com.bteam.common.models.TurnResult;
import com.bteam.common.utils.Event;
import com.bteam.fantasychess_client.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static com.bteam.fantasychess_client.Main.getLogger;
import static com.bteam.fantasychess_client.Main.getWebSocketService;

/**
 * Service class that gives access to important gamestate objects
 * <p>
 * A globally available service class that gives access to the current {@link GridService} and a lists of {@link CharacterEntity}.
 *
 * @author lukas
 * @version 1.0
 */
public class ClientGameStateService {
    private final List<CharacterEntity> friendlyCharacters;
    private final List<CharacterEntity> enemyCharacters;
    public final Event<TurnResult> onApplyTurnResult = new Event<>();
    public final Event<CharacterEntity> onCharacterDeath = new Event<>();
    public final Event<String> onWin = new Event<>();
    private GridService gridService;
    private List<CharacterEntity> characters;
    private String gameId;
    private TurnResult turnResult;
    private HashMap<String,CharacterEntity> characterMapper;

    /**
     * Default constructor of {@link ClientGameStateService}
     */
    public ClientGameStateService() {
        characters = new ArrayList<>();
        friendlyCharacters = new ArrayList<>();
        enemyCharacters = new ArrayList<>();
        gridService = new GridService(new GridModel(9, 9));
        characterMapper = new HashMap<>();
    }

    /**
     * Registers a new game in the object
     * <p>
     * Resets all previous information
     *
     * @param rows row count of the new board
     * @param cols col count of the new board
     */
    public void registerNewGame(int rows, int cols) {
        gridService = new GridService(new GridModel(rows, cols));

        characters.clear();
        friendlyCharacters.clear();
        enemyCharacters.clear();
        characterMapper.clear();
    }

    /**
     * Gives you the current {@link GridService} of the game
     *
     * @return {@link GridService} of the current game
     */
    public GridService getGridService() {
        return gridService;
    }

    /**
     * Gives you the list of all {@link CharacterEntity}
     *
     * @return the {@link CharacterEntity} list
     */
    public List<CharacterEntity> getCharacters() {
        return characters;
    }


    /**
     * Set all Characters on the Board and update its data
     *
     * @param characters list of all Characters to set
     */
    public void setCharacters(List<CharacterEntity> characters) {
        for (CharacterEntity character : characters) {
            try {
                gridService.setCharacterTo(character.getPosition(), character);
            } catch (Exception e) {
                Main.getLogger().log(Level.SEVERE, e.getMessage());
            }
        }
        updateCharacters(characters);
    }

    /**
     * Updates the {@link CharacterEntity} lists
     * <p>
     * Needs the playerId to check if the {@link CharacterEntity} is friendly or not.
     *
     * @param characters updated list of all {@link CharacterEntity}
     */
    public void updateCharacters(List<CharacterEntity> characters) {
        this.characters = characters;

        friendlyCharacters.clear();
        enemyCharacters.clear();
        characterMapper.clear();

        String playerId = getWebSocketService().getUserid();

        for (CharacterEntity character : characters) {

            characterMapper.put(character.getId(),character);

            try {
                gridService.setCharacterTo(character.getPosition(), character);
            } catch (Exception e) {
                Main.getLogger().log(Level.SEVERE, e.getMessage());
            }

            if (character.getPlayerId().equals(playerId)) {
                friendlyCharacters.add(character);
            } else {
                enemyCharacters.add(character);
            }
        }
    }

    /**
     * Returns the character with the given id
     *
     * @param characterId id of the requested {@link CharacterEntity}
     * @return {@link CharacterEntity} with the given id
     */
    public CharacterEntity getCharacterById(String characterId) {
        return characterMapper.get(characterId);
    }

    /**
     * Tells you how many {@link CharacterEntity} you how have on the board
     *
     * @return amount of friendly {@link CharacterEntity}
     */
    public int getFriendlyCharacterCount() {
        return friendlyCharacters.size();
    }

    /**
     * Getter for {@code friendlyCharacters}
     *
     * @return list of all friendly characters on the board
     */
    public List<CharacterEntity> getFriendlyCharacters() {
        return friendlyCharacters;
    }

    /**
     * Tells you how many {@link CharacterEntity} your enemy has on the board
     *
     * @return amount of enemy {@link CharacterEntity}
     */
    public int getEnemyCharacterCount() {
        return enemyCharacters.size();
    }

    /**
     * Getter for {@code enemyCharacters}
     *
     * @return list of all enemy characters on the board
     */
    public List<CharacterEntity> getEnemyCharacters() {
        return enemyCharacters;
    }

    public TurnResult getTurnResult() {
        return turnResult;
    }

    public void applyTurnResult(TurnResult turnResult) {
        this.turnResult = turnResult;
        Main.getLogger().log(Level.SEVERE, "Set turn result");
        TurnLogic.applyMovement(turnResult.getValidMoves(), characters, gridService);
        onApplyTurnResult.invoke(turnResult);
        if (turnResult.getWinner() != null)
            onWin.invoke(turnResult.getWinner());
    }

    /**
     * Synchronize All Characters with current server state
     *
     * @param turnResult
     */
    public void syncCharacters(TurnResult turnResult) {

        List<String> markedForDelete = new ArrayList<>();
        for (CharacterEntity character : characters) {
            if (turnResult.getUpdatedCharacters().stream()
                .anyMatch(c -> c.getId().equals(character.getId()))) continue;
            markedForDelete.add(character.getId());
            try{
                gridService.removeCharacterFrom(character.getPosition());
            } catch (Exception e) {
                getLogger().log(Level.SEVERE, e.getMessage());
            }
            onCharacterDeath.invoke(character);
        }

        updateCharacters(characters.stream().filter(p -> !markedForDelete.contains(p.getId()))
            .collect(Collectors.toList()));

        characters.forEach(character -> {
            var optional = turnResult.getUpdatedCharacters().stream()
                .filter(p -> Objects.equals(p.getId(), character.getId())).findFirst();
            if (optional.isEmpty()) {
                getLogger().log(Level.SEVERE, "Character " + character.getId() + " has no updated character");
                return;
            }
            character.setHealth(optional.get().getHealth());
        });
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public void resetGame() {
        friendlyCharacters.clear();
        enemyCharacters.clear();
        characters.clear();
        gridService = new GridService(new GridModel(9, 9));
        gameId = null;
        turnResult = null;
        onApplyTurnResult.clear();
    }
}
