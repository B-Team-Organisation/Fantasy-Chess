# Services

`Author: Marc Matija`

All services are SpringBoot services classes, which are Singletons within the Server context and can automatically
be referenced in fields using dependency Injection. They are the backbone of the server and form the link between
incoming requests and the database.

Creating a service
:
```java
@Service
public class ExampleService{
    // Add one or more repositories to this service
    private final ExampleRepository exampleRepository;
    /* ... */
    public ExampleService( 
        // Springboot automatically wires the created singleton
        // into the constructor witht the Autowired Keyword
        @Autowired ExampleRepository exampleRepository;
    ){
        //Inject the Repository Dependency into the service
        this.exampleRepository = exampleRepository;    
    }
}
```

## Game State Service

Handling all data regarding the game's current state, the Game State Service is arguably the most important of the
services. It incorporates the [](Turn-Logic-Service.md) from the common package. It gathers all commands the player
send and then forwards them to the said service. It also rotates the coordinates according to the players view
within their browser and sets up new games. Additionally, it provides the ability to query for specific games
or see all currently running games.

The most important methods include:

startNewGame
: Takes in a `GameSettingsModel` instance, the `lobbyId` and the `playerIds` and returns a new Game model as well 
as saving the newly created game to a hashmap for later reference.

processMoves
: Takes in a `gameId` along with all move and attack commands made for this turn and processes them, returning a 
`TurnResult` along with the current `GridModel`.

> For more information, please look at the Java Docs.

## Lobby Service

Used to manage active lobbies and routing players to the correct ones. It exposes basic CRUD operations for lobbies,
along with a way to join and leave a lobby. It does not rely on database connections and just saves all lobbies to 
a `HashMap`. This is done out of simplicity for now, but could be expanded on later to make this service stateless.
The HashMap saves instances of [](Models.md#lobby-model) to later be queried.

Some notable methods include:

joinLobby
: Takes in the uuid of a lobby and a player id and assigns him to a lobby, granted this doesn't fail.

leaveLobby
: Given the uuid of a lobby and the player to remove, it removes the player from the lobby and checks, if the player
was the host. If it was the host, or there are no more players within the lobby. it automatically closes the lobby.

closeLobby
: Given the uuid of a lobby and a reason, it closes the lobby and calls the WebSocketService to get the current
connected client for all remaining players and sends them a [](Packet.md#lobby-closed) packet.

## Player Service

The player service handles all data regarding a player's state and information, such as username and userid. It
communicates with the Database using the [](Repositories.md#player-repository) and exposes methods to get a player,
create a new one and set a player's status.

> This service should be expanded upon, once proper authentication has been implemented. For the current state of
> Authentication see [](Authentication.md).

## Token Service

The token service is used to generate a token for a given user and check its validity.

<procedure title="Token Generation" id="token-generation">
<step>
Generate a random 14 character <a href="https://base64.guru/standards/base64url">Base64URL</a> string
</step>
<step>
Generate the CRC8 checksum for the string and append it to the token.
</step>
<step>
Generate the expiry date of the token.
</step>
<step>
Assign the token to a player's UUID and add it, along with it's <code>expiration</code> into the database. 
</step>
<p>
In the end, the Token should be written to the database and have a structure, similar to this:<br /> 
<code>V3D5qNVl8IpP2y_qEgQ24</code>
</p>
</procedure>
<procedure title="Token Validation" id="token-validation">
    <step>
        Check token for it's CRC8 checksum. Should the CRC8 checksum of the first 14 characters not match with the
        provided checksum, then discard the token and stop the connection attempt.
    </step>
    <step>
        Should the checksum match, then it is safe to assume the token exits in the database, thus fetch the 
        <code>expiration</code> and the <code>playerId</code> from the <code>Token repository</code>
    </step>
    <step>
        Check if the token is expired and if so, stop the current connection attempt and delete the token from the.
    </step>
<step>
If all steps succeed, you can safely say, that the token is valid.
</step>
<warning>
Tokens are supposed to be used only once, so once a Token was found to be valid, it is to be invalidated right
after the required operations with the token have succeeded.
</warning>
</procedure>
<procedure title="Token Invalidation" id="token-invalidation">
    <p>To invalidate a token, simply delete it from the <code>TokenRepository</code></p>
</procedure>

## WebSocket Service

The Websocket Service is used for handling all active WebSocket connections and routing the incoming [](Packet.md)
to the correct [](Packet-Handler.md). It stores all currently connected clients within a `HashMap<String, Client>` for
quick access. Also housed within the WebSocket Service are the [](Packet-Handler.md), which get assigned to the
websocket service inside the constructor.

Registering a Packet Handler in the Websocket Service
:
```java
public WebSocketService(/* ... */) {
        /* ... */
        addPacketHandler(new ExampleHandler());
        /* ... */
    }
```

Some of the most important methods exposed by this service include:

`registerSession(WebSocketSession session, Player player)`
:
Given a WebSocket Session and a Player it will link them together and return a new `Client` instance which gets
saved to the hashmap. It also adds an OnClientDisconnected listener to the created client, as well as sending the
[](Packet.md#connected-status) packet to the client to indicate a successful connection.

`handleTextMessage(WebSocketSession session, TextMessage message)`
:
Handles incoming messages, figures out, which id they belong to and calls the proper handler given the id in the
packet. If the packet is invalid it prints the Stack Trace to the standard console.

>  For further information and Methods, please have a look at the [JavaDocs](https://fantasy-chess.github.io/javadocs/server/).