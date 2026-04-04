package minecraft.command;

import minecraft.entity.Entity;
import minecraft.entity.Player;
import minecraft.math.IVector;
import minecraft.math.Vector;
import minecraft.window.Camera;

public class MoveCommand implements ICommand {
    private final Player player;
    private final Camera camera;
    private final Camera.CameraDirection direction;
    public MoveCommand(Player player, Camera camera, Camera.CameraDirection direction)
    {
        this.player = player;
        this.camera = camera;
        this.direction = direction;
        // Set the camera position to the player position
        IVector camera_spawn = player.getLocation().toVector();
        camera_spawn.setY(camera_spawn.getY() + 1);
        camera.setPosition(camera_spawn);
    }
    @Override
    public void execute(float deltaTime)
    {
        final Vector front = (Vector) camera.getFront();
        final Vector right = (Vector) camera.getRightDirection();

        Vector displacement = (Vector) switch(direction)
        {
            case FORWARD -> front.multiply(Entity.WALK_SPEED * deltaTime);
            case BACKWARD -> front.multiply(-Entity.WALK_SPEED * deltaTime);
            case LEFT -> right.multiply(Entity.WALK_SPEED * deltaTime);
            case RIGHT -> right.multiply(-Entity.WALK_SPEED * deltaTime);
        };

        boolean moved = player.move(displacement);
        if (moved) {
            // The player's position is at their feet, so set the camera up by one block
            IVector camera_height = player.getLocation().toVector();
            camera_height.setY(camera_height.getY() + 1);

            camera.setPosition(camera_height);
            camera.processInput(direction, deltaTime);
           // camera.updateCameraVectors();
            System.out.println("Camera vector: " + camera.getFront().toJOML().toString());
            System.out.println("Velocity: " + Entity.WALK_SPEED * deltaTime);
            System.out.println("Player Position: " + player.getLocation().toVector().toJOML().toString());
        }
    }
}
