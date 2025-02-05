# Testing

`Author: Albano Vukelaj`

## Testing Overview
All three modules (`common`, `client`, `server`) are extensively tested.
While [](Common.md) and [](Server.md) modules utilized automated testing through **JUnit** (and **Mockito** where needed),
the [](Client.md) module has been difficult to cover with unit tests due to the complexity of the game state 
and the challenges of validating graphical elements.
Additionally, the **GWT compiler's** output has only been tested manually due to time constraints.

## Common Module
The [](Common.md) module contains shared models, DTOs, and utilities used across the application, both from Server and Client.
This module was rigorously tested using **JUnit** to ensure the correctness and reliability of the shared functionality.

### Key Areas of Testing
- **[](Models.md):**
    - Verified data integrity and edge cases in classes such as [](GridModel.md),[](GridService.md), `LobbyModels`,[](PatternModel.md),[](PatternService.md) and `Vector2D`.
- **DTOs:**
    - Ensured correct serialization and deserialization of data through tests like `TestPatternListDTO`.
- **Services:**
    - Validated game logic and command verification processes in services such as [](Turn-Logic.md) and [CommandValidator](Turn-Logic.md#1-command-validation).
  
### Testing Approach
1. **Unit Tests:**
    - Core methods were tested to confirm expected outcomes.
2. **Edge Case Validation:**
    - Tested for null inputs and extreme values to ensure stability and robustness.

## Server Module
The [](Server.md) module is responsible for backend logic, WebSocket communication, and controller functionality. Testing in this module was automated using **JUnit**.

### Areas Covered
- **WebSocket Services:**
    - Verified connection stability.
- **Controllers:**
    - Ensured REST endpoints in classes like `HealthControllerTest` processed requests and responses correctly
- **Handlers:**
    - Validated event management and packet handling with tests such as `LobbyPacketHandlerTest` and
`TextWebSocketHandlerExtTest`
- **Utilities:**
    - Tested supporting classes like `EventTest` and `PairTest` to ensure reliability in backend operations

## Client Module
The [](Client.md) module relied on **manual testing** due to the limitations of testing visual features 
which restricted the ability to perform automated tests, as well as the quirks of GWT that provide cryptic error
messages

### Manual Testing Process
- **Scenario-Based Testing:**
  - Play-tested different game states to test game cycle, UI consistency and state transitions
- **Functional Testing:**
    - Ensured UI responsiveness and verified gameplay features, such as character interactions and grid interactions
- **Cross-Device Validation:**
    - Confirmed compatibility and responsiveness on various operational systems and screen sizes

### Key Focus Areas
1. Character and map rendering
2. Command system and previews
3. Input responses
4. UI features

