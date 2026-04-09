package minecraft.window.input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import minecraft.command.ICommand;

public class InputManager implements IInputManager {
    private final Map<Integer, ICommand> upKeyBindings = new HashMap<>(), downKeyBindings = new HashMap<>();

    private final IInputSource inputSource;

    public InputManager(IInputSource input) {
        inputSource = input;
    }

    public void bindUpKey(int key, ICommand command) {
        upKeyBindings.put(key, command);
    }

    public void bindDownKey(int key, ICommand command) {
        downKeyBindings.put(key, command);
    }

    public List<ICommand> pollInputs() {
        if (inputSource == null) return List.of();
        List<ICommand> commands = new ArrayList<>();

        inputSource.poll();
        upKeyBindings.forEach((key, command) -> {
            if (!this.inputSource.isKeyDown(key)) commands.add(command);
        });

        downKeyBindings.forEach((key, command) -> {
            if (this.inputSource.isKeyDown(key)) commands.add(command);
        });

        return commands;
    }
}
