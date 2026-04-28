package minecraft.window.input;

import minecraft.command.ICommand;

import java.util.List;

public interface IInputManager {

    void bindKey(int key, ICommand command);

    void bindDownKey(int key, ICommand command);

    void bindUpKey(int key, ICommand command);

    List<ICommand> pollInputs();
}
