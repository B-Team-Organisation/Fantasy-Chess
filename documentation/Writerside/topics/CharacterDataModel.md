# Character Data Model

`Author: Jacinto Schwarzwälder`

A CharacterDataModel defines all static properties of a character. They represent a model used by the [CharacterEntities](CharacterEntity.md).
See the following class diagram for more information:

```plantuml
@startuml
class AttackDataModel {
    - String name;
    - String description;
    - int health;
    - int attackPower;
    - PatternService[] attackPatterns;
    - PatternService[] movementPatterns;
    - String attackDescription;
    - String movementDescription;
    + String getName()
    + String getDescription()
    + int getHealth()
    + int getAttackPower()
    + PatternService[] getAttackPatterns()
    + PatternService[] getMovementPatterns()
    + String getAttackDescription()
    + String getMovementDescription()
    + void setName(name: String)
    + void setDescription(description: String)
    + void setHealth(health: int)
    + void setAttackPower(int attackPower)
    + void setAttackPatterns(attackPatterns: PatternService[])
    + void setMovementPatterns(movementPatterns: PatternService[])
    + void setAttackDescription(attackDescription: String)
    + void setMovementDescription(movementDescription: String)
}
@enduml
```

As shown, a character has a name and description, as well as a fixed amount of health, attackPower (damage from attacks)
and lists of [](PatternService.md) for the attack and movement patterns. Additionally, there are descriptions for attacks
and movements.

