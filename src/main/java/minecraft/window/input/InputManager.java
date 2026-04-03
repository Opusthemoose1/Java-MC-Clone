package minecraft.window.input;

import minecraft.command.ICommand;

import java.util.HashMap;
import java.util.Map;

public class InputManager {
    private final Map<Integer, ICommand> keyBindings = new HashMap<>();

    private final IInput glfwInput;

    public InputManager(IInput input){
        glfwInput = input;
    }
    public void bind(int key, ICommand command)
    {
        keyBindings.put(key, command);
    }

    public void pollInputs(Input input, double deltaTime) {
        if (input == null) return;
        input.poll();
        keyBindings.forEach((key, command) -> {
            if (glfwInput.isKeyDown(key)) command.execute((float)deltaTime);
        });
    }
}
