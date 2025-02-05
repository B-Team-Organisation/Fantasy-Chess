# Tile Model

`Author: Lukas Walker`

A tile represents a single position on the [](GridModel.md) and is the fundamental
building block of the [](GridModel.md).

It can store a [](CharacterEntity.md) and the information if its a valid start tile, which is important
for the [Initialisation Phase](GameScreen.md#initialisation-phase) of the game.

```Java
private CharacterEntity character = null;
private boolean isStartTile = false;
```

All fiends in the TileModel are read/write-able using getters/setters.

For implementation details, please check out the [JavaDoc](https://b-team-organisation.github.io/Fantasy-Chess/java-docs/common/com/bteam/common/models/TileModel.html)