# Client

`Author: Marc Matija`

The Client is a LibGDX Project which is compiled to JavaScript using GWT. This allows easy access for any user, but
brings its fair share of development issues. The client is restricted to Java 11, which in turn applies for the 
`common` module, as well as missing some other features such as UUID's, Reflection and other various features.
When writing code for the client, these issues need to be navigated around.

## Structure

The client consists of 2 submodules.

- **HTML** - handles the ability to render to the browser
- **Core** - contains the program code independent of platform

## Services

In order to manage Data, the client makes use of Services. The services are:

### WebSocketService

Manages the WebSocket connection to the server and handles packet routing.
More infos under [](Networking.md)

### Lobby Service

The Lobby service manages the active lobby the user is part of and the players within it.

### Client Game State Service

Manages the current state of the game on the client.
