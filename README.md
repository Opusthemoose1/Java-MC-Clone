# Minecraft

A simple Java reproduction of the classic block game

```
Ryen Johnston, Alex McDonald
Java Version: 25
```

**Note**: Prof. Wright told us that it wouldn't be necessary to have 100% test method coverage for graphics/UI classes, as these are difficult to write tests for. By running and interacting with the full game enough, 100% test coverage should still be possible.

### Notable Design Patterns
1. Entities are created using the factory pattern (EntityFactory)
2. ChunkRenderer and EntityRenderer are observers to Window, which pushes updates containing a WorldContext object
3. Vector uses the facade pattern: JOML Vector3f implements 3 interfaces, Vector only implements IVector
4. EntityManager uses the Iterator pattern to iterate over all entities
5. InputManager uses the command pattern to take actions off of user keyboard inputs
6. Commands are created with the factory pattern (CommandFactory), bound to an IInputManager

**Polymorphism Example**: Entities are polymorphic, with methods such as `Entity#getWeight()` and `AttackingEntity#getAttackDamage()` implemented by each concrete class.
AttackingEntity is a subclass of Entity.

**Dependency Injection Example**: The Minecraft class has a dependency injected at construction, requiring an IWindow. The IWindow (implemented by Window) has several 
dependencies that are injected by setters, as there are requirements for the order that the underlying LWJGL steps are executed in.

# Getting Started

To run the game, the jar file can be built with gradle, or if running from IntelliJ, the main function can be located in the Main class.