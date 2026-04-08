package minecraft.window.input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import minecraft.command.ICommand;

public class InputManager {
    private final Map<Integer, ICommand> keyBindings = new HashMap<>();

    private final IInput inputSource;

    public InputManager(IInput input) {
        inputSource = input;
    }
    public void bind(int key, ICommand command)
    {
        keyBindings.put(key, command);
    }

    public List<ICommand> pollInputs(Input input) {
        if (input == null) return List.of();
        List<ICommand> commands = new ArrayList<>();

        input.poll();
        keyBindings.forEach((key, command) -> {
            if (inputSource.isKeyDown(key)) commands.add(command);
        });

        return commands;
    }
}
