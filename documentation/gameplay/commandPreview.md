# Command Preview Indicator Concept

## Overview

The Command Preview Indicator is a visual element that shows the player the outcome of their intended moves before 
committing them. Players can choose between Movement and Attack actions, with each action type visually distinguished 
by colors and paths.

---

## Indicator Types:

### Movement Preview:
* Marks the path a piece will take when moved
* Highlights all possible destination tiles of the selected piece (in yellow)
* (Illegal moves will also get displayed but labelled as "illegal")

### Attack Preview:
* Marks the path a piece will take when it attacks
* Highlights all possible tiles that can get attacked (in red)
* Shows how much damage the attacked piece will take
* (Illegal attacks will also get displayed but labelled as "illegal")

---

## Preview Activation:

* Hovering over a piece will show the attack and movement preview in different colors
    * (Draft) yellow for movement 
    * (Draft) red for attack
* Clicking on a piece opens a Sidebar with general information about the movement and attack patterns of the 
selected piece

---

## Sidebar Content:

### Piece Info:
* Piece Name

### Movement Patterns of Piece:
* Highlights all destination tiles of the selected piece (whether legal or not) in a small display of the field

### Attack Patterns of Piece:
* Highlights all tiles under attack of the selected piece (whether legal or not) in a small display of the field
* Shows how much damage the attack will make on each tile
