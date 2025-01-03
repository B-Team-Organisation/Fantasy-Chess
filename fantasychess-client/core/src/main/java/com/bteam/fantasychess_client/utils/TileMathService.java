package com.bteam.fantasychess_client.utils;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.bteam.common.models.Vector2D;

/**
 * Mathematical Functions for Grid
 * <p>
 * Offers methods for easy coordinate transformation between world and grid coordinates.
 *
 *
 * @author Jacinto, Lukas
 */
public class TileMathService {
    private final int mapWidth;
    private final int mapHeight;

    private final Vector2 doubleTopCorner;
    private final Vector2 bottomCorner;

    private final Vector2 topCorner;
    private final Vector2 leftCorner;
    private final Vector2 rightCorner;

    private final Vector2D center;



    /**
     * Create a new Service for a map.
     * <p>
     * Corners are calculated for the grid based on center, map and tile sizes,
     * whereas the fake corner is offsetting height differences from the isometric
     * map so that the borders of the map can be treated as perpendicular,
     * simplifying {@link #pointLineProjection}
     *
     * @param mapWidth Width of grid in tiles
     * @param mapHeight Height of grid in tiles
     * @param tiledMap The center in world coordinates
     * @param tilePixelWidth the width of a tile in pixel
     * @param tilePixelHeight the height of a tile in pixel
     */
    public TileMathService(int mapWidth, int mapHeight, TiledMap tiledMap, int tilePixelWidth, int tilePixelHeight) {

        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;

        center = getMapCenter(tiledMap);

        this.leftCorner = new Vector2(center.getX()-(mapWidth/2f)*tilePixelWidth, center.getY());
        this.rightCorner = new Vector2(center.getX()+(mapWidth/2f)*tilePixelWidth,center.getY());
        this.topCorner = new Vector2(center.getX(),center.getY()+(mapHeight/2f)*tilePixelHeight);
        this.bottomCorner = new Vector2(center.getX(),center.getY()-(mapHeight/2f)*tilePixelHeight);

        // tilePixelWidth is correct in this context, because it makes sure the map is treated is a square.
        this.doubleTopCorner = new Vector2(center.getX(),center.getY()+(mapHeight/2f)*tilePixelWidth);
    }

    /**
     * Get the position of the center of a tile.
     * <p>
     * Works by creating two lines on the outer edges of the columns,
     * moving col * the width of a tile on it and then doing the same
     * for row between both points.
     *
     * @param row the row on the grid starting at 0
     * @param col the column on the grid starting at 0
     * @return The position in world coordinates
     */
    public Vector2 gridToWorld(int row, int col) {
        float columnTileUnit = 1f/(this.mapWidth*2); // middle of a tile in percent of a line
        float rowTileUnit = 1f/(this.mapHeight*2);

        col = col*2+1; // get the middle of the tile instead of the edge
        row = row*2+1;

        Vector2 upperColumnPos = topCorner.cpy().lerp(leftCorner, col*columnTileUnit);
        Vector2 lowerColumnPos = rightCorner.cpy().lerp(bottomCorner, col*columnTileUnit);
        return upperColumnPos.lerp(lowerColumnPos, row*rowTileUnit);
    }

    /**
     * Transforms grid coordinates into tiled map coordinates
     *
     * @param grid the grid coordinates
     * @return the tiled map coordinates
     */
    public Vector2D gridToTiled(Vector2D grid) {
        return new Vector2D(grid.getX(), getMapHeight() - 1 - grid.getY());
    }

    /**
     * Get the center of the nearest tile from 2 world coordinates.
     * <p>
     * Projects the coordinates given onto the edges of the grid.
     * Then, the percentage of the projected lines is calculated using
     * the column size (TileUnits) to get the nearest tile.
     *
     * @param x the x world coordinate
     * @param y the y world coordinate
     * @return {@link Vector2D} containing rows and columns on the grid.
     */
    public Vector2D worldToGrid(float x, float y) {
        Vector2 point = new Vector2(x, y + (y-center.getY()));
        float columnTileUnit = 1f / mapWidth;
        float rowTileUnit = 1f / mapHeight;

        // Use fake top corner because the edges aren't perpendicular, avoids angle transformation
        Vector2 columnProjection = pointLineProjection(doubleTopCorner, rightCorner, point.cpy());
        Vector2 rowProjection = pointLineProjection(doubleTopCorner, leftCorner, point.cpy());

        float colPercent = percentOnLine(rowProjection, doubleTopCorner.cpy(), leftCorner.cpy());
        int column = (int)(colPercent/rowTileUnit);
        if (column >= mapWidth) column = mapWidth-1; // cap at grid limit

        float rowPercent = percentOnLine(columnProjection, doubleTopCorner.cpy(), rightCorner.cpy());
        int row = (int)(rowPercent/columnTileUnit);
        if (row >= mapHeight) row = mapHeight-1;

        return new Vector2D(row, column);
    }

    /**
     * Project a point on a line.
     * <p>
     * Simple math, can be found on Wikipedia (orthogonal projection)
     *
     * @param start the location vector of the line
     * @param end the direction vector of the line
     * @param point the point to project onto the line
     * @return the nearest point on the line.
     */
    public Vector2 pointLineProjection(Vector2 start, Vector2 end, Vector2 point) {
        Vector2 directionVector = end.cpy().sub(start);
        Vector2 dirNorm = directionVector.cpy().nor();
        Vector2 lineToPoint = point.cpy().sub(start);
        float projectionFactor = lineToPoint.dot(dirNorm);
        Vector2 scaled = dirNorm.scl(projectionFactor);
        return start.cpy().add(scaled);
    }

    /**
     * Calculate the percentage of a point on a line.
     * <p>
     * Percentage of the point on the line capped between 0.0 and 1.0.
     * The point needs to be on the line.
     *
     * @param point a point on the line
     * @param startPoint the starting point
     * @param endPoint the end point
     * @return Percentage of the distance between the points
     */
    public float percentOnLine(Vector2 point, Vector2 startPoint, Vector2 endPoint) {
        Vector2 lineVector = endPoint.cpy().sub(startPoint);
        float lineLengthSquared = lineVector.len2();

        if (lineLengthSquared < 1e-6f) {
            return 0.0f;
        }

        Vector2 pointVector = point.cpy().sub(startPoint);
        float percentage = pointVector.dot(lineVector) / lineLengthSquared;

        return Math.max(0f, Math.min(1f, percentage));
    }

    /**
     * Get the center of the map.
     *
     * @param tiledMap The map
     * @return The coordinates of the map as {@link Vector2D}
     */
    public Vector2D getMapCenter(TiledMap tiledMap) {
        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        int mapWidthInTiles = layer.getWidth();
        int mapHeightInTiles = layer.getHeight();
        float tileWidth = layer.getTileWidth();
        float tileHeight = layer.getTileHeight() / 2f;
        float centerX = (mapWidthInTiles * tileWidth)/2.0f;
        float centerY = (mapHeightInTiles * tileHeight)/4.0f;
        return new Vector2D((int)centerX,(int)centerY);
    }

    /**
     * Getter for the map center
     */
    public Vector2D getMapCenter() {
        return this.center;
    }

    /**
     * Getter for the map width
     *
     * @return map width
     */
    public int getMapWidth() {
        return mapWidth;
    }

    /**
     * Getter for the map height
     *
     * @return map height
     */
    public int getMapHeight() {
        return mapHeight;
    }

}
