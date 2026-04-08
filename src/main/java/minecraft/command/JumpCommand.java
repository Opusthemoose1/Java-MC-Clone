package minecraft.command;

import minecraft.entity.Player;

public class JumpCommand implements ICommand {

    private final Player player;

    public JumpCommand(Player player) {
        this.player = player;
    }

    @Override
    public void execute(float deltaTime) {
        player.jump();
    }
}
