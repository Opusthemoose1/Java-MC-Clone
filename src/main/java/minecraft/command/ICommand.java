package minecraft.command;

import minecraft.entity.Entity;

public interface ICommand {
    void execute(Entity player);
}
