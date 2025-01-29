# JsonDTOs

JsonDTOs (`Data Transfer Object`) are classes, which implement the JsonDTO interface, which Identifies that a certain
class can be serialized to JSON. This process needs to be done manually using the custom [`Json Writer`](Json-Writer.md)
provided in the [common](Common.md) package.

### CharacterEntityDTO
Represents an in-game character entity with its model, health, owner, position, and unique identifier.
```json
{
  "modelId":"[CHARACTER MODEL NAME]",
  "health": 0,
  "playerId" : "[OWNED PLAYER ID]",
  "id" : "[ENTITY UUID]",
  "x" : 0,
  "y" : 0
}
```

### CommandDTO
Transmits a command issued to a character, including movement or attack actions and target coordinates.
```json
{
  "characterId":"[COMMANDED CHARACTER ID]",
  "x" : 0,
  "y" : 0,
  "commandType" : "[MOVEMENT | ATTACK]"
}
```

### CommandListDTO
Contains a list of commands for a game session, along with the game’s unique identifier.
**Json:**
```json
{
  "commands":[{CommandDTO},...],
  "gameId" : "[UUID OF BELONGING GAME]"
}
```

### CreateLobbyDTO
Carries data to request the creation of a new game lobby.
```json
{
  "lobbyName" : "[LOBBYNAME]"
}
```

### GameInitDTO
Contains initial game data, including a list of character entities and the unique game identifier.
```json
{
  "characters" : [{CharacterEntityDTO},...],
  "gameId" : "[INITIALIZED GAME ID]"
}
```

### JoinLobbyDTO
Carries data to request joining an existing lobby by its unique identifier.
```json
{
  "id" : "[ID OF LOBBY TO JOIN]"
}
```

### JoinLobbyResultDTO
Transmits the result of a lobby join request, indicating success or error.
```json
{
  "result" : "success" | "error" 
}
```

### LobbyClosedDTO
Contains information about a closed lobby, including its identifier and the reason for closure.
```json
{
  "id" : "[UUID OF CLOSED LOBBY]",
  "reason": "[TEXT EXPLANATION OF CLOSING REASON]"
}
```

### LobbyDTO
Represents a game lobby, containing its metadata such as name, players, host, and state.
```json
{
  "lobbyId" :  "[UUID OF LOBBY]",
  "name" : "[LOBBY NAME]",
  "maxPlayers" :  2,
  "playerIds" : ["[PLAYER UUID]", "[PLAYER UUID]"],
  "hostId" :  "[HOST UUID]",
  "gameState" : "CLOSED" | "OPEN" | "FULL" | "RUNNING"
}
```

### LobbyListDTO
Transmits a list of available lobbies.
```json
{
  "lobbies" : [{LobbyDTO}]
}
```

### PatternDTO
Represents a pattern with its name, structure, and mappings of subpatterns.
```json
{
  "patternName" : "[PATTERN NAME]",
  "patternString" : "[PATTERN STRING]",
  "subpatternMappings": {
    "[SUBPATTERN CHAR]" : "[PATTERN NAME]"
  }
}
```

### PatternListDTO
Transmits a list of pattern definitions.
```json
{
  "lobbies" : [{PatternDTO}]
}
```

### PlayerInfoDTO
Contains basic player information, including their username and unique identifier.
```json
{
  "username" : "[USERNAME]",
  "playerId" : "[PLAYER ID]"
}
```

### PlayerStatusDTO
Transmits a player’s current status in the game or lobby.
```json
{
  "status" : "playerReady" | "playerNotReady" | "playerAbandon",
  "clientId" : "[PLAYER ID]"
}
```

### StatusDTO
Provides a simple status update about the client’s connection state.
```json
{
  "status" : "connected" // status of connection
}
```

### TokenDTO
Transmits an authentication token and its expiration timestamp.
```json
{
  // 14 Character base64 URL string with 2 digit CRC8
  "token" : "V3D5qNVl8IpP2y_qEgQ24", 
  "expires" : 0 // unix epoch milliseconds
}
```

### TurnResultDTO
Contains the results of a game turn, including updated characters, conflict resolution, and the winner.
```json
{
  "updatedCharacters": [{CharacterEntityDTO}]
  "conflictCommands":
  [
    {
      "first" :  {CommandDTO} ,
      "second" :  {CommandDTO}
    }
  ],
  "validCommands":  [{CommandDTO}],
  "winner": "[WINNER UUID]" | null
}
```