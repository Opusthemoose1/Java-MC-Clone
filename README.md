# Minecraft

A simple Java reproduction of the classic block game

### 3 Design Patterns
1. Entities are created using the factory pattern (EntityFactory)
2. Window uses the Observer Pattern to listen for changes to the window size
3. Vector uses the facade pattern: JOML Vector3f implements 3 interfaces, Vector only implements IVector

**Polymorphism Example**: Entities are polymorphic, with methods such as `Entity#getWeight()` and `AttackingEntity#getAttackDamage()` implemented by each concrete class.
AttackingEntity is a subclass of Entity.

**Dependency Injection Example**: The Minecraft class has a dependency injected at construction, requiring an IWindow. The IWindow (implemented by Window) has several 
dependencies that are injected by setters, as there are requirements for the order that the underlying LWJGL steps are executed in.

# Getting Started

To run the game, the jar file can be built with gradle, or if running from IntelliJ, the main function can be located in the Main class.