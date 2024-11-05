# Screens:

## Splashscreen:

* Game Name
* Username Input
* Play Button (active, if username set)
* (Full screen button)
* (Credits, Build Version)

## Main Menu:

* Game Name
* Lobby list
  * Lobby entry
    * Lobby name
    * Lobby status (Open, full, running)
    * (Lobby timer)
    * (Password protected)
    * Is a lobby join button
* Refresh lobbies button
* Create lobby button 
  * Opens `Dialog`:
    * Lobby name -> Default:"{username}'s Lobby"
    * Create button -> Switches to game screen, lobby mode
    * (Draft Type)
    * (Map size)
    * (Map type)
    * (Password)
    * (Gamemode)
    * (Round limits)
    * Cancel button -> Closes the Dialog

## Game Screen (Lobby mode):

* Map rendered in background
* Lobby window in foreground
  * Player names
  * Player status (Ready, Not ready)
  * Start button
  * Lobby settings (Map, Limits)
  * Leave lobby button

## Game Screen (Play mode):

* Renders game in center
* Send moves button (name in progress)
* (Round timer)
* ...

## Game Screen (Summary mode):

* Result
* Back to menu
* (See [issue notes](https://github.com/B-Team-Organisation/Fantasy-Chess/issues/18))
* ...