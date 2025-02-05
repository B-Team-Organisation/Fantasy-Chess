# Json Writer

`Author: Marc Matija`

Utility class for constructing JSON strings in a type-safe manner. This class was made out of necessity, as
the client cannot use Reflection due to limitations with the LibGDX and GWT version. This results in being unable to
automatically generate JSON from java classes, thus this solution was created.

## Basic Usage


Creating a String
:
```java
String s = new JsonWriter().writeKeyValue("string", "value")
    .and().writeKeyValue("primitive", 20).toString();
```
```json
{
  "string" : "value",
  "primitive" : 20 
}
```