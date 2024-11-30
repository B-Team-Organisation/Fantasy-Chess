# Turn Outcome Visualization Concept

This document outlines how to visualize the outcomes of a turn in the game. The goal is to display movements, attacks, damage,
and other game-related actions in a clear and engaging way. Transitions between actions should be smooth and visually appealing.


## Movements

- **Representation**:
    - Characters visually transfer from their current position to the target position.
    - (NOT MVP):Glide,Movement speed

- **Effects**:
    - The destination tile is briefly highlighted ( **yellow**).
    - (NOT MVP)A subtle shadow trail effect can enhance movement dynamics.

---

## Attacks

- **Attack Representation**:
    - The attacker is highlighted with red glow(possible like a light and blink?) or a brief animation.
    - The attack target is marked:
        - **Option 1**: The target tile blinks in red.
        - **Option 2**: The target character darkens briefly, (eventually in red)
      and a symbol (sword ?) appears above it.

- **Damage Display**:
    - The damage value appears above the target character for 0.5s(example) second (`-25 HP`).
    - (NOT MVP)Damage value colors:
        - **Red**: Critical damage.
        - **Orange**: normal damage.
        - **Green**: Healing or shields.

- **Character Death Animation**:
    - A defeated character remains visible for 0.5 seconds, gradually darkens to black, and then disappears.
    - **Option2**:(NOT MVP) Explosion effect or the character shatters into fragments.

---

## Turn Sequence

### Step-by-Step Visualization
1. **Movements**:**WE BEGIN WITH MOVEMENT SHOW**
    - All movements in the round are executed sequentially.(TO DECIDE)
    - Order: Alternate between Player 1 and Player 2 characters, if no more character then just the rest
    - Each movement visualization should take no more than 0.5 second.

2. **Attacks**:
    - Attacks are displayed alternately between players.
    - If an attack affects multiple targets then the effects should appear simultaneously on all targets.
   See Damage Display if a Character is hit

3. **Additional Effects**:
    - Area-of-effect attacks are visualized by highlighting affected tiles with color(Same on as preview indicator) and animation.
    - **Option1** :Non-involved characters are subtly faded to a darker color(or maybe grey) for better focus.
    - **Option2**:The Intensity of the two(or more) Character is vivid in the attack , the rest remains the same.

---

## Animations and Transitions

- **Timing**:TO SEE AND TEST
    - Movement: 0.5 seconds per tile.
    - Attack: 1 second per attack (including damage display).
    - Death: 0.5 seconds to fade out and disappear.

- **Smooth Transitions**:
    - Add a minimal pause between actions to make each step distinct.
    - Ensure attacks seamlessly follow movements to maintain game pacing.

