# Patterns

Patterns are used to define the valid target positions and effected areas of both movement and attack commands. 
They are defined using [Pattern Models](PatternModel.md).

# How to create a pattern

>In the following, the pattern `String` will be referred to as the "pattern", the amount of lines in it will be the called "height" and
the length of each line will be called "width"

1. Calcualte the required dimensions of the pattern

The size of the pattern is calculated using the maximum distance on the x or y axis between the center of the pattern
and any valid position in the pattern multiplied by 2 plus 1.
If the pattern lets the character target positions that are at most 3 tiles away from the characters position,
the size would have to be 7 = 2 * 3 + 1.

>The pattern has to have equal width and height, both being the same uneven number. This is required because it
automatically creates a central tile making it easy to with equal space in all directions for the
character to be located at.
{style=warning}

2. Create pattern `String`

The pattern itself is a `String` with `\n` to separate the lines.
In our example from above it would look like this (Here used underscores to visualize the blank `chars`):

```
"_______\n
 _______\n
 _______\n
 _______\n
 _______\n
 _______\n
 _______"
```

3. Populate the `String` 

Using any `chars` you want, mark the valid positions of the pattern.
The characters don't have to match but doing so helps in the next step.
Again the example:

```
"__xxx__\n
 _x___x_\n
 x_____x\n
 x_____x\n
 x_____x\n
 _x___x_\n
 __xxx__"
```

This pattern has valid positions in a circle around its center.

4. Define subpatterns

This step is only necessary for area of effect attacks.
If you want to attack an entire area, simply add a subpattern that maps from a `char` in the pattern to the subpattern
you want to apply at that position.
All commands targeted at positions with subpatterns will instead hit all valid positions of the subpattern.
This even works with sub-subpatterns and so forth.

>Watch out for loops in subpatterns when constructing new patterns.
{style=warning}

