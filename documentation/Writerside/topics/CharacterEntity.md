# Character Entity

`Author: Lukas Walker`

The Character Entity represents an active instance of a character in the game. 
These entities are created by the server when the game is created and distributed
to the clients.

The server acts as the single source of truth about the Character Entities and its
versions are always to be preferred.

Each Character Entity consists contains the following data:

- Its [](CharacterDataModel.md)
- Its unique ID as a String
- Its current health
- Its position on the grid (in [grid coordinates](CoordinateSystemConversions.md))
- The playerID of the player it belongs to

The health and position are updates dynamically in order to represent and track the current state of the game.

## Lifecycle

1. Creation in the server
2. Initial transfer to both clients
3. Queued for information by the client
4. Used for turn logic in the server
5. Transfer to clients to keep the clients up to date with the ground truth 

Take a look at the [javadoc](https://b-team-organisation.github.io/Fantasy-Chess/java-docs/common/)
for more information.