package minecraft.command;

public class CommandFactory {

    public ICommand newJumpCommand() {
        return new JumpCommand();
    }

    public ICommand newMoveForwardsCommand() {
        return new MoveForwardCommand();
    }

    public ICommand newMoveBackwardsCommand() {
        return new MoveBackwardsCommand();
    }

    public ICommand newMoveLeftCommand() {
        return new MoveLeftCommand();
    }

    public ICommand newMoveRightCommand() {
        return new MoveRightCommand();
    }

    public ICommand newSprintStartCommand() {
        return new SprintingStartCommand();
    }

    public ICommand newSprintStopCommand() {
        return new SprintingStopCommand();
    }

    public ICommand newBreakBlockCommand() {
        return new BreakBlockCommand();
    }

    public ICommand newPlaceBlockCommand() {
        return new PlaceBlockCommand();
    }

    public ICommand newAttackCommand() {
        return new AttackCommand();
    }

}
