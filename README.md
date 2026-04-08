# Minecraft

A simple Java reproduction of the classic block game

### Notable Design Patterns
1. Entities are created using the factory pattern (EntityFactory)
2. ChunkRenderer and EntityRenderer are observers to Window, which pushes updates containing a WorldContext object
3. Vector uses the facade pattern: JOML Vector3f implements 3 interfaces, Vector only implements IVector
4. EntityManager uses the Iterator pattern to iterate over all entities
5. InputManager returns a list of commands based on the player's inputs

**Polymorphism Example**: Entities are polymorphic, with methods such as `Entity#getWeight()` and `AttackingEntity#getAttackDamage()` implemented by each concrete class.
AttackingEntity is a subclass of Entity.

**Dependency Injection Example**: The Minecraft class has a dependency injected at construction, requiring an IWindow. The IWindow (implemented by Window) has several 
dependencies that are injected by setters, as there are requirements for the order that the underlying LWJGL steps are executed in.

# Getting Started

To run the game, the jar file can be built with gradle, or if running from IntelliJ, the main function can be located in the Main class.