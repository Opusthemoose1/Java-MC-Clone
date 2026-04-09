package minecraft.command;

import minecraft.entity.Player;
import minecraft.math.IVector;

public class MoveBackwardsCommand implements ICommand {

    private final Player player;

    public MoveBackwardsCommand(Player player) {
        this.player = player;
    }

    @Override
    public void execute(float deltaTime) {
        IVector direction = player.getLocation()
                .getDirection()
                .setY(0)
                .normalize()
                .multiply(-player.getWalkSpeed());
        player.addInstantaneousVelocity(direction);
    }

}
