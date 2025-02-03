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

````Java
public GridModel(int rows, int cols){

    if (rows < 1 || cols < 1){
        this.rows = 0;
        this.cols = 0;
        tileGrid = new TileModel[0][0];
        return;
    }

    this.rows = rows;
    this.cols = cols;

    tileGrid = new TileModel[rows][cols];

    for (TileModel[] tileRow : tileGrid) {
        for (int i = 0; i < tileRow.length; i++) {
            tileRow[i] = new TileModel();
        }
    }
}
````

All fields can be accessed using getters.