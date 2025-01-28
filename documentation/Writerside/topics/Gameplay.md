# Gameplay

Gameplay revolves around players making choices about what each of their Characters does every turn, then reveal all
commands bundled together and executing them all at the same time. First [movements](#movement-commands) are handled and 
afterward, all [attacks](#attack-commands) are processed.

## Commands

### Movement Commands
An attack command defines which space a character is moving the next turn. Its movement is restricted by its Movement
Pattern

### Attack Commands:
An Attack command revolves around setting a location for the attack a character can make. On this location the Character
will attack within the current turn. Depending on the characters ability he will either attack multiple tiles around the
selected tile, or just the selected tile, where a character's attack is defined by its [Attack Pattern](Patterns.md).

