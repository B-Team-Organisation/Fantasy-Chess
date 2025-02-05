# Character Data Model

`Author: Albano Vukelaj`

It defines all static properties such as attack and movement patterns, base stats, and descriptions.
Unlike [](CharacterEntity.md), which changes dynamically during the game,
`CharacterDataModel` remains constant and serves as a template for creating new character entities.

### Structure of CharacterDataModel
📌 **For a full technical reference, check the**
[CharacterDataModel JavaDoc](https://b-team-organisation.github.io/Fantasy-Chess/java-docs/common/com/bteam/common/models/CharacterDataModel.html).

### **Key Attributes** (Quick Overview)
- **Name & Description**: Basic character information.
- **Health & Attack Power**: Base stats determining survivability and damage.
- **Attack & Movement Patterns**: Defines how the character moves and attacks.
- **Pattern Descriptions**: Additional explanations for attacks and movements.  
