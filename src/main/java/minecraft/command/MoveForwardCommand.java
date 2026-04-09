package minecraft.command;

import minecraft.entity.Entity;
import minecraft.math.IVector;

public class MoveForwardCommand implements ICommand {

    @Override
    public void execute(Entity player) {
        IVector direction = player.getLocation()
                .getDirection()
                .setY(0)
                .normalize()
                .multiply(player.getWalkSpeed());
        player.addInstantaneousVelocity(direction);
    }
}
