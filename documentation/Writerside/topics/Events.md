# Events

Events are custom classes, that allow for the registration of [](#listener) and invocation of these listeners.
As Java does not provide a native Event System out of the box, this one was created. They are type of generic class
in which listeners are consumers.


Creating a Listener
: 
```java
final Event<String> OnDocumentationRead = new Event<>();
```

Creating a listener with a custom error Handler
:
```java
final Event<String> OnCustomInvocationErrorHandler 
    = new Event<>((ex) -> ex.getMessage());
```

Invoking an event
:
```java
    OnDocumentationRead.Invoke(
        "You have successfully read the documentation");
```

Invoking with a custom error callback
:
```java
    OnDocumentationRead.Invoke(
        "You have successfully read the documentation", 
        (ex) -> System.out.println(ex.getMessage()));
```

> Events are not required to be final, however, they should be set as such to prevent unexpected behaviour.

## Listener

Listeners are a type of Java [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html),
where the consumed type is defined by the corresponding event.

Adding A Listener
:
```java
    final Event<String> OnDocumentationRead = new Event<>();
    OnDocumentationRead.AddListener((s) -> System.out.println(s));
```

