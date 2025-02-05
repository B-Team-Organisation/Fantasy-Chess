# Turn Outcome Animation

`Author: Lukas Walker`

The turn outcome is animated using the `TurnResultAnimationHandler`. 
As soon as the client receives the [Turn Result](Turn-Logic.md), a new instance of the `TurnResultAnimationHandler` is
created to animate the results for the player.

All results that are displayed using animations are converted to their related animation objects.

When the animation is started, the queue is processed to show collisions, movements and attacks until all animations
have been played. This information can be retrieved to again let the player plan commands and the game to continue.

For implementation details, please check out the [JavaDoc](https://b-team-organisation.github.io/Fantasy-Chess/java-docs/client/core/com/bteam/fantasychess_client/graphics/package-summary.html)

## Abstract Animation

All animation classes inherit from the abstract class [AbstractAnimation](https://b-team-organisation.github.io/Fantasy-Chess/java-docs/client/core/com/bteam/fantasychess_client/graphics/AbstractAnimation.html).
This class holds a `TiledMapTileLayer` for all animations that require map effects (like attacks) and outlines two
abstract methods:
- `isAnimationOver` for checking the status of the specific animation
- `startAnimation` for starting the animation

The `TiledMapTileLayer` is given the object when its animation is initialized in order for it to always be a fresh
and displayed version of the layer.

So far, there are three classes inheriting this class:

````plantuml
@startuml

class TurnResultAnimationHandler {
    + isDoneWithAnimation(): boolean
    + progressAnimation(): void
    + startAnimation(): void
}

class AbstractAnimation {
    + isAnimationOver(): boolean
    + startAnimation(): void
}

class AttackAnimation {
    + isAnimationOver(): boolean
    + startAnimation(): void
}

class CollisionAnimation {
    + isAnimationOver(): boolean
    + startAnimation(): void
}

class MovementAnimation {
    + isAnimationOver(): boolean
    + startAnimation(): void
}

TurnResultAnimationHandler --> AbstractAnimation : uses >
AbstractAnimation <|-- AttackAnimation
AbstractAnimation <|-- CollisionAnimation
AbstractAnimation <|-- MovementAnimation

@enduml

````

This diagramm shows their relation in a heavily simplified manner.

### Movement Animation

The Movement Animation is responsible for animating movements of characters on the map. It holds a [](CharacterSprite.md)
that gets prompted to move to the target location when the animation is started.

### Attack Animation

The Attack Animation is responsible for animating attacks of characters on the map. It holds a [](CharacterSprite.md)
that gets prompted to move to the target location and back to its original position when the animation is started.
During this movement, the attacked area on the map is highlighted in red using the `TiledMapTileLayer`.

Attacks targeting the position the character is standing on directly are treated different, because the normal method
would result in a animation too short to process. For this reason, these attacks lead to a series of small movements
from left to right.

### Collision Animation

Collisions work like movements but move two [](CharacterSprite.md) to the target location instead of one.

All animations are over when the sprites stopped moving.

