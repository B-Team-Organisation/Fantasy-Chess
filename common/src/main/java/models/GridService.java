package models;

import Exceptions.DestinationInvalidException;
import Exceptions.NoCharacterToMoveException;
import entities.CharacterEntity;

public class GridService {

    private GridModel gridModel;

    public GridService(GridModel gridModel){
        this.gridModel = gridModel;
    }

    public CharacterEntity getCharacterAt(Vector2D position) throws DestinationInvalidException {
        return getTileAt(position).getCharacter();
    }

    public void moveCharacter(Vector2D from, Vector2D to) throws NoCharacterToMoveException, DestinationInvalidException {
        TileModel originTile = getTileAt(from);
        if (originTile.getCharacter() == null){
            throw new NoCharacterToMoveException();
        }

        TileModel destinationTile = getTileAt(to);
        if (destinationTile.getCharacter() != null){
            throw new DestinationInvalidException(to);
        }

        destinationTile.setCharacter(originTile.getCharacter());
        originTile.setCharacter(null);
    }

    public void setCharacterTo(Vector2D to, CharacterEntity character) throws DestinationInvalidException {
        TileModel tile = getTileAt(to);
        if (tile.getCharacter() != null){
            throw new DestinationInvalidException(to);
        }
        tile.setCharacter(character);
    }

    public CharacterEntity removeCharacterFrom(Vector2D position) throws DestinationInvalidException {
        CharacterEntity character = getCharacterAt(position);
        if (character == null){
            throw new DestinationInvalidException(position);
        }
        getTileAt(position).setCharacter(null);
        return character;
    }

    public TileModel getTileAt(Vector2D position) throws DestinationInvalidException {
        if (checkPositionInvalid(position)){
            throw new DestinationInvalidException(position);
        }
        return gridModel.getTileGrid()[position.getY()][position.getX()];
    }

    public boolean checkPositionInvalid(Vector2D position){
        return !(position.getX() >= 0
                && position.getX() < gridModel.getCols()
                && position.getY() >= 0
                && position.getY() < gridModel.getRows());
    }

    public Vector2D getDimentions(){
        return new Vector2D(gridModel.getRows(), gridModel.getCols());
    }

    public GridModel getGridModel() {
        return gridModel;
    }
}
