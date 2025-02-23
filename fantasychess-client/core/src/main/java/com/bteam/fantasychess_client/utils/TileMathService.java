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

    private final Vector2 bottomCorner;

    private final Vector2 topCorner;
    private final Vector2 leftCorner;
    private final Vector2 rightCorner;

    private final Vector2D center;



    /**
     * Create a new Service for a map.
     * <p>
     * Corners are calculated for the grid based on center, map and tile sizes.
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
     * @return {@link Vector2D} containing rows and columns on the grid, or null if percent not within 0-100.
     */
    public Vector2D worldToGrid(float x, float y) {

        Vector2 point = new Vector2(x, y);
        float columnTileUnit = 1f / mapWidth;
        float rowTileUnit = 1f / mapHeight;
        Vector2 leftTop = topCorner.cpy().sub(leftCorner.cpy());
        Vector2 rightTop = topCorner.cpy().sub(rightCorner.cpy());

        Vector2 columnProjection = lineIntersection(point.cpy(), leftTop.cpy(), rightCorner.cpy(), rightTop.cpy());
        Vector2 rowProjection = lineIntersection(point.cpy(), rightTop.cpy(), leftCorner.cpy(), leftTop.cpy());

        float colPercent = percentOnLine(rowProjection, topCorner.cpy(), leftCorner.cpy());
        float rowPercent = percentOnLine(columnProjection, topCorner.cpy(), rightCorner.cpy());

        if( colPercent <0.0f || colPercent > 1.0f || rowPercent <0.0f || rowPercent > 1.0f ) {
            return null;
        }
        int column = (int)(colPercent/rowTileUnit);
        if (column >= mapWidth) column = mapWidth-1; // cap at grid limit

        int row = (int)(rowPercent/columnTileUnit);
        if (row >= mapHeight) row = mapHeight-1;

        return new Vector2D(row, column);
    }

    /**
     * Find the intersection between two lines.
     *
     * @param pointVector1 The point vector of the first line
     * @param directionVector1 The direction vector of the first line
     * @param pointVector2 The point vector of the second line
     * @param directionVector2 The direction vector of the second line
     * @return The coordinates of the intersection, or null, if non-existent
     */
    public Vector2 lineIntersection(
        Vector2 pointVector1, Vector2 directionVector1, Vector2 pointVector2, Vector2 directionVector2
    ) {
        float det = directionVector1.x * (-directionVector2.y) - directionVector1.y * (-directionVector2.x);

        if (Math.abs(det) < 1e-10) return null;

        float dx = pointVector2.x - pointVector1.x;
        float dy = pointVector2.y - pointVector1.y;

        float lambda = (dx * (-directionVector2.y) - dy * (-directionVector2.x)) / det;

        return new Vector2(pointVector1.x + lambda * directionVector1.x, pointVector1.y + lambda * directionVector1.y);
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

        return percentage;
        //return Math.max(0f, Math.min(1f, percentage));
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
