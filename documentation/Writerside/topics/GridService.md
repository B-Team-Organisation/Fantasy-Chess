# Grid Service

`Author: Lukas Walker`

The Grid Service wraps a [](GridModel.md) and hosts methods providing information about the [](GridModel.md) or to manipulate it.
It only holds a single [](GridModel.md):

````Java
private final GridModel gridModel;
````

The following tasks can be accomplished using the Grid Service:

## Information retrieval

- Retrieve a character from a specified position
- Retrieve the [](TileModel.md) of a specific position
- Retrieve the dimensions of the [](GridModel.md)
- Retrieve the underlying [](GridModel.md)
- Check if a given position is a valid position on the grid
    
## Grid Manipulation

- Swap the positions of two characters on the grid
- Move a characters from a tile to another
- Place a character on a specific tile
- Remove a character from a specific tile
- Flag a number of rows as [start tiles](GameScreen.md#initialisation-phase):

For implementation details, please check out the [JavaDoc](https://b-team-organisation.github.io/Fantasy-Chess/java-docs/common/com/bteam/common/models/GridService.html)