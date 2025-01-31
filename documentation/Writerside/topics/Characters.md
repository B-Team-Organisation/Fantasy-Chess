# Character Entity

The character entities are core components of the game, representing active instances of characters that players control. These entities integrate with the game's logic and maintain their dynamic state throughout the game lifecycle.

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

## Importance of Character Entity in the Game
The `CharacterEntity` serves as the link between the static character definitions and the in-game logic. It ensures that character behavior remains consistent while allowing flexibility in combat, movement, and player interactions.

By maintaining structured entity states, the game supports engaging and tactical gameplay, enabling expansions such as inventory systems, experience points, and status effects in future updates.


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

The `CharacterDataModel` acts as a blueprint for `CharacterEntity` creation. When a new `CharacterEntity` is instantiated, it references the `CharacterDataModel` to determine its initial attributes. However, the entity itself evolves based on player actions, while the data model remains a static reference.

# Character Lifecycle

## Character Definition and Instantiation

- ###  Character Definition
Before creating a `CharacterEntity`, a `CharacterDataModel` must first be defined. This step involves setting static attributes such as health, attack patterns, movement patterns, and descriptions. The `CharacterDataModel` serves as the foundation for the entity.

- ### Character Instantiation
Once the `CharacterDataModel` is defined, a `CharacterEntity` can be instantiated. This process involves assigning a unique ID, initializing the character’s current health and position, and associating it with a player.


## State Updates

The `CharacterEntity` updates dynamically during the game.

- Position and health values change in response to game actions.
- Movement and attack actions interact with the `GridModel` and `Turn Logic Service`.

### Combat and Damage Handling
- Characters take damage based on attack interactions.
- When health reaches zero, the character is removed from the game grid.

### Destruction and Removal
- Characters that die are removed from active gameplay but may remain in logs .


