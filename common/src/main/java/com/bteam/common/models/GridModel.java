package com.bteam.common.models;

import java.util.Arrays;
import java.util.Objects;

/**
 * Data model for the map grid
 * <p>
 * This class represents a grid of tiles. Its dimensions are defined by the amount
 * of {@link GridModel#rows} and {@link GridModel#cols}.
 * Each tile is represented by an instance of the {@link TileModel} class.
 * The grid is initialized with empty tiles.
 * <p>
 * The coordinate origin of the grid ("tileGrid[0][0]") lies in the top corner of the map.
 * The rows correspond to y-coordinated, the columns represent the x-coordinates.
 * By this definition, a tile is addressed by tileGrid[row][column].
 *
 * @author Lukas, Albano
 * @version 1.1
 */
public class GridModel{
    private int rows;
    private int cols;

    private TileModel[][] tileGrid;

    /**
     * Constructor for the GridModel.
     * <p>
     * Row or col values below 1 will result in a 0x0 grid. This is not advised.
     *
     * @param rows the number of rows in the grid
     * @param cols the number of cols in the grid
     */
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

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public TileModel[][] getTileGrid() {
        return tileGrid;
    }

    @Override
    public String toString() {
        return "GridModel [rows=" + rows + ", cols=" + cols + "]";
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof GridModel && Arrays.deepEquals(tileGrid, ((GridModel) o).tileGrid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rows, cols, Arrays.deepHashCode(tileGrid));
    }
}
