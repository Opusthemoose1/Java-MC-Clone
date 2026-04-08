package minecraft.command;

import minecraft.entity.Player;
import minecraft.math.IVector;

public class MoveLeftCommand implements ICommand {

    private final Player player;

    public MoveLeftCommand(Player player) {
        this.player = player;
    }

    @Override
    public void execute(float deltaTime) {
        IVector direction = player.getLocation()
                .getRightDirection()
                .setY(0)
                .normalize()
                .multiply(-Player.PLAYER_WALK_SPEED);
        player.addInstantaneousVelocity(direction);
    }
}
