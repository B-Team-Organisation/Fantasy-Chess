# Testing

## Testing Overview
The project was tested extensively in three primary modules: `common`, `server`, and `client`. 
While `common` and `server` modules utilized automated testing through **JUnit**, 
the `client` module relied on manual testing due to **GWT** limitations.

---

## Common Module
The [](Common.md) module contains shared models, DTOs, and utilities used across the application,both from Server and Client.
This module was rigorously tested using **JUnit** to ensure the correctness and reliability of the shared functionality.

### Key Areas of Testing
- **[](Models.md):**
    - Verified data integrity and edge cases in classes such as `GridModel`,`GridService`, `LobbyModels`,`PatternModel`,`PatternService`and `Vector2D`.
- **DTOs:**
    - Ensured correct serialization and deserialization of data through tests like `TestPatternListDTO`.
- **Services:**
    - Validated game logic and command verification processes in services such as [](Turn-Logic-Service.md) and `CommandValidator`.
- **Utilities:**
    - Covered helper classes like `TestUtils` and `TurnResultNoOrder` to ensure robust utility methods.

### Testing Approach
1. **Unit Tests:**
    - Each method was tested to confirm expected outcomes.
2. **Edge Case Validation:**
    - Tested for null inputs and extreme values to ensure stability and robustness.

---

## Server Module
The [](Server.md) module is responsible for backend logic, WebSocket communication, and controller functionality. Testing in this module was automated using **JUnit**.

### Areas Covered
- **WebSocket Services:**
    - Verified connection stability and proper handling of message payloads.
- **Controllers:**
    - Ensured REST endpoints in classes like `HealthControllerTest` processed requests and responses correctly.
- **Handlers:**
    - Validated event management and packet handling with tests such as `LobbyPacketHandlerTest` and `TextWebSocketHandlerExtTest`.
- **Utilities:**
    - Tested supporting classes like `EventTest` and `PairTest` to ensure reliability in backend operations.

---

## Client Module
The [](Client.md) module relied on **manual testing** due to the limitations of **GWT** , which restricted the ability to perform automated tests.

### Manual Testing Process
- **Functional Testing:**
    - Ensured UI responsiveness and verified gameplay features, such as character interactions and grid mechanics.
- **Scenario-Based Testing:**
    - Simulated different game states ( beginning, mid-game, and endgame) to test UI consistency and state transitions.
- **Cross-Device Validation:**
    - Confirmed compatibility and responsiveness on various devices and screen sizes.

### Key Focus Areas
1. Health bar rendering and updates.
2. Character representation
3. Character stat overlays for accuracy and visibility.
4. Scrollable UI components ( lists and dialogs) for smooth navigation.

Manual testing played a critical role in identifying UI/UX issues that automated tests could not detect.

---

