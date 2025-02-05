# Character Entities

The `CharacterEntity` represents an active instance of a character in the game. These entities
are dynamically instantiated during gameplay, integrating with the game's logic to maintain their
state throughout the match lifecycle.

## Structure of CharacterEntity
📌 **For a full technical reference, check the** 
[CharacterDataModel JavaDoc](https://b-team-organisation.github.io/Fantasy-Chess/java-docs/common/com/bteam/common/entities/CharacterEntity.html).

## Key Attributes (Definition List)

- **Character Base Model ([](CharacterDataModel.md))**: Provides the static attributes,
  including attack and movement patterns, descriptions, and base health.
- **Unique ID (`String`)**: A unique identifier for each instance, assigned by the server.
- **Current Health (`int`)**: Tracks the character's remaining health.
- **Position (`Vector2D`)**: The character's current position on the game grid.
- **Player ID (`String`)**: Identifies the player controlling this character.

## Role of Character Entity

The `CharacterEntity` bridges the gap between the static [](CharacterDataModel.md) and the game's dynamic logic.
It evolves throughout gameplay while maintaining a reference to its original data model.

Key responsibilities:

- Tracks health, position, and player association.
- Interacts with the game grid and logic services for combat and movement.
- Supports expansions like inventory systems, experience points, and status effects.

## Character Lifecycle

### Definition and Instantiation

#### Character Definition
The [](CharacterDataModel.md) is defined in the Commons-module and provides the foundation for creating
new `CharacterEntity` instances. It sets static attributes such as health,
attack patterns, movement patterns, and descriptions.

#### Character Instantiation (Server-Side)
The server creates a new `CharacterEntity` by referencing the [](CharacterDataModel.md):

- Assigns a unique ID.
- Initializes the character’s health and position.
- Associates the entity with a controlling player.

The instantiated entity is then sent to the client, ensuring synchronized gameplay.

#### Instantiation Example
A `CharacterEntity` is created dynamically on the server:

```java
CharacterEntity character = new CharacterEntity(
    characterModel,   // The predefined CharacterDataModel
    uniqueId,         // Server-assigned unique ID
    initialHealth,    // Starting health value
    startPosition,    // Initial position on the grid
    playerId          // Player ID controlling this character
);
```

### State Updates

The `CharacterEntity` updates dynamically during the game.

- Position and health values change in response to [](Turn-Logic.md) that elaborates game actions.
- Movement and attack actions interact with the `GridModel`.

### Combat and Damage Handling
- Characters take damage based on attack interactions.
- When health reaches zero, the character is removed from the game grid.





