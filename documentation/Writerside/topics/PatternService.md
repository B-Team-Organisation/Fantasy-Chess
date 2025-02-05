# Pattern Services

`Author: Lukas Walker`

Pattern Services wrap a [Pattern Model](PatternModel.md) and provide methods to manipulate and retrieve information
about the [Pattern](Patterns.md).
A pattern service holds its underlying [Pattern Model](PatternModel.md), a reference to 
the [Pattern Store](PatternStore.md) and two Maps, one mapping from relative position to `char` and one 
mappings from relative position to effected positions.
They are created directly by the client or server and are never send over the network.

Their main task is to provide target positions and positions effected by commands on target positions.



## Creation

Pattern Services are created from a [Pattern Model](PatternModel.md) and a [Pattern Store](PatternStore.md):

```java
    public PatternService(PatternModel patternModel, PatternStore patternStore) 
        throws PatternShapeInvalidException, InvalidSubpatternMappingException {
            this.patternStore = patternStore;
            this.patternModel = patternModel;
            validatePattern(patternModel);
            relativeTargetMappings = createMappingsFromPattern(patternModel);
    
            attackAreasPerRelativePosition = new HashMap<>();
            for (Vector2D target : relativeTargetMappings.keySet()) {
                attackAreasPerRelativePosition
                    .put(target,calculateAreaOfEffect(target));
            }
    }
```

When constructed like this, they are directly validated for possible ambiguity due to pattern shape or errors in 
the subpattern-mappings. If nothing arises from these checks, the two maps are calculated and saved.

