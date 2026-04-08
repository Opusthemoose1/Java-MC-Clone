package minecraft.command;

import minecraft.entity.Player;
import minecraft.math.IVector;

public class MoveForwardCommand implements ICommand {

    private final Player player;

    public MoveForwardCommand(Player player) {
        this.player = player;
    }

    @Override
    public void execute(float deltaTime) {
        IVector direction = player.getLocation()
                .getDirection()
                .setY(0)
                .normalize()
                .multiply(Player.PLAYER_WALK_SPEED);
        System.out.println("move forward" + direction.toString());
        player.setWalkingVelocity(direction);
    }
}
