# Pattern Model

`Author: Lukas Walker`

A pattern model stores the unique name, a `String` representation of a [Pattern](Patterns.md) and
the subpattern-mappings to decode it (`char` -> pattern name). It comes with getters for all fields.

```java
    private final String patternName;
    private final String patternString;
    private final Map<Character, String> subpatternMappings;
```

Pattern models are stored in [](PatternStore.md) and used by [](PatternService.md).

## Creation

If no subpattern-mappings are provided, the constructor creates an empty `HashMap`.

```java
    public PatternModel(String patternString,
    Map<Character, String> subpatternMappings,
    String patternName){
        this.patternName = patternName;
        this.patternString = patternString;
        if (Objects.isNull(subpatternMappings)){
            this.subpatternMappings = new HashMap<>();
        } else {
            this.subpatternMappings = subpatternMappings;
        }
	}
```

A call to create a pattern model could look like this:


```java
    new PatternModel(" +++ \n++ ++\n+   +\n++ ++\n +++ ",
        new HashMap<>(){{put('+', "plus");}}, "bombAttack");
```

## Components

The name is used to identify the pattern when its used as a subpattern in another [pattern](Patterns.md).
```java
    public String getPatternName() {
        return patternName;
    }
```
The pattern itself is stored as a `String`, each `char` representing a single [tile](TileModel.md),
each line a row on the [grid](GridModel.md) with the character
always assumed in the center of the pattern.
> All lines in the `String` have to be of uneven length equal to the number of lines in the `String` itself
for the mapping to be unambiguous. 
{style="warning"}

```java
    public String getPatternString() {
        return patternString;
    }
```

A pattern string could look like this:
```
"xxx\n
 x x\n
 xxx"
```
This Pattern string without any subpattern-mappings would allow the associated command to target all 8 positions
adjacent to the [character](CharacterEntity.md). If the empty `char` in the middle would be non-empty, the command
could even target the position the [character](CharacterEntity.md) is standing on,
resulting in the [character](CharacterEntity.md) hurting himself.

Chars in the String can represent three different things:
1. Empty `char`: invalid target position
2. Non-empty `char` without mapped subpattern: valid target position effecting only itself
3. Non-empty `char` with mapped subpattern: valid target position effecting all valid target positions of 
the `char` subpattern

The subpattern-mappings contain mappings for all `chars` in the pattern that effect multiple tiles when targeted.
They are saved using a `HashMap`.

```java
    public Map<Character, String> getSubpatternMappings() {
        return subpatternMappings;
    }
```

Here is an example of a pattern that uses the subpattern-mapping "+" -> "plus":

This is the string representation of the pattern called "plus":
```
" x \n
 xxx\n
  x "
```

This is our new complex pattern:
```
" +++ \n
 ++ ++\n
 +   +\n
 ++ ++\n
  +++ "
```

An [attack](AttackDataModel.md) using this pattern would now result in 5 positions being attacked in 
a plus shape when issuing a [attack command](AttackDataModel.md) on a valid target position of the pattern.


