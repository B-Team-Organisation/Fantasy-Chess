# JsonDTOs

JsonDTOs (`Data Transfer Object`) are classes, which implement the JsonDTO interface, which Identifies that a certain
class can be serialized to JSON. This process needs to be done manually using the custom [`Json Writer`](Json-Writer.md)
provided in the [common](Common.md) package.

### CharacterEntityDTO
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
```json
{
  "characterId":"[COMMANDED CHARACTER ID]",
  "x" : 0,
  "y" : 0,
  "commandType" : "[MOVEMENT | ATTACK]"
}
```


### CommandListDTO

**Json:**
```json
{
  "commands":[{CommandDTO},...],
  "gameId" : "[UUID OF BELONGING GAME]"
}
```


### CreateLobbyDTO

**Json:**
```json
{
  "lobbyName" : "[LOBBYNAME]"
}
```


### GameInitDTO

```json
{
  "characters" : [{CharacterEntityDTO},...],
  "gameId" : "[INITIALIZED GAME ID]"
}
```


### JoinLobbyDTO

```json
{
  "id" : "[ID OF LOBBY TO JOIN]"
}
```

### JoinLobbyResultDTO

```json
{
  "result" : "success" | "error" 
}
```

### LobbyClosedDTO

```json
{
  "id" : "[UUID OF CLOSED LOBBY]",
  "reason": "[TEXT EXPLANATION OF CLOSING REASON]"
}
```

### LobbyDTO

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

```json
{
  "lobbies" : [{LobbyDTO}]
}
```

### PatternDTO

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

```json
{
  "lobbies" : [{PatternDTO}]
}
```

### PlayerInfoDTO

```json
{
  "username" : "[USERNAME]",
  "playerId" : "[PLAYER ID]"
}
```

### PlayerStatusDTO

```json
{
  "status" : "playerReady" | "playerNotReady" | "playerAbandon",
  "clientId" : "[PLAYER ID]"
}
```

### StatusDTO

```json
{
  "status" : "connected" // status of connection
}
```

### TokenDTO

```json
{
  // 14 Character base64 URL string with 2 digit CRC8
  "token" : "V3D5qNVl8IpP2y_qEgQ24", 
  "expires" : 0 // unix epoch milliseconds
}
```

### TurnResultDTO

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
  "winner": "[WINNDER UUID]" | null
}
```