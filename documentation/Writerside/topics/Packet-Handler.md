# Packet Handler

Packet Handler process incoming WebSocket Packets and implement the `PacketHandler` interface. The `PacketHandler`
interface defines 2 methods, one for handling the incoming packet and one for it's `PacketPattern`, which is specific
for each Packet Handler and defines the types of packets sent to each handler. The `handle` method gets called by
the `WebSocketService` to process the packet.

## Game Packet Handler

The Game Packet Handler processes all Packets relevant to a running game. Which currently only consists of the:
[GAME_COMMANDS](Packet.md###GAME_COMMANDS) packet, which contains the current commands a player has set for their characters.

PacketPattern: `GAME_`

## Lobby Packet Handler

The Lobby Packet Handler processes all Packets relevant to lobbies, such as creating a new one 
([LOBBY_CREATE](Packet.md#LOBBY_CREATE)), joining a lobby ([LOBBY_JOIN](Packet.md#LOBBY_JOIN)), leaving a lobby 
([LOBBY_LEAVE](Packet.md#LOBBY_LEAVE)) and more that can be found [here](Packet.md##Lobby Packets).

PacketPattern: `LOBBY_`

## Player Packet Handler

The Player Packet handler processes all Packets relevant to player data, such as setting a Players Ready status 
([PLAYER_READY](Packet.md#PLAYER_READY)), as well as an abandoning Player ([PLAYER_ABANDONED](Packet.md#PLAYER_ABANDONED)) 
and current information about a Player ([PLAYER_INFO](Packet.md#PLAYER_INFO)).

PacketPattern: `PLAYER_`