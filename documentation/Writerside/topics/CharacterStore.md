# Character Store

The `CharacterStore` holds all character definitions. When implementing a new character, the [](CharacterDataModel.md)
is created in it.

## Components

The `CharacterStore` has three main components:

Two `HashMaps`, one containing all defined characters mapped to by their name, the other
one containing all defined patterns for future use. The last component is a big static code block
in which all characters are defined.

```java
public static final Map<String, CharacterDataModel> characters = new HashMap<>();
public static final Map<String, PatternService> patterns = new HashMap<>();
static {
    /*
     * Definitions
     */
}
```

To retrieve a specific character, simply use this method:

```java
public static CharacterDataModel getCharacter(String characterName) {
    return characters.get(characterName);
}
```
