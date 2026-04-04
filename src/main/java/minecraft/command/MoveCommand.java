package minecraft.command;

import minecraft.chunk.Location;
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

        final Vector position_ = (Vector) camera.getPosition().clone();
        Vector new_position = (Vector) switch(direction)
        {
            case FORWARD -> position_.add(camera.getFront().clone().multiply(Entity.WALK_SPEED * deltaTime));
            case BACKWARD -> position_.subtract(camera.getFront().clone().multiply(Entity.WALK_SPEED * deltaTime));
            case LEFT -> position_.subtract(camera.getRightDirection().multiply(Entity.WALK_SPEED * deltaTime));
            case RIGHT -> position_.add(camera.getRightDirection().multiply(Entity.WALK_SPEED * deltaTime));
        };

        boolean moved = player.move(new_position);
        if (moved) {
            Vector camera_height = new Vector(
                    player.getLocation().getX(),
                    player.getLocation().getY(),
                    player.getLocation().getZ()
            );
            camera.setPosition(camera_height);
            camera.updateCameraVectors();

        }
    }
}
