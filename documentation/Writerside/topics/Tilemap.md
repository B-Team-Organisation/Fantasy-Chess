# Tilemap

Start typing here...


#### Mouse to grid conversion

Because we are using isometric tile maps, the position of the mouse can not be easily converted 
to a specific tile on the grid. The `TileMathService` provides a way to calculate this, independent
of any screen sizes and mostly of the map format. <!--TODO: Calculate the stretching value 2 so it really
is independent of shape--> It provides a method to go from screen to grid and one from grid to screen,
respectively.

<procedure title="Basic idea" id="TileMathService">
    <step>Take the lines of the edges of the grid by using their corners</step>
    <step>Use their normal vector to get the equivalent of the mouse position on each edge</step>
    <step>Given the size of a tile, calculate where the mouse is</step>
    <step></step>
</procedure>
