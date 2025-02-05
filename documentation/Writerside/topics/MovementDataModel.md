# Movement Data Model
`Author: Jacinto Schwarzwälder`

Analogous to the [AttackDataModel](AttackDataModel.md), Movement Data Models represent movements
and store the direction of a movement, as well as the ID of the moving [CharacterEntity](CharacterEntity.md).
As its attributes suggest, such movements can be applied to a [CharacterEntity](CharacterEntity.md)
to move it.
See the following class diagram for more information.

> We are only referencing the ID of the [CharacterEntity](CharacterEntity.md) as a
> String representation instead of more detailed information to save overhead.

```plantuml
@startuml

class MovementDataModel {
    - String characterId;
    - Vector2D movementVector;
    + MovementDataModel(characterId: String, movementVector: Vector2D)
    + String getCharacterId()
    + Vector2D getMovementVector()
    + void setCharacterId(String characterId)
    + void setMovementVector(Vector2D movementVector)
    
}
@enduml
```
