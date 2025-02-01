# Character Data Model

It defines all static properties such as attack and movement patterns, base stats, and descriptions.
Unlike [](CharacterEntity.md), which changes dynamically during the game,
`CharacterDataModel` remains constant and serves as a template for creating new character entities.

### Structure of CharacterDataModel
📌 **For a full technical reference, check the**
[CharacterDataModel JavaDoc](../../../common/src/main/java/com/bteam/common/models/CharacterDataModel.java).

### Key Attributes of CharacterDataModel

- **Name (`String`)**: The name of the character type.
- **Description (`String`)**: A textual representation of the character’s background or abilities.
- **Health (`int`)**: The base health assigned to this character type.
- **Attack Power (`int`)**: The damage potential of this character.
- **Attack Patterns (`PatternService[]`)**: Defines how the character attacks.
- **Movement Patterns (`PatternService[]`)**: Defines how the character moves.
- **Attack Description (`String`)**: Additional explanation of the attack patterns.
- **Movement Description (`String`)**: Additional explanation of the movement patterns.