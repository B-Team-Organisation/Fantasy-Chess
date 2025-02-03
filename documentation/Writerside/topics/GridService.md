# Grid Service

The Grid Service wraps a [](GridModel.md) and hosts methods providing information about the [](GridModel.md) or to manipulate it.
It only holds a single [](GridModel.md):

````Java
private final GridModel gridModel;
````

The following is a list and short description of all methods provided. As always: please read their javadoc for
in-depth descriptions.

## Information retrieval

Check a position for a character, returning him if existent:
````Java
public CharacterEntity getCharacterAt(Vector2D position) 
    throws DestinationInvalidException {
        return getTileAt(position).getCharacter();
}
````

Check if a given position is a valid position on the grid:

````Java
public boolean checkPositionInvalid(Vector2D position) {
    return !(position.getX() >= 0
        && position.getX() < gridModel.getCols()
        && position.getY() >= 0
        && position.getY() < gridModel.getRows());
}
````

Retrieve the dimensions of the [](GridModel.md):

````Java
public Vector2D getDimensions() {
    return new Vector2D(gridModel.getRows(), gridModel.getCols());
}
````

Retrieve the [](GridModel.md) itself:

````Java
public GridModel getGridModel() {
    return gridModel;
}
````

## Grid Manipulation

Swap the positions of two characters on the grid:

````Java
public void swapCharacters(Vector2D one, Vector2D two) 
    throws DestinationInvalidException, NoCharacterFoundException {
        TileModel tileOne = getTileAt(one);
        if (tileOne.getCharacter() == null) {
            throw new NoCharacterFoundException(one);
        }
        TileModel tileTwo = getTileAt(two);
        if (tileTwo.getCharacter() == null) {
            throw new NoCharacterFoundException(two);
        }
        CharacterEntity characterOne = tileOne.getCharacter();
        tileOne.setCharacter(tileTwo.getCharacter());
        tileTwo.setCharacter(characterOne);

        tileOne.getCharacter().setPosition(one);
        tileTwo.getCharacter().setPosition(two);
}
````

Move a characters between two positions:

`````java
public void moveCharacter(Vector2D from, Vector2D to) 
    throws DestinationInvalidException, DestinationAlreadyOccupiedException,
    NoCharacterFoundException {
        TileModel originTile = getTileAt(from);
        if (originTile.getCharacter() == null) {
            throw new NoCharacterFoundException(from);
        }

        TileModel destinationTile = getTileAt(to);
        if (destinationTile.getCharacter() != null) {
            throw new DestinationAlreadyOccupiedException(to);
        }

        destinationTile.setCharacter(originTile.getCharacter());
        originTile.setCharacter(null);

        destinationTile.getCharacter().setPosition(to);
}
`````

Position a character on a specific tile on the grid:

````Java
public void setCharacterTo(Vector2D to, CharacterEntity character)
    throws DestinationInvalidException, DestinationAlreadyOccupiedException {
        TileModel tile = getTileAt(to);
        if (tile.getCharacter() != null) {
            throw new DestinationAlreadyOccupiedException(to);
        }
        tile.setCharacter(character);
        character.setPosition(to);
   }
````

Remove a character from a specific tile on the grid:

````Java
public CharacterEntity removeCharacterFrom(Vector2D position)
    throws DestinationInvalidException, NoCharacterFoundException {
        CharacterEntity character = getCharacterAt(position);
        if (character == null) {
            throw new NoCharacterFoundException(position);
        }
        getTileAt(position).setCharacter(null);
        character.setPosition(null);
        return character;
    }
````

Retrieve the [](TileModel.md) of a specific position on the grid itself:

````Java
public TileModel getTileAt(Vector2D position)
    throws DestinationInvalidException {
        if (checkPositionInvalid(position)) {
            throw new DestinationInvalidException(position);
        }
        return gridModel.getTileGrid()[position.getY()][position.getX()];
    }
````

Flag a number of rows as [start tiles](GameScreen.md#initialisation-phase):

````Java
public void setStartTiles(int[] rows) throws DestinationInvalidException {
    for (int row : rows) {
        if (checkPositionInvalid(new Vector2D(0, row))) {
            throw new DestinationInvalidException(new Vector2D(0, row));
        }
        for (int col = 0; col < gridModel.getCols(); col++) {
            gridModel.getTileGrid()[row][col].setStartTile(true);
        }
    }
}
````