Pattern Services can also be created directly using components, which is important for [reversal](#reversal):

```java
    private PatternService(
            PatternModel patternModel, PatternStore patternStore,
            Map<Vector2D, Character> relativeTargetMappings,
            Map<Vector2D, Vector2D[]> attackAreasPerRelativePosition
        ) {
        this.patternStore = patternStore;
        this.patternModel = patternModel;
        this.relativeTargetMappings = relativeTargetMappings;
        this.attackAreasPerRelativePosition = attackAreasPerRelativePosition;
    }
```

Method to calculate mappings from relative position to `char`:

```java
    private Map<Vector2D,Character> createMappingsFromPattern(
        PatternModel patternModel) {
            String[] lines = patternModel.getPatternString().split("\n");
    
            Map<Vector2D,Character> mappings = new HashMap<>();
            for (int y = 0; y < lines.length; y++) {
                String line = lines[y];
                for (int x = 0; x < lines.length; x++) {
                    char tileChar = line.charAt(x);
                    if (tileChar != ' '){
                        mappings.put(
                            new Vector2D(x-lines.length/2, y-lines.length/2), 
                            tileChar
                        );
                    }
                }
            }
            return mappings;
    }
```

Methods to calculate effected areas:

```java
    private Vector2D[] calculateAreaOfEffect(Vector2D targetPosition){
        char c = relativeTargetMappings.get(targetPosition);

        Set<Vector2D> targets = new HashSet<>();

        if (patternModel.getSubpatternMappings().containsKey(c)) {
            PatternModel subpatternModel = patternStore
                .getPatternByName(patternModel.getSubpatternMappings().get(c));
            collectTargetsFromSubpattern(subpatternModel,targetPosition,targets);
        } else {
            targets.add(targetPosition);
        }

        return targets.toArray(new Vector2D[0]);
    }
    
    private void collectTargetsFromSubpattern(PatternModel patternModel, 
        Vector2D targetPosition, Set<Vector2D> targets) {
            Map<Vector2D,Character> mappings = createMappingsFromPattern(
                patternModel
            );
            for (Vector2D relativeSubpatternPosition : mappings.keySet()) {
                char c = mappings.get(relativeSubpatternPosition);
                Vector2D subpatternTarget = targetPosition
                    .add(relativeSubpatternPosition);
                if (patternModel.getSubpatternMappings().containsKey(c)) {
                    PatternModel subpatternModel = patternStore
                        .getPatternByName(patternModel
                            .getSubpatternMappings().get(c));
                    collectTargetsFromSubpattern(subpatternModel,
                        subpatternTarget,targets
                    );
                } else {
                    targets.add(subpatternTarget);
                }
            }
    }
```

## Usage

Use the method "getPossibleTargetPositions" to retrieve all absolute target positions defined by the pattern based on player position:

```java
    public Vector2D[] getPossibleTargetPositions(Vector2D player){
        return Arrays.stream(relativeTargetMappings.keySet()
            .toArray(new Vector2D[0])).map(player::add).toArray(Vector2D[]::new);
    }
```

Use the method "getAreaOfEffect" to retrieve a list of all absolute positions effected by a specified command:

```java
    public Vector2D[] getAreaOfEffect(Vector2D player, Vector2D targetPosition) {
        Vector2D relativePosition = targetPosition.subtract(player);
        if (!attackAreasPerRelativePosition.containsKey(relativePosition)) {
            return new Vector2D[0];
        }

        Vector2D[] relativePositions = attackAreasPerRelativePosition
            .get(relativePosition);
        return Arrays.stream(relativePositions).map(player::add)
            .toArray(Vector2D[]::new);
    }
```

## Reversal

The server has to transform all coordinates for one player to be able to process their commands. This includes the patterns of their [characters](CharacterEntity.md).
Pattern services provide a method to create a new pattern service that holds the same [pattern](Patterns.md) in reverse.

```java
    public PatternService reversePattern() {
        Map<Vector2D,Vector2D[]> newAttackAreasPerRelativePosition = new HashMap<>();
        Map<Vector2D, Character> newRelativeTargetMappings = new HashMap<>();


        for (Vector2D target : attackAreasPerRelativePosition.keySet()) {

            Vector2D[] oldSubtargets = attackAreasPerRelativePosition
                .get(target);
            Vector2D[] newSubtargets = new Vector2D[oldSubtargets.length];

            for (int i = 0; i < oldSubtargets.length; i++) {
                newSubtargets[i] = reversePosition(oldSubtargets[i]);
            }

            newAttackAreasPerRelativePosition
                .put(reversePosition(target),newSubtargets);
        }

        for (Vector2D target : relativeTargetMappings.keySet()) {
            newRelativeTargetMappings.put(reversePosition(target),
                relativeTargetMappings.get(target)
            );
        }

        return new PatternService(
                patternModel, patternStore, newRelativeTargetMappings,
                newAttackAreasPerRelativePosition
        );
    }
    
    private Vector2D reversePosition(Vector2D position){
        return new Vector2D(-position.getX(),-position.getY());
    }
```

This method effectively turns the pattern including all subpattern-information by 180 degrees.

## Validations

Method to validate [Pattern Model](PatternModel.md) using all validation methods

```java
    private void validatePattern(PatternModel patternModel) 
        throws PatternShapeInvalidException, InvalidSubpatternMappingException {
            String[] lines = patternModel.getPatternString().split("\n");
    
            if (isPatternShapeInvalid(lines)) {
                throw new PatternShapeInvalidException();
            }
            if (arePatternMappingsInvalid(patternModel)){
                throw new InvalidSubpatternMappingException();
            }
    }
```

Method to validate pattern shape

```java
    private boolean isPatternShapeInvalid(String[] lines){
        if (lines.length % 2 == 0){
            return true;
        }
        int length = lines.length;
        for (String line : lines) {
            if (length != line.length()) {
                return true;
            }
        }
        return false;
    }
```

Method to recursively check subpattern-mappings

```java
    private boolean arePatternMappingsInvalid(PatternModel patternModel) {
        String[] lines = patternModel.getPatternString().split("\n");
        for (String line:lines){
            for (char c : line.toCharArray()){
                if (c == ' '){
                    continue;
                }
                if (!patternModel.getSubpatternMappings().containsKey(c)){
                    continue;
                }
                String subpatternName = patternModel
                    .getSubpatternMappings().get(c);

                PatternModel subpatternModel = patternStore
                    .getPatternByName(subpatternName);
                if (subpatternModel == null){
                    return true;
                }
                return arePatternMappingsInvalid(subpatternModel);
            }
        }
        return false;
    }
```