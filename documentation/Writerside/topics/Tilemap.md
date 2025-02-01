# Tilemap


### Coordinate System Conversions

##### There are three coordinate systems:

<deflist type="medium">
    <def title="World Coordinates">
        The x and y coordinates on the client's screen.
    </def>
    <def title="Grid Coordinates">
        Coordinates on the tile map, represented by rows and columns.
    </def>
    <def title="Tile Coordinates">
        The internal coordinates used by `gdx.maps.tiled`, 
        which are the same as grid but with y inverted.
    </def>
</deflist>

> Tile coordinates differ from the grid coordinates we use, because of technical debt. They could
> be merged, but we found it more intuitive to have (0, 0) at the top, rather than on the left.
 
![Coordinate Systems illustration](./../img/client/CoordinateMaps.png)


Because we are using isometric tile maps, the position of the mouse (world coordinates) can not be easily converted 
to a specific tile on the grid (grid coordinates). The `TileMathService` provides a way to calculate this, independent
of any screen sizes and mostly of the map format. It provides methods to go from screen to grid and the other way around.

To create a `TileMathService` instance, you need the `TiledMap` and the width and height of both the map and an 
individual tile. <br />

You can use the `gridToWorld` method to convert row and column numbers to x and y world coordinates
of the middle of a tile. This is particularly useful for placing characters. <br />
The `worldToGrid` method is primarily used 
to convert the user input to a position on the map by taking in said x and y coordinates to convert to grid column 
and row positions. <br /> 
Lastly, there's a `gridToTiled` method which will convert grid coordinates to tiled map 
coordinates and is used before interactions with `gdx.maps.tiled`.

<procedure title="Grid to World Algorithm" id="gridToWorld">
    <p>(As shown in the example figure below)</p>
    <step>Use the edges to create line in parametric form on the column edges.</step>
    <step>Using percentages of the tile to width, project the point onto the column lines.</step>
    <step>Use those two points for a new line to calculate the percentages on the lines.</step>
    <step>Use linear interpolation on your line to go from edge coordinates to point coordinates.</step>
</procedure>

![Grid to World illustration](./../img/client/gridToWorld.png)

<procedure title="World to Grid Algorithm" id="worldToGrid">
    <p>(As shown in the example figure below)</p>
    <step>Take two lines from left to top and right to top edges.</step>
    <step>Using the directional vector of one line, project the point onto the other.</step>
    <step>Use both projected points on the lines to calculate the percentages.</step>
    <step>Round the percentages to the respective column/row number.</step>
</procedure>

![World to Grid illustration](./../img/client/worldToGrid.png)

<procedure title="Grid to Tiled Algorithm" id="gridToTiled">
    <p>Inverts the y coordinate.</p>
</procedure>




