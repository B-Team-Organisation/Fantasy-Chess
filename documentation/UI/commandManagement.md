# Concept for the Command Management

This document outlines the design and functionality of the Command Management system for players. 
The primary objective is to provide a clear and intuitive interface for managing character commands and overview about the wished game-tactic

---
 see Figma-graphic for better understanding  https://www.figma.com/design/UBKCqhJdGT1nt1KMNF5DEH/Untitled?node-id=0-1&node-type=canvas&t=QWnWi1FAYMrMBg9c-0

---
## **Board Layout**
- **Tile Visibility**:
    - Tiles selected for movement or attack remain visible on the board but are visually darkened to indicate they are not selected.
    - When hovering over a character, their assigned move or attack is highlighted with increased brightness for better visibility.
    - **Option 2**: Selected tiles are hidden from view. Hovering over a character reveals their assigned move or attack, displayed dynamically.
---

## **Countdown Functionality**
- **Countdown:** A timer is displayed to enforce decision-making within a limited timeframe.
    - Timer starts at the beginning of the turn and counts down to **0:00**.
    - If the timer expires:
        - **TO CLARIFY**: Should all valid moves be submitted automatically, or should no moves be processed if the player hasn’t clicked **"Send Moves"**?
    - Players can manually submit their commands before the timer expires using the **"Send Moves"** button.
    - If all the commands given are valid and every character has a valid move assigned :
        - Button **"Send Moves"** again disabled and changes to **""Ready"**( until the Opponent is ready)

- **(NOT MVP)Timer Alerts:**:
    - Audio or visual alerts when the timer is close to expiration
    - **Offer Pause** A pause feature may be implemented in multiplayer matches for consensus breaks

---

## **Command Summary Panel**
- **Overview of Commands**:
    - Displays the total number of characters with assigned commands versus total needed (7/10)
    - if clicked:
        - **Navigation:** automatically highlights one random of the remaining Character without Command

- **Interaction shown**:
    - The **"Send moves"** button is deactivated until all characters have assigned commands.
    - (NOT MVP)Hovering over inactive buttons shows a tooltip explaining why the button is unavailable ("Not all characters have assigned commands")
    - Once all commands are assigned, the **"Send moves"** button activates and becomes clickable.-> see Countdown
- **Edit Command**:
    - Ability to modify or cancel commands if a player is clicked 

