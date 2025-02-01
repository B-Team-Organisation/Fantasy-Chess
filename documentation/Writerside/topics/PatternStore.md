# Pattern Stores

Pattern Stores are used to store and provide all [](Patterns.md). 

```java
    public interface PatternStore {
        PatternModel getPatternByName(String patternName);
    }
```

Both client and server implement their own pattern store.