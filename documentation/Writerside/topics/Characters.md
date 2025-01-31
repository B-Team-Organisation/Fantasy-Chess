# Character Entity 

All character entities are core components of the game, representing active instances of characters that players control. These entities integrate with the game's logic and maintain their dynamic state throughout the game lifecycle.

Creating a character entity:
```java
public class CharacterEntity {
    private final CharacterDataModel characterBaseModel;
    private final String id;
    private int health;
    private Vector2D position;
    private final String playerId;

    public CharacterEntity(CharacterDataModel characterBaseModel, String id, int health, Vector2D position, String playerId) {
        this.characterBaseModel = characterBaseModel;
        this.id = id;
        this.health = health;
        this.position = position;
        this.playerId = playerId;
    }
}
```

## Role of Character Entity

Handling all data regarding a character’s current state, the Character Entity is a crucial part of the game's logic. It acts as a bridge between the static `CharacterDataModel` and the active game state. The entity keeps track of in-game attributes, such as health, position, and player association.

The most important aspects include:

### Relationship with Character Data Model
Each `CharacterEntity` instance is directly linked to a `CharacterDataModel`, which acts as its blueprint. While the data model provides static properties such as movement and attack patterns, the entity dynamically maintains the character’s real-time status.

## Character Attributes

- **Character Base Model (`CharacterDataModel`)**: Defines static attributes, including attack and movement patterns, descriptions, and base health.
- **Unique ID (`String`)**: Identifies the character instance.
- **Current Health (`int`)**: Tracks health that decreases in combat and regenerates through healing effects.
- **Position (`Vector2D`)**: Stores the current position of the character on the game grid.
- **Player ID (`String`)**: Indicates the controlling player.

## Character Data Model

The `CharacterDataModel` represents the predefined attributes of a character. It defines all static properties such as attack and movement patterns, base stats, and descriptions. Unlike `CharacterEntity`, which changes dynamically during the game, `CharacterDataModel` remains constant and serves as a template for creating new character entities.

### Structure of CharacterDataModel

```java
public class CharacterDataModel {
    private String name;
    private String description;
    private int health;
    private int attackPower;
    private PatternService[] attackPatterns;
    private PatternService[] movementPatterns;
    private String attackDescription;
    private String movementDescription;

    public CharacterDataModel(String name, String description, int health, int attackPower, PatternService[] attackPatterns, PatternService[] movementPatterns, String attackDescription, String movementDescription) {
        this.name = name;
        this.description = description;
        this.health = health;
        this.attackPower = attackPower;
        this.attackPatterns = Arrays.copyOf(attackPatterns, attackPatterns.length);
        this.movementPatterns = Arrays.copyOf(movementPatterns, movementPatterns.length);
        this.attackDescription = attackDescription;
        this.movementDescription = movementDescription;
    }
}
```

### Key Attributes of CharacterDataModel

- **Name (`String`)**: The name of the character type.
- **Description (`String`)**: A textual representation of the character’s background or abilities.
- **Health (`int`)**: The base health assigned to this character type.
- **Attack Power (`int`)**: The damage potential of this character.
- **Attack Patterns (`PatternService[]`)**: Defines how the character attacks.
- **Movement Patterns (`PatternService[]`)**: Defines how the character moves.
- **Attack Description (`String`)**: Additional explanation of the attack patterns.
- **Movement Description (`String`)**: Additional explanation of the movement patterns.

### Relationship Between CharacterDataModel and CharacterEntity

The `CharacterDataModel` acts as a foundation for `CharacterEntity`. When a new character is instantiated, it references the `CharacterDataModel` to determine its initial attributes. However, the entity itself evolves based on player actions, while the data model remains a static reference.

## Character Lifecycle

### Creation
- A `CharacterEntity` is instantiated with a reference to its `CharacterDataModel`.
- It is assigned to a player and placed at a specific starting position.

### State Updates
- Position and health values update dynamically.
- Movement and attack actions interact with the `GridModel` and `Turn Logic Service`.

### Combat and Damage Handling
- Characters take damage based on attack interactions.
- When health reaches zero, the character is removed from the game grid.

### Destruction and Removal
- Characters that die are removed from active gameplay but may remain in logs .

## Key Methods

#### `getCharacterBaseModel()`
- **Returns**: `CharacterDataModel`
- **Description**: Retrieves the base model, providing reference to static attributes.

#### `getId()`
- **Returns**: `String`
- **Description**: Retrieves the unique identifier of the character.

#### `getHealth()` and `setHealth(int health)`
- **Returns**: `int`
- **Description**: Retrieves or updates the character’s health.

#### `getPosition()` and `setPosition(Vector2D position)`
- **Returns**: `Vector2D`
- **Description**: Retrieves or updates the character’s grid position.

#### `getPlayerId()`
- **Returns**: `String`
- **Description**: Retrieves the player ID controlling this character.

## Importance of Character Entity in the Game
The `CharacterEntity` serves as the link between the static character definitions and the in-game logic. It ensures that character behavior remains consistent while allowing flexibility in combat, movement, and player interactions.

By maintaining structured entity states, the game supports engaging and tactical gameplay, enabling expansions such as inventory systems, experience points, and status effects in future updates.

