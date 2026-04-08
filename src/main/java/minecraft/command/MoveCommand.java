package minecraft.command;

import minecraft.entity.Entity;
import minecraft.entity.Player;
import minecraft.math.Vector;
import minecraft.window.Camera;

public class MoveCommand implements ICommand {
    private final Player player;
    private final Camera.CameraDirection direction;
    public MoveCommand(Player player, Camera.CameraDirection direction)
    {
        this.player = player;
        this.direction = direction;
        // Set the camera position to the player position
    }

    @Override
    public void execute(float deltaTime) {



//        Vector newPosition = (Vector) switch(direction)
//        {
//            case FORWARD -> position.add(camera.getFront().clone().multiply(Entity.WALK_SPEED * deltaTime));
//            case BACKWARD -> position.subtract(camera.getFront().clone().multiply(Entity.WALK_SPEED * deltaTime));
//            case LEFT -> position.subtract(camera.getRightDirection().multiply(Entity.WALK_SPEED * deltaTime));
//            case RIGHT -> position.add(camera.getRightDirection().multiply(Entity.WALK_SPEED * deltaTime));
//        };

//        boolean moved = player.move(newPosition);
//        if (moved) {
//            Vector cameraHeight = new Vector(
//                    player.getLocation().getX(),
//                    player.getLocation().getY(),
//                    player.getLocation().getZ()
//            );
//        }
    }
}
