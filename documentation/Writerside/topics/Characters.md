# Characters

`Author: Lukas Walker`

Characters are the pieces players move on the board. Each character has:
- A **name** and **description**
- **Base stats** for health and damage
- **Attack and movement patterns**, each with a corresponding description

Characters are defined in the [](CharacterDataModel.md) and instantiated using a [](CharacterEntity.md).

## Creating a new Character

Before adding a new character, keep in mind that it must be **balanced** with existing characters to 
ensure a fun and fair game.

To add a new character to the game, you need to follow three steps:

1. **Character Definition** – Add the character to the [](CharacterStore.md).
2. **Graphics** – Provide two images:
    - **Ally version** (`-back` suffix)
    - **Enemy version** (`-front` suffix)
3. **File Placement** – Save the images in:  
   ```
   fantasychess-client/assets/tileset/characters/[character-name]
   ```


### Image Guidelines
- Max size: **24×20 pixels** (smaller is recommended to minimize occlusion).

Once these steps are complete and the **texture packer** has run, the character will be available in the game.