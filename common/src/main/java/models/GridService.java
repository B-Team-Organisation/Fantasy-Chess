package models;

import Exceptions.DestinationInvalidException;
import Exceptions.NoCharacterFoundException;
import Exceptions.DestinationAlreadyOccupiedException;
import entities.CharacterEntity;

/**
 * Service class for the {@link GridModel}
 * <p>
 * This class wraps a gridModel instance and provides useful functions for grid-manipulation and data-retrieval.
 *
 * @author lukas albano
 * @version 1.0
 */
public class GridService {

    private GridModel gridModel;

    /**
     * Constructor for the GridService class.
     *
     * @param gridModel tileGrid the service class manages
     */
    public GridService(GridModel gridModel){
        this.gridModel = gridModel;
    }

    /**
     * Tries to retrieve a {@link CharacterEntity} from the given {@link Vector2D}-position.
     *
     * @param position {@link Vector2D}-position the position in the tileGrid it searches in
     * @return the {@link CharacterEntity} that occupies the referenced tile, or <code>null</code> if the tile is not occupied
     * @throws DestinationInvalidException if the given position is invalid
     */
    public CharacterEntity getCharacterAt(Vector2D position) throws DestinationInvalidException {
        return getTileAt(position).getCharacter();
    }

    /**
     * Tries to move the {@link CharacterEntity} found at {@code from} over to {@code to}.
     *
     * @param from {@link Vector2D}-position it tries to retrieve the {@link CharacterEntity} from
     * @param to {@link Vector2D}-position it tries to place the {@link CharacterEntity} at
     * @throws NoCharacterFoundException if the {@code from}-position doesn't contain a {@link CharacterEntity}
     * @throws DestinationInvalidException if the {@code from} or {@code to} positions are invalid
     * @throws DestinationAlreadyOccupiedException if the {@code to}-position is already occupied
     */
    public void moveCharacter(Vector2D from, Vector2D to) throws DestinationInvalidException, DestinationAlreadyOccupiedException, NoCharacterFoundException {
        TileModel originTile = getTileAt(from);
        if (originTile.getCharacter() == null){
            throw new NoCharacterFoundException(from);
        }

        TileModel destinationTile = getTileAt(to);
        if (destinationTile.getCharacter() != null){
            throw new DestinationAlreadyOccupiedException(to);
        }

        destinationTile.setCharacter(originTile.getCharacter());
        originTile.setCharacter(null);
    }

    /**
     * Tries to place the given {@link CharacterEntity} at the specified {@link Vector2D}-position.
     * <p>
     * Will throw an exception if anything goes wrong.
     *
     * @param to the {@link Vector2D}-position it tries to place the {@link CharacterEntity} at
     * @param character the {@link CharacterEntity} it tries to place at the {@link Vector2D}
     * @throws DestinationInvalidException if the {@link Vector2D}-position is invalid
     * @throws DestinationAlreadyOccupiedException if the destination-tile is already occupied
     */
    public void setCharacterTo(Vector2D to, CharacterEntity character) throws DestinationInvalidException, DestinationAlreadyOccupiedException {
        TileModel tile = getTileAt(to);
        if (tile.getCharacter() != null){
            throw new DestinationAlreadyOccupiedException(to);
        }
        tile.setCharacter(character);
    }

    /**
     * Removes the {@link CharacterEntity} from the specified {@link Vector2D}-position.
     *
     * @param position the position in the tileGrid it searches in
     * @return {@link CharacterEntity}, which was removed from the tileGrid
     * @throws DestinationInvalidException if the {@link Vector2D}-position is invalid
     * @throws NoCharacterFoundException if the tile doesn't contain a character
     */
    public CharacterEntity removeCharacterFrom(Vector2D position) throws DestinationInvalidException, NoCharacterFoundException {
        CharacterEntity character = getCharacterAt(position);
        if (character == null){
            throw new NoCharacterFoundException(position);
        }
        getTileAt(position).setCharacter(null);
        return character;
    }

    /**
     * Retrieves the {@link TileModel} from the specified {@link Vector2D}-position.
     *
     * @param position the position in the tileGrid it searches in
     * @return the {@link TileModel} at the given position in the {@link GridModel}
     * @throws DestinationInvalidException if the given position is invalid
     */
    public TileModel getTileAt(Vector2D position) throws DestinationInvalidException {
        if (checkPositionInvalid(position)){
            throw new DestinationInvalidException(position);
        }
        return gridModel.getTileGrid()[position.getY()][position.getX()];
    }

    /**
     * Checks if the given {@link Vector2D} lies outside the dimensions of the tileGrid.
     *
     * @param position the position in the tileGrid it searches in
     * @return <code>true</code> if the position is invalid, <code>false</code> otherwise
     */
    public boolean checkPositionInvalid(Vector2D position){
        return !(position.getX() >= 0
                && position.getX() < gridModel.getCols()
                && position.getY() >= 0
                && position.getY() < gridModel.getRows());
    }

    /**
     * A method for easy tileGrid dimension retrieval.
     * <p>
     * Following the definition of the tileGrid model, x represents the cols and y the rows.
     *
     * @return {@link Vector2D} of the tileGrid dimensions
     */
    public Vector2D getDimensions(){
        return new Vector2D(gridModel.getRows(), gridModel.getCols());
    }

    public GridModel getGridModel() {
        return gridModel;
    }
}
