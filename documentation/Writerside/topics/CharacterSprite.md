# Character Sprite

`Author: Lukas Walker`

The Character Sprite is the graphical representation of the [](CharacterEntity.md). It inherits from the Sprite class
from LibGDX, extending its capabilities for the purposes of this game.

Each Character Sprite holds a reference to the [](CharacterEntity.md) it represents and itself can be found using the
`spriteMapper` found in the [](GameScreen.md). This allows for the rendered version of the character to be positioned
and moved independently for movement animations.

It also capsules all values necessary for rendering and animation the sprite as explained later.

For implementation details, please check out the [JavaDoc](https://b-team-organisation.github.io/Fantasy-Chess/java-docs/client/core/com/bteam/fantasychess_client/graphics/CharacterSprite.html)


## Rendering

The Sprite class holds a textureRegion from the `atlas` to be rendered at its position.
For this, the default rendering method of the Sprite class is overwritten to include our rendering offset:

Rendering offsets:
`````java
private final float xOffset;
private final float yOffset;

@Override
public void draw (Batch batch) {
    batch.draw(this, getX() - xOffset, getY() - yOffset);
}
`````

The Character Sprite can also be drawn at any other position on the grid. This is used for preview purposes.

## Animation

Each Character Sprite has a movement queue to move between positions.

`````java
private ArrayDeque<Vector2> destinations;
`````

This queue can be filled using two methods: `moveToGridPos` and `moveToWorldPos`.
The calculations between the two are made using the `mathService`.

When there are destinations in the queue, the `update` method orchestrates movements until all 
queues destinations have been visited with the Sprite ending up at the last destination. This allows complex movements all initiated at the same time.

The status of this animation can be checked using the `isInAnimation` method.