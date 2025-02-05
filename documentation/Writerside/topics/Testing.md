# Testing

`Author: Albano Vukelaj`

## Testing Overview
All three modules (`common`, `client`, `server`) were extensively tested.
While `common` and `server` modules utilized automated testing through **JUnit**,
the `client` module has been difficult to cover with unit tests due to the complexity of the game state 
and the challenges of validating graphical elements.
Additionally, the **GWT compiler's** output has only been tested manually due to time constraints.

---

## Common Module
The [](Common.md) module contains shared models, DTOs, and utilities used across the application, both from Server and Client.
This module was rigorously tested using **JUnit** to ensure the correctness and reliability of the shared functionality.

### Key Areas of Testing
- **[](Models.md):**
    - Verified data integrity and edge cases in classes such as `GridModel`,`GridService`, `LobbyModels`,`PatternModel`,`PatternService`and `Vector2D`.
- **DTOs:**
    - Ensured correct serialization and deserialization of data through tests like `TestPatternListDTO`.
- **Services:**
    - Validated game logic and command verification processes in services such as [](Turn-Logic.md) and[CommandValidator](Turn-Logic.md#commandvalidation).
  
### Testing Approach
1. **Unit Tests:**
    - Core methods were tested to confirm expected outcomes.
2. **Edge Case Validation:**
    - Tested for null inputs and extreme values to ensure stability and robustness.

---

## Client Module
The [](Client.md) module relied on **manual testing**.

### Manual Testing Process
- **Functional Testing:**
  - Ensured the game loop runs without errors.
  - Verified server communication and response handling.
  - Tested expected reactions to various inputs.
- **Rendering & Animations:**
  - Ensured correct rendering of all elements.
  - Verified accurate CommandPreview and TurnOutcome.
- **UI Testing:**
  - Checked the accuracy and visibility of stat overlays.
  - Ensured smooth navigation of scrollable UI components (lists and dialogs).

### Key Focus Areas
- Server communication & response handling
- Correct input handling
- Character representation
- Health bar rendering & updates
- Stat overlays accuracy & visibility
- Scrollable UI components (lists & dialogs) for smooth navigation

---

## Server Module
The [](Server.md) module is responsible for backend logic, WebSocket communication, and controller functionality. Testing in this module was automated using **JUnit**.

### Areas Covered
- **WebSocket Services:**
    - Verified connection stability.
- **Controllers:**
    - Ensured REST endpoints in classes like `HealthControllerTest` processed requests and responses correctly.
- **Handlers:**
    - Validated event management and packet handling with tests such as `LobbyPacketHandlerTest` and `TextWebSocketHandlerExtTest`.

---

