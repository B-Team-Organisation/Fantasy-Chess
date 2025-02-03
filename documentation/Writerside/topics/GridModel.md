# Grid Model

The grid model represents the playing field of our game. Its core component is a 2D array of [Tile Models](TileModel.md),
its size being defined by a `rows` and a `cols` value. The [](GridService.md) wraps this model to efficiently query and
manipulate it.

````Java
private int rows;
private int cols;

private TileModel[][] tileGrid;
````

This array is dynamically created in the constructor and is theoretically able to represent every
grid size imaginably.

All fields can be accessed using getters.

For implementation details, please check out the [JavaDoc](https://b-team-organisation.github.io/Fantasy-Chess/java-docs/common/com/bteam/common/models/GridModel.html)