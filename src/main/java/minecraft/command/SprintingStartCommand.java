package minecraft.command;

import minecraft.entity.Entity;

public class SprintingStartCommand implements ICommand {

    @Override
    public void execute(Entity player) {
        player.setSprinting(true);
    }
}
