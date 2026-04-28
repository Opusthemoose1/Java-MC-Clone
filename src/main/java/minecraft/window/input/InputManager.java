package minecraft.window.input;

import java.util.*;

import minecraft.command.ICommand;

import static org.lwjgl.glfw.GLFW.*;
public class InputManager implements IInputManager {

    enum KeyBindingMode {
        ONCE,
        DOWN,
        UP;
    }

    record KeyBinding(ICommand command, KeyBindingMode mode) {}

    private final Map<Integer, List<KeyBinding>> keyBindings = new HashMap<>();
    private final Set<Integer> commandsExecutedForKeys = new HashSet<>();

    private final IInputSource inputSource;

    public InputManager(IInputSource input) {
        inputSource = input;
    }

    private void bindKey(int key, KeyBinding binding) {
        List<KeyBinding> list = keyBindings.getOrDefault(key, new ArrayList<>());
        list.add(binding);
        if (list.size() == 1) keyBindings.put(key, list);
    }

    public void bindUpKey(int key, ICommand command) {
        bindKey(key, new KeyBinding(command, KeyBindingMode.UP));
    }

    @Override
    public void bindKey(int key, ICommand command) {
        bindKey(key, new KeyBinding(command, KeyBindingMode.ONCE));
    }

    public void bindDownKey(int key, ICommand command) {
        bindKey(key, new KeyBinding(command, KeyBindingMode.DOWN));
    }

    public List<ICommand> pollInputs() {
        if (inputSource == null) return List.of();
        List<ICommand> commands = new ArrayList<>();

        inputSource.poll();
        keyBindings.forEach((key, bindings) -> {
            boolean keyDown = inputSource.isKeyDown(key);
            for (KeyBinding binding : bindings) {
                if (keyDown) {
                    if (binding.mode == KeyBindingMode.DOWN) {
                        commands.add(binding.command);
                    } else if (binding.mode == KeyBindingMode.ONCE && !commandsExecutedForKeys.contains(key))
                        commands.add(binding.command);
                    commandsExecutedForKeys.add(key);
                } else {
                    commandsExecutedForKeys.remove(key);
                    if (binding.mode == KeyBindingMode.UP) commands.add(binding.command);
                }
            }
        });

        return commands;
    }
}
