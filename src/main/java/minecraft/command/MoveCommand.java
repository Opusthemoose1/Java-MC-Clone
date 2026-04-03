package minecraft.command;

import minecraft.entity.Entity;
import minecraft.entity.Player;
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
    }
    @Override
    public void execute(float deltaTime)
    {
        Vector displacement = (Vector) switch(direction)
        {
            case FORWARD -> camera.getFront().multiply(Entity.WALK_SPEED * deltaTime);
            case BACKWARD -> camera.getFront().multiply(-Entity.WALK_SPEED * deltaTime);
            case LEFT -> camera.getRightDirection().multiply(Entity.WALK_SPEED * deltaTime);
            case RIGHT -> camera.getRightDirection().multiply(-Entity.WALK_SPEED * deltaTime);
        };
        boolean moved = player.move(displacement);
        if (moved) {
            camera.setPosition(player.getLocation().toVector());
        }
    }
}
