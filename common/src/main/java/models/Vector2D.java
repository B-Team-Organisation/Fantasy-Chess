package main.java.models;

import java.util.Objects;

/**
 * Represents a 2D vector used primarily for movement and positioning.
 * Contains calculation methods like addition and subtraction.
 */
public class Vector2D {
    private final int x;
    private final int y;

    /**
    * Constructs a new movement vector from x and y coordinates.
    *
    * @param x the x-coordinate of the vector
    * @param y the y-coordinate of the vector
     */
    public Vector2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Adds the two vectors to get a new one with added coordinates.
     *
     * @param vector - the vector to add
     * @return a new vector with added coordinates
     */
    public Vector2D add(Vector2D vector) {
        return new Vector2D(this.x + vector.getX(), this.y + vector.getY());
    }

    /**
     * Subtracts two vectors to get a new one with subtracted coordinates.
     *
     * @param vector - the vector to subtract
     * @return a new vector with the subtracted coordinates
     */
    public Vector2D subtract(Vector2D vector) {
        return new Vector2D(this.x - vector.getX(), this.y - vector.getY());
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    @Override
    public String toString() {
        return "x: " + this.x + ", y: " + this.y;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Vector2D && x == ((Vector2D) o).x && y == ((Vector2D) o).y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}