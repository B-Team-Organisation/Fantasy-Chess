# Packets

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

## Lobby Packets
Lobby Packets contain relevant information to actions on or with lobbies.

## Game Packets

### GAME_COMMANDS

**DTO:** [CommandListDTO](JsonDTOs.md###CommandListDTO)
Contains all Commands a player has sent