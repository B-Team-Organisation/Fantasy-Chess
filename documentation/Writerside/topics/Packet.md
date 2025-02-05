# Packets

`Author: Marc Matija`

Packets take the form of JSON data and are defined in the code as a Java class, which takes in a [JsonDTO](JsonDTOs.md).
Multiple packets can have the same DTO, however each Type of packet gets an ID, which is unique to that type of packet.

```json
{
  "id": "<PACKETID>",
  "data" : {}
}
```

```java
public class Packet 
{
    JsonDTO data;
    String id;

    public Packet() {
        data = null;
        id = "";
    }

    public Packet(JsonDTO data, String id) {
        this.data = data;
        this.id = id;
    }

    public JsonDTO getData() {
        return data;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return new JsonWriter().writeKeyValue("id", id)
            .and().writeKeyValue("data", data).toString();
    }
}
```
{collapsible="true"}

## Connection Packets

### CONNECTED_STATUS

Sends the connected status

**DTO:** [](JsonDTOs.md#statusdto)
**Sent by:** Server

## Lobby Packets
Lobby Packets contain relevant information to actions on or with lobbies.

### LOBBY_CLOSE

Indicates that a lobby should be closed

**DTO:** [](JsonDTOs.md#lobbydto)  
**Sent by:** Client

### LOBBY_CLOSED

Indicates that a lobby has been closed

**DTO:** [](JsonDTOs.md#lobbycloseddto)  
**Sent by:** Server

### LOBBY_INFO

Get detailed information about a single lobby

**DTO:** [](JsonDTOs.md#lobbydto)  
**Sent by:** Server & client

### LOBBY_JOIN

Request to join a lobby.

**DTO:** [](JsonDTOs.md#joinlobbydto)  
**Sent by:** Client

### LOBBY_JOINED

Response to [](#lobby-join) to indicate that a lobby was either successfully joined or not.

**DTO:** [](JsonDTOs.md#joinlobbyresultdto)  
**Sent by:** Server

### LOBBY_CREATE

Request a new lobby.

**DTO:** [](JsonDTOs.md#createlobbydto)  
**Sent by:** Client

### LOBBY_CREATED

Response to [](#lobby-create) to indicate that a lobby has been created.

**DTO:** [](JsonDTOs.md#lobbydto)  
**Sent by:**  Server

### LOBBY_ALL

Used by the client to request all currently active lobbies.

**DTO:** [](JsonDTOs.md#lobbylistdto)  
**Sent by:** Client


## Player Packets

### PLAYER_READY

Used to identify the current player's status.  
Should there not be an id, it is to be assumed that the status belongs to the sending client.

**DTO:** [](JsonDTOs.md#playerstatusdto)  
**Sent by:** Client and Server

### PLAYER_JOINED

Used to notify that a player has abandoned the lobby.

**DTO:** [](JsonDTOs.md#playerinfodto)  
**Sent by:** Server

### PLAYER_ABANDONED

Used to notify that a player has abandoned the current game.

**DTO:** [](JsonDTOs.md#playerstatusdto)  
**Sent by:**

### PLAYER_INFO

Used to retrieve basic information about a player.  
Should the username field be empty, it is to be assumed that the client requests the missing information.

**DTO:** [](JsonDTOs.md#playerinfodto)  
**Sent by:** Client and Server

## Game Packets

### GAME_COMMANDS

Contains a list of all commands a player has set.

**DTO:** [](JsonDTOs.md#commandlistdto)  
**Sent by:** Client

### GAME_INIT

Contains character entities and their initial state, as well as the created Game Id.

**DTO:** [](JsonDTOs.md#gameinitdto)  
**Sent by:** Server

### GAME_TURN_RESULT

Contains all information about the result of the current turn, including the state of characters, 
commands that conflicted and if there was a winner or not.

**DTO:** [](JsonDTOs.md#turnresultdto)  
**Sent by:** Server
