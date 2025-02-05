# Main Menu

`Author: Lukas Walker`

The main menu is the second screen of the game and provides the lobbies to the user. The user navigates
to it by connecting to the server on the [](SplashScreen.md).

It has several important ui components:

## Lobby List

The Lobby List contains all lobbies retrieved from the server in alphabetical order. It also displays the
state of the lobbies (OPEN, FULL).

While the Lobby List is filled with the lobbies present while entering this screen, it can be
refreshed using the [](MainMenu.md#refresh-button).
Additionally, it can be searched using the [](MainMenu.md#search-bar)

By clicking on a lobby, the client communicated its intention to join the lobby. For this, a `LOBBY_JOIN` package is
send to the server. The response in form of a `LOBBY_JOINED` package holds the result of this request, either sending
the user to the lobby menu in the [](GameScreen.md) or not responding at all. In that case, the lobbies should be
refreshed.

## Refresh Button

The Refresh Button prompts the server to again send the list of lobbies by sending a `LOBBY_ALL` package.
By adding a [](Networking.md#packet-handler), the incoming `LOBBY_INFO` packet containing the lobbys can be processed.

## Search Bar

The search bar allows filtering the lobbys in the [](MainMenu.md#lobby-list). It utilizes the levenshtein distance
between the searched name and the lobby names to provide reliable results.

## Create Lobby Button

This button opens a dialog for lobby creation. The default lobby name always is the username + "'s lobby".
When attempting to create a lobby, the client sends out a `LOBBY_CREATE` package.
The server answers with a `LOBBY_CREATED` package and sends the client to the [](GameScreen.md).