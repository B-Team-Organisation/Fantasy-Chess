# Packet Handler

Packet Handler process incoming WebSocket Packets and implement the `PacketHandler` interface. The `PacketHandler`
interface defines 2 methods, one for handling the incoming packet and one for it's `PacketPattern`, which is specific
for each Packet Handler and defines the types of packets sent to each handler. The `handle` method gets called by
the `WebSocketService` to process the packet.

## Game Packet Handler

The Game Packet Handler processes all Packets relevant to a running game. Which currently only consists of the:
[](Packet.md#game-commands) packet, which contains the current commands a player has set for their characters.

> A list of all game packets can be found [here](Packet.md#game-packets).


PacketPattern: `GAME_`

## Lobby Packet Handler

The Lobby Packet Handler processes all Packets relevant to lobbies, such as creating a new one 
([](Packet.md#lobby-create)), joining a lobby ([](Packet.md#lobby-join)), closing a lobby 
([](Packet.md#lobby-close)) and more.

> A list of all lobby packets can be found [here](Packet.md#lobby-packets).


PacketPattern: `LOBBY_`

## Player Packet Handler

The Player Packet handler processes all Packets relevant to player data, such as setting a Players Ready status 
([](Packet.md#player-ready)), as well as an abandoning Player ([](Packet.md#player-abandoned)) 
and current information about a Player ([](Packet.md#player-info)).

> A list of all player packets can be found [here](Packet.md#player-packets).

PacketPattern: `PLAYER_`