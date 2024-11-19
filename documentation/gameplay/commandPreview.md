# Command Preview Indicator Concept

## Overview

The Command Preview Indicator is a visual element that shows the player the outcome of their intended moves before 
committing them. Players can choose between Movement and Attack actions, with each action type visually distinguished 
by colors and paths.

---

## Indicator Types:

### Movement Preview:
* Highlights all possible destination tiles of the selected piece (in yellow)
* Illegal moves will be filtered out earlier
* Strong highlight when hovering over destination tile


### Attack Preview:
* Highlights all possible tiles that can get attacked (in red)
* Shows how much damage the hit pieces will take
* Illegal moves will be filtered out earlier
* Strong highlight of to be attacked tiles when hovering over destination tile

---
## Implementation idea:

* create highlight as tiles to render on top of the map tiles
* same for damage

---

## Figma Concept:

https://www.figma.com/design/n5RoY75WL2p77ALjyK8HXU/Command-Preview-Indicator--Concept?node-id=0-1&t=5t2vm8P9FjaiH7s5-1
