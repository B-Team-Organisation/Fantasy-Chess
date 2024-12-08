package com.bteam.fantasychess_client.services;

import com.bteam.common.entities.CharacterEntity;
import com.bteam.common.models.GridModel;
import com.bteam.common.models.GridService;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class that gives access to important gamestate objects
 * <p>
 * A globally available service class that gives access to the current {@link GridService} and a lists of {@link CharacterEntity}.
 *
 * @author lukas
 */
public class ClientGameStateService {
    private GridService gridService;
    private List<CharacterEntity> characters;

    private List<CharacterEntity> friendlyCharacters;
    private List<CharacterEntity> enemyCharacters;

    /**
     * Default constructor of {@link ClientGameStateService}
     *
     */
    public ClientGameStateService(){
        characters = new ArrayList<>();
        friendlyCharacters = new ArrayList<>();
        enemyCharacters = new ArrayList<>();
    }

    /**
     * Registers a new game in the object
     * <p>
     * Resets all previous information
     */
    public void registerNewGame(int rows, int cols){
        gridService = new GridService(new GridModel(rows, cols));

        characters.clear();
        friendlyCharacters.clear();
        enemyCharacters.clear();
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
     * Updates the {@link CharacterEntity} lists
     * <p>
     * Needs the playerId to check if the {@link CharacterEntity} is friendly or not.
     *
     * @param characters updated list of all {@link CharacterEntity}
     * @param playerId your playerId
     */
    public void updateCharacters(List<CharacterEntity> characters, String playerId) {
        this.characters = characters;

        friendlyCharacters.clear();
        enemyCharacters.clear();

        for (CharacterEntity character : characters) {
            if (character.getPlayerId().equals(playerId)) {
                friendlyCharacters.add(character);
            } else {
                enemyCharacters.add(character);
            }
        }
    }

    /**
     * Tells you how many {@link CharacterEntity} you how have on the board
     *
     * @return amount of friendly {@link CharacterEntity}
     */
    public int getFriendlyCharacterCount(){
        return friendlyCharacters.size();
    }

    /**
     * Tells you how many {@link CharacterEntity} your enemy has on the board
     *
     * @return amount of enemy {@link CharacterEntity}
     */
    public int getEnemyCharacterCount(){
        return enemyCharacters.size();
    }
}
