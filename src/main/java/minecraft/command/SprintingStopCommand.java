package minecraft.command;

import minecraft.entity.Entity;

public class SprintingStopCommand implements ICommand {

    @Override
    public void execute(Entity player) {
        player.setSprinting(false);
    }
}
